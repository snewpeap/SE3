package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AuthorPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.Future;

@Component
public class AuthorPopWorker {
    private final AuthorDao authorDao;
    private final AuthorPopDao authorPopDao;
    private final PaperPopDao paperPopDao;

    @Autowired
    public AuthorPopWorker(AuthorDao authorDao, AuthorPopDao authorPopDao, PaperPopDao paperPopDao) {
        this.authorDao = authorDao;
        this.authorPopDao = authorPopDao;
        this.paperPopDao = paperPopDao;
    }

    @Async
    public Future<?> generateAuthorPop() {
        authorDao.getAll().forEach(this::generatePop);
        LoggerFactory.getLogger(getClass()).info("Done generate author popularity");
        return new AsyncResult<>(null);
    }

    void generatePop(Author author) {
        Author actual = author.getActual();
        Optional<Author.Popularity> result = authorPopDao.findByAuthor_Id(actual.getId());
        Author.Popularity pop = result.orElse(new Author.Popularity(actual, 0.0));
        Double sum = paperPopDao.getPopSumByAuthorId(actual.getId());
        sum = sum == null ? 0.0 : sum;
        pop.setPopularity(pop.getPopularity() + sum);
        authorPopDao.save(pop);
    }
}
