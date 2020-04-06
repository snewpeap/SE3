package edu.nju.se.teamnamecannotbeempty.backend.data;

import edu.nju.se.teamnamecannotbeempty.api.IRefreshJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataRefresh {
    private final IRefreshJob refreshJob;
    private static Logger logger = LoggerFactory.getLogger(DataRefresh.class);

    @Autowired
    public DataRefresh(IRefreshJob refreshJob) {
        this.refreshJob = refreshJob;
    }

    public void refresh() {
        logger.info("Start daily refresh");
        refreshJob.trigger();
    }
}
