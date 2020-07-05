package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper.Popularity;
import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.RefDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PaperPopWorker {
    private static final ArrayList<Popularity> pops = new ArrayList<>();
    private final PaperDao paperDao;
    private final RefDao refDao;
    private final JdbcTemplate jdbcTemplate;
    public static final Logger logger = LoggerFactory.getLogger(PaperPopWorker.class);

    @Autowired
    public PaperPopWorker(PaperDao paperDao, RefDao refDao, JdbcTemplate jdbcTemplate) {
        this.paperDao = paperDao;
        this.refDao = refDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void generatePaperPop() {
        //TODO PageRank
        int count = Math.toIntExact(count());
        pops.ensureCapacity(2 * count);
        refDao.findAll().stream().filter(ref -> ref.getReferee() != null)
                .collect(Collectors.groupingBy(Ref::getReferee))
                .forEach((referee, refs) ->
                        refs.stream().collect(
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
                        ).forEach((year, yearlyRefs) -> {
                            if (year != 0) {
                                pops.add(new Popularity(referee, (double) yearlyRefs.size(), year));
                            }
                        })
                );

        ArrayList<Popularity> sumPop = new ArrayList<>(count);
        pops.parallelStream().collect(
                Collectors.groupingBy(
                        Popularity::getPaper,
                        Collectors.summingDouble(Popularity::getPopularity)
                )
        ).forEach((paper, pop) -> sumPop.add(new Popularity(paper, pop, null)));
        pops.addAll(sumPop);

        jdbcTemplate.batchUpdate(
                "insert paper_popularity(id, popularity, year, paper_id) VALUES (0,?,?,?)",
                pops,
                1000,
                (ps, argument) -> {
                    ps.setDouble(1, argument.getPopularity());
                    ps.setObject(2, argument.getYear(), JDBCType.INTEGER);
                    ps.setLong(3, argument.getPaper().getId());
                }
        );
        logger.info("Done generate paper popularity");
    }

    public long count() {
        return paperDao.count();
    }

    public static Stream<Popularity> popsStream() {
        return pops.stream();
    }

    public static Stream<Popularity> popsParallelStream() {
        return pops.parallelStream();
    }
}
