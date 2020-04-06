package edu.nju.se.teamnamecannotbeempty.batch;

import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
import edu.nju.se.teamnamecannotbeempty.api.IRefreshJob;
import edu.nju.se.teamnamecannotbeempty.batch.job.DataImportJob;
import edu.nju.se.teamnamecannotbeempty.batch.job.RefreshJob;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Component;

@Configuration
public class RMIConfig {
    @Bean
    public RmiServiceExporter dateImportRMIServiceExporter(DataImportJob dataImportJob,
                                                           RmiRegistryFactoryBean rmiRegistryFactoryBean,
                                                           RMI rmi) throws Exception {
        System.setProperty("java.rmi.server.hostname", rmi.getHost());
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceInterface(IDataImportJob.class);
        exporter.setServiceName("dataImportJob");
        exporter.setService(dataImportJob);
        exporter.setRegistry(rmiRegistryFactoryBean.getObject());
        exporter.setServicePort(9912);
        return exporter;
    }

    @Bean
    public RmiServiceExporter refreshRMIServiceExporter(RefreshJob refreshJob,
                                                        RmiRegistryFactoryBean rmiRegistryFactoryBean,
                                                        RMI rmi) throws Exception {
        System.setProperty("java.rmi.server.hostname", rmi.getHost());
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceInterface(IRefreshJob.class);
        exporter.setServiceName("refreshJob");
        exporter.setService(refreshJob);
        exporter.setRegistry(rmiRegistryFactoryBean.getObject());
        exporter.setServicePort(9912);
        return exporter;
    }

    @Bean
    public RmiRegistryFactoryBean rmiRegistryFactoryBean() {
        RmiRegistryFactoryBean bean = new RmiRegistryFactoryBean();
        bean.setPort(9911);
        return bean;
    }

    @ConfigurationProperties("rmi")
    @Component
    public static class RMI {
        private String host;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }
}
