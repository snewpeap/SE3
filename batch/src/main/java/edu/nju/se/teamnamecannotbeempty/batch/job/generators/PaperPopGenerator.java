package edu.nju.se.teamnamecannotbeempty.batch.job.generators;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaperPopGenerator {
    private final PaperDao paperDao;
    private final PaperPopDao paperPopDao;

    @Autowired
    public PaperPopGenerator(PaperDao paperDao, PaperPopDao paperPopDao) {
        this.paperDao = paperDao;
        this.paperPopDao = paperPopDao;
    }

    public void generatePaperPop() {
        //TODO PageRank implemented by Spark
        paperDao.streamAll().forEach(paper -> {
            Paper.Popularity pop = new Paper.Popularity(paper, paper.getCitation().doubleValue());
            paperPopDao.save(pop);
        });
    }

    public long count() {
        return paperDao.count();
    }
}
