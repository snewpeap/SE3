package edu.nju.se.teamnamecannotbeempty.batch.job;

import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.TermDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AffiPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AuthorPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefreshJob {
    private final AuthorPopGenerator authorPopGenerator;
    private final AffiPopGenerator affiPopGenerator;
    private final TermPopGenerator termPopGenerator;
    private final PaperDao paperDao;
    private final PaperPopDao paperPopDao;

    @Autowired
    public RefreshJob(AuthorPopGenerator authorPopGenerator, AffiPopGenerator affiPopGenerator, TermPopGenerator termPopGenerator,
                      PaperDao paperDao, PaperPopDao paperPopDao) {
        this.authorPopGenerator = authorPopGenerator;
        this.affiPopGenerator = affiPopGenerator;
        this.termPopGenerator = termPopGenerator;
        this.paperDao = paperDao;
        this.paperPopDao = paperPopDao;
    }

    @Async
    public void trigger() {
        authorPopGenerator.generateAuthorPop();
        affiPopGenerator.generateAffiPop();
        termPopGenerator.generateTermPop();
    }

    @Async
    void trigger_init(long total) {
        long startTime = System.currentTimeMillis();
        final long DEADLINE = 1000 * 60 * 3;
        while (paperDao.count() != total) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.warn(e.getMessage());
            }
            if (System.currentTimeMillis() - startTime > DEADLINE) {
                logger.warn("Data Import time exceed " + DEADLINE + " millis, and current count is " + paperDao.count() + ". Abort it");
                return;
            }
        }
        logger.info("Done import papers. Start generating paper popularity...");
        generatePaperPop();
        logger.info("Done generate paper popularity");
        trigger();
    }

    void generatePaperPop() {
        //TODO PageRank implemented by Spark
        paperDao.streamAll().forEach(paper -> {
            Paper.Popularity pop = new Paper.Popularity(paper, paper.getCitation().doubleValue());
            paperPopDao.save(pop);
        });
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
        void generateAuthorPop() {
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
        void generateAffiPop() {
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
