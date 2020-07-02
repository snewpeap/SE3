package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class PaperPopWorker {
    private final PaperDao paperDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PaperPopWorker(PaperDao paperDao, JdbcTemplate jdbcTemplate) {
        this.paperDao = paperDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void generatePaperPop() {
        //TODO PageRank implemented by Spark
        Collection<Paper.Popularity> pops = paperDao.findAll().parallelStream()
                .map(paper -> new Paper.Popularity(paper, paper.getCitation().doubleValue()))
                .collect(Collectors.toSet());
        jdbcTemplate.batchUpdate(
                "insert paper_popularity(id, popularity, year, paper_id) VALUES (0,?,?,?)",
                pops,
                500,
                (ps, pop) -> {
                    ps.setDouble(1, pop.getPopularity());
                    ps.setInt(2, pop.getYear());
                    ps.setLong(3, pop.getPaper().getId());
                });
    }

    public long count() {
        return paperDao.count();
    }
}
