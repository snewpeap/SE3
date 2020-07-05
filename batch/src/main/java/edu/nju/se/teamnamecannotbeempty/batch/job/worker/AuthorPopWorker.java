package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AuthorPopDao;
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
public class AuthorPopWorker {
    private final AuthorDao authorDao;
    private final AuthorPopDao authorPopDao;
    private final PaperPopDao paperPopDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorPopWorker(AuthorDao authorDao, AuthorPopDao authorPopDao, PaperPopDao paperPopDao, JdbcTemplate jdbcTemplate) {
        this.authorDao = authorDao;
        this.authorPopDao = authorPopDao;
        this.paperPopDao = paperPopDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    public Future<?> generateAuthorPop() {
        int count = Math.toIntExact(authorDao.count());
        ArrayListValuedHashMap<Author, Paper.Popularity> authorsPaperPop =
                new ArrayListValuedHashMap<>(count, 3);
        ArrayList<Author.Popularity> authorPops = new ArrayList<>(3 * count);
        PaperPopWorker.popsParallelStream().filter(paperPop -> paperPop.getYear() != null)
                .forEach(paperPop -> paperPop.getPaper().getAa().forEach(aa -> {
                    synchronized (authorsPaperPop) {
                        authorsPaperPop.put(aa.getAuthor(), paperPop);
                    }
                }));
        authorsPaperPop.asMap().forEach((author, pops) -> pops.stream().collect(
                Collectors.groupingBy(
                        Paper.Popularity::getYear,
                        Collectors.summingDouble(Paper.Popularity::getPopularity)
                )
        ).forEach((year, sum) ->
                authorPops.add(new Author.Popularity(author, sum, year))
        ));
        ArrayList<Author.Popularity> sumPops = new ArrayList<>(count);
        authorPops.stream().collect(
                Collectors.groupingBy(
                        Author.Popularity::getAuthor,
                        Collectors.summingDouble(Author.Popularity::getPopularity)
                )
        ).forEach((author, popSum) -> sumPops.add(new Author.Popularity(author, popSum)));
        authorPops.addAll(sumPops);

        jdbcTemplate.batchUpdate(
                "insert author_popularity(id, popularity, year, author_id) VALUES (0,?,?,?)",
                authorPops,
                500,
                (ps, argument) -> {
                    ps.setDouble(1, argument.getPopularity());
                    ps.setObject(2, argument.getYear(), JDBCType.INTEGER);
                    ps.setLong(3, argument.getAuthor().getId());
                }
        );

        LoggerFactory.getLogger(getClass()).info("Done generate author popularity");
        return new AsyncResult<>(null);
    }

    Author.Popularity generatePop(Author author) {
        Author actual = author.getActual();
        Optional<Author.Popularity> result = authorPopDao.findByAuthor_Id(actual.getId());
        Author.Popularity pop = result.orElse(new Author.Popularity(actual, 0.0));
        Double sum = paperPopDao.getPopSumByAuthorId(actual.getId());
        sum = sum == null ? 0.0 : sum;
        pop.setPopularity(pop.getPopularity() + sum);
        return pop;
    }
}
