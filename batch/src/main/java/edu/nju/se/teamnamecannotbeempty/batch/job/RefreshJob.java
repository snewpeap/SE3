package edu.nju.se.teamnamecannotbeempty.batch.job;

import cn.hutool.core.date.DateUtil;
import edu.nju.se.teamnamecannotbeempty.api.IRefreshJob;
import edu.nju.se.teamnamecannotbeempty.batch.job.worker.AffiDupWorker;
import edu.nju.se.teamnamecannotbeempty.batch.job.worker.AuthorDupWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefreshJob implements IRefreshJob {
    private final AuthorDupWorker authorDupWorker;
    private final AffiDupWorker affiDupWorker;

    @Autowired
    public RefreshJob(AuthorDupWorker authorDupWorker, AffiDupWorker affiDupWorker) {
        this.authorDupWorker = authorDupWorker;
        this.affiDupWorker = affiDupWorker;
    }

    @Override
    public void trigger() {
        Date dateToRefresh = DateUtil.yesterday();
        logger.info("Refresh for data updated after " + dateToRefresh);
        authorDupWorker.refresh(dateToRefresh);
        affiDupWorker.refresh(dateToRefresh);
    }

    private static Logger logger = LoggerFactory.getLogger(RefreshJob.class);
}
