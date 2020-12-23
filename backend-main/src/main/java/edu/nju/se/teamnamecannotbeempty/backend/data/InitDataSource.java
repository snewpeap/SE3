package edu.nju.se.teamnamecannotbeempty.backend.data;

import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.SearchServiceHibernateImpl;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.Searchable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.stereotype.Component;

//TODO 定时任务
@Component
public class InitDataSource implements ApplicationListener<ContextRefreshedEvent> {
    private final IDataImportJob dataImportJob;
    private final SearchServiceHibernateImpl serviceHibernate;

    private static final Logger logger = LoggerFactory.getLogger(InitDataSource.class);

    @Autowired
    public InitDataSource(IDataImportJob dataImportJob, SearchServiceHibernateImpl serviceHibernate) {
        this.dataImportJob = dataImportJob;
        this.serviceHibernate = serviceHibernate;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            if (AppContextProvider.getBean(HibernateProperties.class).getDdlAuto().startsWith("create")) {
                logger.info("Import data...");
                try {
                    long total = dataImportJob.trigger();
                    AppContextProvider.getBean(Searchable.class).setNum(total);
                    logger.info(total + " papers to import. 从数据库获取count(Paper)来确认导入完成");
                    serviceHibernate.flushIndexes();
                } catch (RemoteLookupFailureException e) {
                    e.printStackTrace();
                    logger.error("Connect to remote batch service fail. Import aborted.");
                }
            } else {
                AppContextProvider.getBean(Searchable.class).pass();
            }
        }
    }
}
