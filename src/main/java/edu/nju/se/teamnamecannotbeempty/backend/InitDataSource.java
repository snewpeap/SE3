package edu.nju.se.teamnamecannotbeempty.backend;

import edu.nju.se.teamnamecannotbeempty.backend.dao.PaperDao;
import edu.nju.se.teamnamecannotbeempty.backend.data.FromCSVOpenCSVImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class InitDataSource implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private FromCSVOpenCSVImpl fromCSV;
    @Autowired
    private PaperDao paperDao;
    @Autowired
    @Qualifier("useCSVDataSource")
    private Boolean runMe;

    private static Logger logger = LoggerFactory.getLogger(InitDataSource.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null && runMe) {
            File ase_csv = new File(getClass().getResource("/datasource/ase13_15_16_17_19.csv").getPath());
            paperDao.saveAll(fromCSV.convert(ase_csv));
            logger.info("Done Saving data from " + ase_csv.getName());

            File icse_csv = new File(getClass().getResource("/datasource/icse15_16_17_18_19.csv").getPath());
            paperDao.saveAll(fromCSV.convert(icse_csv));
            logger.info("Done Saving data from " + icse_csv.getName());
        }
    }
}
