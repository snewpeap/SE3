package edu.nju.se.teamnamecannotbeempty.backend.config;

import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class RMIConfig {
    private static final String dataImportJob = "dataImportJob";

    @Bean(dataImportJob)
    public RmiProxyFactoryBean dataImportJob() {
        RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
        bean.setServiceUrl("rmi://127.0.0.1:9911/" + dataImportJob);
        bean.setServiceInterface(IDataImportJob.class);
        return bean;
    }

    @Bean(dataImportJob)
    @Profile("dev")
    public Object servicelessDev() {
        return null;
    }
}
