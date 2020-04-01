package edu.nju.se.teamnamecannotbeempty.backend.data;

import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

//TODO 定时任务
@Component
public class InitDataSource implements ApplicationListener<ContextRefreshedEvent> {
    private final IDataImportJob dataImportJob;

    private static Logger logger = LoggerFactory.getLogger(InitDataSource.class);

    @Autowired
    public InitDataSource(IDataImportJob dataImportJob) {
        this.dataImportJob = dataImportJob;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null &&
                AppContextProvider.getBean(HibernateProperties.class).getDdlAuto().startsWith("create")) {
            logger.info("Import data...");
            long total = dataImportJob.trigger();
            AppContextProvider.getBean(Searchable.class).setNum(total);
            logger.info(total + " papers to import. 从数据库获取count(Paper)来确认导入完成");
        }
    }
}
