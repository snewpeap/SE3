package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaperPopWorker {
    private final PaperDao paperDao;
    private final PaperPopDao paperPopDao;

    @Autowired
    public PaperPopWorker(PaperDao paperDao, PaperPopDao paperPopDao) {
        this.paperDao = paperDao;
        this.paperPopDao = paperPopDao;
    }

    public void generatePaperPop() {
        //TODO PageRank implemented by Spark
        paperDao.findAll().parallelStream()
                .map(paper -> new Paper.Popularity(paper, paper.getCitation().doubleValue()))
                .forEach(paperPopDao::save);
//        paperDao.streamAll().forEach(paper -> {
//            Paper.Popularity pop = new Paper.Popularity(paper, paper.getCitation().doubleValue());
//            paperPopDao.save(pop);
//        });
    }

    public long count() {
        return paperDao.count();
    }
}
