package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.RefDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.JDBCType;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaperPopWorker {
    private final PaperDao paperDao;
    private final JdbcTemplate jdbcTemplate;
    private final RefDao refDao;

    @Autowired
    public PaperPopWorker(PaperDao paperDao, JdbcTemplate jdbcTemplate, RefDao refDao) {
        this.paperDao = paperDao;
        this.jdbcTemplate = jdbcTemplate;
        this.refDao = refDao;
    }

    public void generatePaperPop() {
        //TODO PageRank
        List<Object[]> pops = new LinkedList<>();
        refDao.findAll().stream()
                .filter(ref -> ref.getReferee() != null)
                .collect(Collectors.groupingBy(ref -> ref.getReferee().getId()))
                .forEach((id, refs) -> refs.stream()
                        .collect(
                                Collectors.groupingBy(ref -> {
                                            Paper referer = ref.getReferer();
                                            Integer year = referer.getYear();
                                            if (year == null) {
                                                year = referer.getConference().getYear();
                                                if (year == null) {
                                                    year = 0;
                                                }
                                            }
                                            return year;
                                        }
                                )
                        )
                        .forEach((year, yearlyRefs) -> {
                            if (year != 0) {
                                pops.add(new Object[]{(double) yearlyRefs.size(), year, id});
                            }
                        })
                );

        jdbcTemplate.batchUpdate(
                "insert paper_popularity(id, popularity, year, paper_id) VALUES (0,?,?,?)",
                pops,
                500,
                (ps, pop) -> {
                    ps.setObject(1, pop[0], JDBCType.DOUBLE);
                    ps.setObject(2, pop[1], JDBCType.INTEGER);
                    ps.setObject(3, pop[2], JDBCType.BIGINT);
                });
    }

    public long count() {
        return paperDao.count();
    }
}
