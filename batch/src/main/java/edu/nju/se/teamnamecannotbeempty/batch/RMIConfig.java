package edu.nju.se.teamnamecannotbeempty.batch;

import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
import edu.nju.se.teamnamecannotbeempty.batch.job.DataImportJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

@Configuration
public class RMIConfig {
    @Bean
    public RmiServiceExporter rmiServiceExporter(DataImportJob dataImportJob) {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceInterface(IDataImportJob.class);
        exporter.setServiceName("dataImportJob");
        exporter.setService(dataImportJob);
        exporter.setRegistryPort(9911);
        return exporter;
    }
}
