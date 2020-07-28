package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper.Popularity;
import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PaperPopWorker {
    private static final ArrayList<Popularity> pops = new ArrayList<>();
    private final PaperDao paperDao;
    private final JdbcTemplate jdbcTemplate;
    public static final Logger logger = LoggerFactory.getLogger(PaperPopWorker.class);

    @Autowired
    public PaperPopWorker(PaperDao paperDao, JdbcTemplate jdbcTemplate) {
        this.paperDao = paperDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void generatePaperPop() {
        int count = Math.toIntExact(count());
        pops.ensureCapacity(2 * count);
        Attacher.getRefs().parallelStream().filter(ref -> ref.getReferee() != null)
                .collect(Collectors.groupingBy(Ref::getReferee))
                .forEach((referee, refs) ->
                        refs.stream().collect(
                                Collectors.groupingBy(ref -> {
                                            Integer year = ref.getReferer().getYear();
                                            if (year == null) year = 0;
                                            return year;
                                        }
                                )
                        ).forEach((year, yearlyRefs) -> {
                            if (year != 0) {
                                BigDecimal pop = BigDecimal.ZERO;
                                for (Ref ref : yearlyRefs) {
                                    //pop += 0.01 * referer's citation + 1.0
                                    pop = pop.add(new BigDecimal(Double.toString(ref.getReferer().getCitation()))
                                                    .multiply(new BigDecimal("0.01"))
                                                    .add(BigDecimal.ONE));
                                }
                                pops.add(new Popularity(referee, pop.doubleValue(), year));
                            }
                        })
                );

        ArrayList<Popularity> sumPop = new ArrayList<>(count);
        pops.parallelStream().collect(
                Collectors.groupingBy(
                        Popularity::getPaper,
                        Collectors.summingDouble(Popularity::getPopularity)
                )
        ).forEach((paper, pop) -> sumPop.add(new Popularity(paper, (double) Math.round(pop * 100) / 100, null)));
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

    public static Stream<Popularity> popsParallelStream() {
        return pops.parallelStream();
    }
}
