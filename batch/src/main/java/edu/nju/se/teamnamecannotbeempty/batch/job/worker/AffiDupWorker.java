package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAffiliation;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AffiPopDao;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;

@Component
public class AffiDupWorker {
    private final DuplicateAffiliationDao duplicateAffiliationDao;
    private final AffiPopDao affiPopDao;
    private final AffiliationDao affiliationDao;
    private final AffiPopWorker affiPopWorker;
    private HashMap<Affiliation, HashSet<String>> tokenSetMap;
    private StandardAnalyzer analyzer;

    @Autowired
    public AffiDupWorker(DuplicateAffiliationDao duplicateAffiliationDao, AffiliationDao affiliationDao, AffiPopWorker affiPopWorker, AffiPopDao affiPopDao) {
        this.duplicateAffiliationDao = duplicateAffiliationDao;
        this.affiliationDao = affiliationDao;
        this.affiPopWorker = affiPopWorker;
        this.affiPopDao = affiPopDao;
    }

    @Async
    public void generateAffiDup(Future<?> waitForImport) {
        while (waitForImport.isDone()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Generate duplicate authors aborted due to " + e.getMessage());
                return;
            }
        }
        /*
        下面进行分词
         */
        analyzer = new StandardAnalyzer();
        List<Affiliation> affis = affiliationDao.findAll();
        tokenSetMap = new HashMap<>(affis.size());
        affis.forEach(affi -> {
            try {
                if (!affi.getName().equals("NA")) {
                    tokenSetMap.put(affi, getTokenSet(affi));
                }
            } catch (IOException e) {
                logger.warn("Get token set for " + affi + " failed");
            }
        });
        ArrayListValuedHashMap<Affiliation, Affiliation> cache = new ArrayListValuedHashMap<>();
        for (Affiliation affi : affis) {
            //两两对比
            HashSet<String> affiSet = tokenSetMap.get(affi);
            if (affiSet == null) {
                continue;
            }
            for (Affiliation compare : affis) {
                if (compare.getId().equals(affi.getId()) || compare.getName().equals("NA"))
                    continue;
                if (!cache.containsKey(compare) ||
                        (cache.containsKey(compare) && !cache.get(compare).contains(affi))) { //判断是否已经有倒置的重复
                    HashSet<String> compareSet = tokenSetMap.get(compare);
                    if (compareSet != null && affiSet.containsAll(compareSet) && notBelongsTo(affiSet, compareSet)) {
                        //判断全包含，但是没有考虑到词序，所以有可能误杀 //TODO
                        cache.put(affi, compare);
                        duplicateAffiliationDao.save(new DuplicateAffiliation(affi, compare));
                    }
                }
            }
        }
        analyzer.close();
        logger.info("Done generate duplicate affiliations");
    }

    /**
     * 判断father对象不是son对象的下属机构
     * 主要思想是father的词集里有部门/学院/实验室等表示它可能是下属机构的词，而son没有，就说明存在隶属关系
     *
     * @param father 词集全包含son
     * @param son 词集被father全包含
     * @return father是否隶属于son
     */
    private boolean notBelongsTo(HashSet<String> father, HashSet<String> son) {
        List<String> list = Arrays.asList("dept", "lab", "sch", "inst", "center", "fac", "res", "coll"); //详见同义词表
        boolean b = true;
        for (String s : list) {
            b = b && (!father.contains(s) || son.contains(s));
        }
        return b;
    }

    /**
     * 获得机构名字的分词集合
     *
     * @param affiliation 名字待分词的机构
     * @return 名字分词后的刺激
     * @throws IOException 读取名字字符流的时候可能抛出
     */
    private HashSet<String> getTokenSet(Affiliation affiliation) throws IOException {
        TokenStream tokenStream = analyzer.tokenStream("", affiliation.getFormattedName()); //fieldName不重要，没有索引
        CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
        HashSet<String> affiTokens = new HashSet<>();
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            affiTokens.add(attribute.toString()); //不断分词并添加到词集合里
        }
        tokenStream.end();
        tokenStream.close();
        return affiTokens;
    }

    @Async
    public void refresh(Date date) {
        duplicateAffiliationDao.findByUpdatedAtAfter(date).forEach(dup -> {
            affiPopWorker.generatePop(dup.getSon());
            if (dup.getClear() && !dup.getSon().getId().equals(dup.getSon().getActual().getId())) {
                Optional<Affiliation.Popularity> result = affiPopDao.findByAffiliation_Id(dup.getSon().getId());
                result.ifPresent(pop -> {
                    pop.setPopularity(0.0);
                    affiPopDao.save(pop);
                });
            } else if (!dup.getClear()) {
                affiPopWorker.generatePop(dup.getFather());
            }
        });
    }

    private static Logger logger = LoggerFactory.getLogger(AffiDupWorker.class);
}
