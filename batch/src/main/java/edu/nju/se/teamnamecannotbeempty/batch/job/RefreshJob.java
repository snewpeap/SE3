package edu.nju.se.teamnamecannotbeempty.batch.job;

import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.TermDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AffiPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AuthorPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

@Service
public class RefreshJob {
    private final AuthorPopGenerator authorPopGenerator;
    private final AffiPopGenerator affiPopGenerator;
    private final TermPopGenerator termPopGenerator;
    private final PaperPopGenerator paperPopGenerator;
    private final AuthorDupGenerator authorDupGenerator;
    private final AffiDupGenerator affiDupGenerator;

    @Autowired
    public RefreshJob(AuthorPopGenerator authorPopGenerator, AffiPopGenerator affiPopGenerator, TermPopGenerator termPopGenerator,
                      AuthorDupGenerator authorDupGenerator, AffiDupGenerator affiDupGenerator, PaperPopGenerator paperPopGenerator) {
        this.authorPopGenerator = authorPopGenerator;
        this.affiPopGenerator = affiPopGenerator;
        this.termPopGenerator = termPopGenerator;
        this.authorDupGenerator = authorDupGenerator;
        this.affiDupGenerator = affiDupGenerator;
        this.paperPopGenerator = paperPopGenerator;
    }

    @Async
    public void trigger() {
    }

