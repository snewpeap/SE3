package edu.nju.se.teamnamecannotbeempty.batch.job;

import cn.hutool.core.date.DateUtil;
import edu.nju.se.teamnamecannotbeempty.api.IRefreshJob;
import edu.nju.se.teamnamecannotbeempty.batch.job.generators.AffiDupGenerator;
import edu.nju.se.teamnamecannotbeempty.batch.job.generators.AuthorDupGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefreshJob implements IRefreshJob {
    private final AuthorDupGenerator authorDupGenerator;
    private final AffiDupGenerator affiDupGenerator;

    @Autowired
    public RefreshJob(AuthorDupGenerator authorDupGenerator, AffiDupGenerator affiDupGenerator) {
        this.authorDupGenerator = authorDupGenerator;
        this.affiDupGenerator = affiDupGenerator;
    }

    @Override
    public void trigger() {
        Date dateToRefresh = DateUtil.yesterday();
        logger.info("Refresh for data updated after " + dateToRefresh);
        authorDupGenerator.refresh(dateToRefresh);
        affiDupGenerator.refresh(dateToRefresh);
    }

    private static Logger logger = LoggerFactory.getLogger(RefreshJob.class);
}
