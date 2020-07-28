package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAffiliation;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAffiliationDao;
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

@Component
public class AffiDupWorker {
    private final DuplicateAffiliationDao duplicateAffiliationDao;
    private final AffiliationDao affiliationDao;
    private final AffiPopWorker affiPopWorker;
    private HashMap<Affiliation, HashSet<String>> tokenSetMap;
    private StandardAnalyzer analyzer;

    @Autowired
    public AffiDupWorker(DuplicateAffiliationDao duplicateAffiliationDao, AffiliationDao affiliationDao, AffiPopWorker affiPopWorker) {
        this.duplicateAffiliationDao = duplicateAffiliationDao;
        this.affiliationDao = affiliationDao;
        this.affiPopWorker = affiPopWorker;
    }

    @Async
    public void generateAffiDup() {
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
        List<DuplicateAffiliation> dups = new ArrayList<>(affis.size());
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
                        //判断全包含，但是没有考虑到词序，所以有可能误杀
                        cache.put(affi, compare);
                        dups.add(new DuplicateAffiliation(affi, compare));
                    }
                }
            }
        }
        analyzer.close();
        duplicateAffiliationDao.saveAll(dups);
        logger.info("Done generate duplicate affiliations");
    }

    /**
     * 判断father对象不是son对象的下属机构
     * 主要思想是father的词集里有部门/学院/实验室等表示它可能是下属机构的词，而son没有，就说明存在隶属关系
     *
     * @param father 词集全包含son
     * @param son    词集被father全包含
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
            //记录状态发生了修改，因此无论如何都需要通过作为son一方的机构来更新热度
            Affiliation son = dup.getSon();
            affiPopWorker.refreshPop(son);
            if (dup.getClear() && !son.getId().equals(son.getActual().getId())) {
                //该疑似重复记录被判定为发生了重复，son机构的热度归零
                affiPopWorker.minusPop(son, son);
            } else if (!dup.getClear()) {
                //该疑似重复记录被重置为未解决状态，复原father机构的热度
                affiPopWorker.minusPop(dup.getFather(), son);
            }
        });
    }

    private static final Logger logger = LoggerFactory.getLogger(AffiDupWorker.class);
}
