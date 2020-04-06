package edu.nju.se.teamnamecannotbeempty.batch.job.generators;

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
import java.util.concurrent.Future;

@Component
public class AffiDupGenerator {
    private final DuplicateAffiliationDao duplicateAffiliationDao;
    private final AffiliationDao affiliationDao;
    private final AffiPopGenerator affiPopGenerator;
    private HashMap<Affiliation, HashSet<String>> tokenSetMap;
    private StandardAnalyzer analyzer;

    @Autowired
    public AffiDupGenerator(DuplicateAffiliationDao duplicateAffiliationDao, AffiliationDao affiliationDao, AffiPopGenerator affiPopGenerator) {
        this.duplicateAffiliationDao = duplicateAffiliationDao;
        this.affiliationDao = affiliationDao;
        this.affiPopGenerator = affiPopGenerator;
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
            HashSet<String> affiSet = tokenSetMap.get(affi);
            if (affiSet == null) {
                continue;
            }
            for (Affiliation compare : affis) {
                if (compare.getId().equals(affi.getId()) || compare.getName().equals("NA"))
                    continue;
                if (!cache.containsKey(compare) ||
                        (cache.containsKey(compare) && !cache.get(compare).contains(affi))) {
                    HashSet<String> compareSet = tokenSetMap.get(compare);
                    if (compareSet != null && affiSet.containsAll(compareSet) && notBelongsTo(affiSet, compareSet)) {
                        cache.put(affi, compare);
                        duplicateAffiliationDao.save(new DuplicateAffiliation(affi, compare));
                    }
                }
            }
        }
        analyzer.close();
        logger.info("Done generate duplicate affiliations");
    }

    private boolean notBelongsTo(HashSet<String> father, HashSet<String> son) {
        List<String> list = Arrays.asList("dept", "lab", "sch", "inst", "center", "fac", "res", "coll");
        boolean b = true;
        for (String s : list) {
            b = b && (!father.contains(s) || son.contains(s));
        }
        return b;
    }

    private HashSet<String> getTokenSet(Affiliation affiliation) throws IOException {
        TokenStream tokenStream = analyzer.tokenStream("", affiliation.getFormattedName());
        CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
        HashSet<String> affiTokens = new HashSet<>();
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            affiTokens.add(attribute.toString());
        }
        tokenStream.end();
        tokenStream.close();
        return affiTokens;
    }

    @Async
    public void refresh(Date date) {
        duplicateAffiliationDao.findByUpdatedAtAfter(date).forEach(dup -> affiPopGenerator.generatePop(dup.getSon()));
    }

    private static Logger logger = LoggerFactory.getLogger(AffiDupGenerator.class);
}