    @Async
    void trigger_init(long total) {
        long startTime = System.currentTimeMillis();
        final long DEADLINE = 1000 * 60 * 3;
        while (paperPopGenerator.count() != total) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.warn(e.getMessage());
            }
            if (System.currentTimeMillis() - startTime > DEADLINE) {
                logger.error("Data Import time exceed " + DEADLINE + " millis, and current count is " + paperPopGenerator.count() + ". Abort it");
                return;
            }
        }
        logger.info("Done import papers. Start generating paper popularity...");
        paperPopGenerator.generatePaperPop();
        logger.info("Done generate paper popularity");
        Future<?> authorFuture = authorPopGenerator.generateAuthorPop();
        Future<?> affiFuture = affiPopGenerator.generateAffiPop();
        termPopGenerator.generateTermPop();
        affiDupGenerator.generateAffiDup(affiFuture);
        authorDupGenerator.generateAuthorDup(authorFuture);
    }

    @Component
    public static class PaperPopGenerator {
        private final PaperDao paperDao;
        private final PaperPopDao paperPopDao;

        @Autowired
        public PaperPopGenerator(PaperDao paperDao, PaperPopDao paperPopDao) {
            this.paperDao = paperDao;
            this.paperPopDao = paperPopDao;
        }

        void generatePaperPop() {
            //TODO PageRank implemented by Spark
            paperDao.streamAll().forEach(paper -> {
                Paper.Popularity pop = new Paper.Popularity(paper, paper.getCitation().doubleValue());
                paperPopDao.save(pop);
            });
        }

        long count() {
            return paperDao.count();
        }
    }

    @Component
    public static class AuthorDupGenerator {
        private final DuplicateAuthorDao duplicateAuthorDao;
        private final AuthorDao authorDao;

        @Autowired
        public AuthorDupGenerator(DuplicateAuthorDao duplicateAuthorDao, AuthorDao authorDao) {
            this.duplicateAuthorDao = duplicateAuthorDao;
            this.authorDao = authorDao;
        }

        @Async
        void generateAuthorDup(Future<?> waitForImport) {
            while (waitForImport.isDone()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("Generate duplicate authors aborted due to " + e.getMessage());
                    return;
                }
            }
            ArrayListValuedHashMap<Author, Author> cache = new ArrayListValuedHashMap<>();
            authorDao.getAll().forEach(author -> {
                String authorName = author.getLowerCaseName();
                String[] parts = authorName.split(" ");
                if (parts.length >= 2) {
                    String firstPrefix = String.valueOf(parts[0].charAt(0)).toLowerCase();
                    String lastName = parts[parts.length - 1].toLowerCase();
                    List<Author> suspects = // 名的首字母相同且姓相同
                            authorDao.findByLowerCaseNameIsLikeAndIdIsNot(firstPrefix + "% " + lastName, author.getId());
                    for (Author suspect : suspects) {
                        String suspectName = suspect.getLowerCaseName();
                        if (!cache.containsKey(suspect) ||
                                (cache.containsKey(suspect) && !cache.get(suspect).contains(author))) {
                            // 如果已经存在a-b，b-a不会被加入以防止成环
                            if (isSimilar(parts, suspectName.split(" "))) {
                                cache.put(author, suspect);
                                duplicateAuthorDao.save(new DuplicateAuthor(author, suspect));
                            }
                        }
                    }
                }
            });
            logger.info("Done generate duplicate authors");
        }

        private boolean isSimilar(String[] parts, String[] suspectParts) {
            boolean partsIsLess = parts.length < suspectParts.length;
            String[] less = partsIsLess ? parts : suspectParts;
            String[] more = partsIsLess ? suspectParts : parts;
            boolean isSimilar = true;
            for (int i = 1; i < less.length - 1; i++) {
                less[i] = less[i].replace(".", "");
                for (int j = i; j < more.length - 1; j++) {
                    if (more[j].startsWith(less[i])) break;
                    isSimilar = false;
                }
                if (!isSimilar) break;
            }
            return isSimilar;
        }

        private static Logger logger = LoggerFactory.getLogger(AuthorDupGenerator.class);
    }

    @Component
    public static class AffiDupGenerator {
        private final DuplicateAffiliationDao duplicateAffiliationDao;
        private final AffiliationDao affiliationDao;
        private HashMap<Affiliation, HashSet<String>> tokenSetMap;
        private StandardAnalyzer analyzer;

        @Autowired
        public AffiDupGenerator(DuplicateAffiliationDao duplicateAffiliationDao, AffiliationDao affiliationDao) {
            this.duplicateAffiliationDao = duplicateAffiliationDao;
            this.affiliationDao = affiliationDao;
        }

        @Async
        void generateAffiDup(Future<?> waitForImport) {
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
                    tokenSetMap.put(affi, getTokenSet(affi));
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
                    if (compare.getId().equals(affi.getId())) continue;
                    HashSet<String> compareSet = tokenSetMap.get(compare);
                    if (!cache.containsKey(compare) ||
                            (cache.containsKey(compare) && !cache.get(compare).contains(affi))) {
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
            return (!father.contains("dept") || son.contains("dept")) &&
                    (!father.contains("lab") || son.contains("lab")) &&
                    (!father.contains("sch") || son.contains("sch")) &&
                    (!father.contains("institute") || son.contains("institute"));
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

        private static Logger logger = LoggerFactory.getLogger(AffiDupGenerator.class);
    }

    @Component
    public static class AuthorPopGenerator {
        private final AuthorDao authorDao;
        private final AuthorPopDao authorPopDao;
        private final PaperPopDao paperPopDao;

        @Autowired
        public AuthorPopGenerator(AuthorDao authorDao, AuthorPopDao authorPopDao, PaperPopDao paperPopDao) {
            this.authorDao = authorDao;
            this.authorPopDao = authorPopDao;
            this.paperPopDao = paperPopDao;
        }

        @Async
        Future<?> generateAuthorPop() {
            authorDao.getAll().forEach(author -> {
                Author actual = author.getActual();
                Optional<Author.Popularity> result = authorPopDao.findByAuthor_Id(actual.getId());
                Author.Popularity pop = result.orElse(new Author.Popularity(actual, 0.0));
                Double sum = paperPopDao.getPopSumByAuthorId(actual.getId());
                sum = sum == null ? 0.0 : sum;
                pop.setPopularity(pop.getPopularity() + sum);
                authorPopDao.save(pop);
            });
            LoggerFactory.getLogger(getClass()).info("Done generate author popularity");
            return new AsyncResult<>(null);
        }
    }

    @Component
    public static class AffiPopGenerator {
        private final AffiliationDao affiliationDao;
        private final AffiPopDao affiPopDao;
        private final PaperPopDao paperPopDao;

        @Autowired
        public AffiPopGenerator(AffiliationDao affiliationDao, AffiPopDao affiPopDao, PaperPopDao paperPopDao) {
            this.affiliationDao = affiliationDao;
            this.affiPopDao = affiPopDao;
            this.paperPopDao = paperPopDao;
        }

        @Async
        Future<?> generateAffiPop() {
            affiliationDao.getAll().forEach(affi -> {
                Affiliation actual = affi.getActual();
                Optional<Affiliation.Popularity> result = affiPopDao.findByAffiliation_Id(actual.getId());
                Affiliation.Popularity pop = result.orElse(new Affiliation.Popularity(actual, 0.0));
                Double sum = paperPopDao.getPopSumByAffiId(actual.getId());
                sum = sum == null ? 0.0 : sum;
                pop.setPopularity(pop.getPopularity() + sum);
                affiPopDao.save(pop);
            });
            LoggerFactory.getLogger(getClass()).info("Done generate affiliation popularity");
            return new AsyncResult<>(null);
        }
    }

    @Component
    public static class TermPopGenerator {
        private final TermDao termDao;
        private final TermPopDao termPopDao;
        private final PaperPopDao paperPopDao;

        @Autowired
        public TermPopGenerator(TermDao termDao, TermPopDao termPopDao, PaperPopDao paperPopDao) {
            this.termDao = termDao;
            this.termPopDao = termPopDao;
            this.paperPopDao = paperPopDao;
        }

        @Async
        void generateTermPop() {
            termDao.findAllAuthorKeywords().forEach(term -> {
                Long id = term.getId();
                Term.Popularity pop = termPopDao.getDistinctByTerm_Id(id).orElse(new Term.Popularity(term, 0.0));
                Double sum = paperPopDao.getPopSumByAuthorKeywordId(id);
                sum = sum == null ? 0.0 : sum;
                pop.setPopularity(pop.getPopularity() + sum);
                termPopDao.save(pop);
            });
            LoggerFactory.getLogger(getClass()).info("Done generate term popularity");
        }
    }

    private static Logger logger = LoggerFactory.getLogger(RefreshJob.class);
}
