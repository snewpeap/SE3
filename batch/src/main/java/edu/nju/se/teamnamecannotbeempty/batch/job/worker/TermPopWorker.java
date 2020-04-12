package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.TermDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TermPopWorker {
    private final TermDao termDao;
    private final TermPopDao termPopDao;
    private final PaperPopDao paperPopDao;

    @Autowired
    public TermPopWorker(TermDao termDao, TermPopDao termPopDao, PaperPopDao paperPopDao) {
        this.termDao = termDao;
        this.termPopDao = termPopDao;
        this.paperPopDao = paperPopDao;
    }

    @Async
    public void generateTermPop() {
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
