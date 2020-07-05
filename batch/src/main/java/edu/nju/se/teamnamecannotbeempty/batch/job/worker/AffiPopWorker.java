package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AffiPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class AffiPopWorker {
    private final AffiliationDao affiliationDao;
    private final AffiPopDao affiPopDao;
    private final PaperPopDao paperPopDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AffiPopWorker(AffiliationDao affiliationDao, AffiPopDao affiPopDao, PaperPopDao paperPopDao, JdbcTemplate jdbcTemplate) {
        this.affiliationDao = affiliationDao;
        this.affiPopDao = affiPopDao;
        this.paperPopDao = paperPopDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    public Future<?> generateAffiPop() {
        int count = Math.toIntExact(affiliationDao.count());
        ArrayListValuedHashMap<Affiliation, Paper.Popularity> affisPaperPop =
                new ArrayListValuedHashMap<>(count, 3);
        ArrayList<Affiliation.Popularity> affiPops = new ArrayList<>(3 * count);
        PaperPopWorker.popsParallelStream().filter(paperPop -> paperPop.getYear() != null)
                .forEach(paperPop -> paperPop.getPaper().getAa().forEach(aa -> {
                    synchronized (affisPaperPop) {
                        if (!aa.getAffiliation().getActual().getName().equals("NA"))
                            affisPaperPop.put(aa.getAffiliation(), paperPop);
                    }
                }));
        affisPaperPop.asMap().forEach((affiliation, pops) -> pops.stream().collect(
                Collectors.groupingBy(
                        Paper.Popularity::getYear,
                        Collectors.summingDouble(Paper.Popularity::getPopularity)
                )
        ).forEach((year, sum) ->
                affiPops.add(new Affiliation.Popularity(affiliation, sum, year))
        ));
        ArrayList<Affiliation.Popularity> sumPops = new ArrayList<>(count);
        affiPops.stream().collect(
                Collectors.groupingBy(
                        Affiliation.Popularity::getAffiliation,
                        Collectors.summingDouble(Affiliation.Popularity::getPopularity)
                )
        ).forEach((affiliation, popSum) -> sumPops.add(new Affiliation.Popularity(affiliation, popSum)));
        affiPops.addAll(sumPops);

        jdbcTemplate.batchUpdate(
                "insert affi_popularity(id, popularity, year, affiliation_id) VALUES (0,?,?,?)",
                affiPops,
                500,
                (ps, argument) -> {
                    ps.setDouble(1, argument.getPopularity());
                    ps.setObject(2, argument.getYear(), JDBCType.INTEGER);
                    ps.setLong(3, argument.getAffiliation().getId());
                }
        );

        LoggerFactory.getLogger(getClass()).info("Done generate affiliation popularity");
        return new AsyncResult<>(null);
    }

    Affiliation.Popularity generatePop(Affiliation affi) {
        Affiliation actual = affi.getActual();
        Optional<Affiliation.Popularity> result = affiPopDao.findByAffiliation_Id(actual.getId());
        Affiliation.Popularity pop = result.orElse(new Affiliation.Popularity(actual, 0.0));
        if (!actual.getName().equals("NA")) {
            Double sum = paperPopDao.getPopSumByAffiId(actual.getId());
            sum = sum == null ? 0.0 : sum;
            pop.setPopularity(pop.getPopularity() + sum);
        }
        return pop;
    }
}
