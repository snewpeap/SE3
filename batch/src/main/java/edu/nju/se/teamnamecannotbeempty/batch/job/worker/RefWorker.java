package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;
import edu.nju.se.teamnamecannotbeempty.data.repository.RefDao;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class RefWorker {
    private final RefDao refDao;

    @Autowired
    public RefWorker(RefDao refDao) {
        this.refDao = refDao;
    }

    @Async
    public Future<?> generate() {
        for (Ref ref : refDao.findByRefereeIsNotNull()) {
            for (Ref nameless : refDao.findByLowercaseTitleEqualsAndRefereeIsNull(ref.getLowercaseTitle())) {
                nameless.setReferee(ref.getReferee());
                refDao.save(nameless);
            }
        }
        LoggerFactory.getLogger(getClass()).info("Ref synced");
        return new AsyncResult<>(null);
    }
}
