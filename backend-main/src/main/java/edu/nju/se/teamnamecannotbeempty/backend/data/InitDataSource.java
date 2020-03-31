package edu.nju.se.teamnamecannotbeempty.backend.data;

import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.NeedParseCSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (event.getApplicationContext().getParent() == null && AppContextProvider.getBean(NeedParseCSV.class).isNeed()) {
            logger.info("Import data...");
            System.out.printf("%d papers to import. 从数据库获取count(Paper)来确认导入完成", dataImportJob.trigger());
        }
    }
}
