package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.TermDao;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class TermPopWorker {
    private final TermDao termDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TermPopWorker(TermDao termDao, JdbcTemplate jdbcTemplate) {
        this.termDao = termDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    public Future<?> generateTermPop() {
        int count = Math.toIntExact(termDao.count());
        ArrayListValuedHashMap<Term, Paper.Popularity> termsPaperPop =
                new ArrayListValuedHashMap<>(count, 5);
        ArrayList<Term.Popularity> termPops = new ArrayList<>(count * 3);
        PaperPopWorker.popsParallelStream().filter(paperPop -> paperPop.getYear() != null)
                .forEach(paperPop -> paperPop.getPaper().getAuthor_keywords().forEach(ak -> {
                    synchronized (termsPaperPop) {
                        termsPaperPop.put(ak, paperPop);
                    }
                }));
        termsPaperPop.asMap().forEach((term, pops) -> pops.stream().collect(
                Collectors.groupingBy(
                        Paper.Popularity::getYear,
                        Collectors.summingDouble(Paper.Popularity::getPopularity)
                )
        ).forEach((year, sum) ->
                termPops.add(new Term.Popularity(term, (double) Math.round(sum * 100) / 100, year))
        ));
        ArrayList<Term.Popularity> sumPops = new ArrayList<>(count);
        termPops.stream().collect(
                Collectors.groupingBy(
                        Term.Popularity::getTerm,
                        Collectors.summingDouble(Term.Popularity::getPopularity)
                )
        ).forEach((term, popSum) ->
                sumPops.add(new Term.Popularity(term, (double) Math.round(popSum * 100) / 100)));
        termPops.addAll(sumPops);

        jdbcTemplate.batchUpdate(
                "insert term_popularity(id, popularity, year, term_id) VALUES (0,?,?,?)",
                termPops,
                500,
                (ps, argument) -> {
                    ps.setDouble(1, argument.getPopularity());
                    ps.setObject(2, argument.getYear(), JDBCType.INTEGER);
                    ps.setLong(3, argument.getTerm().getId());
                }
        );
        LoggerFactory.getLogger(getClass()).info("Done generate term popularity");
        return new AsyncResult<>(null);
    }
}
