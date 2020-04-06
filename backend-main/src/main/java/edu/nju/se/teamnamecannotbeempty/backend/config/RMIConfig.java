package edu.nju.se.teamnamecannotbeempty.backend.config;

import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
import edu.nju.se.teamnamecannotbeempty.api.IRefreshJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class RMIConfig {
    private static final String dataImportJob = "dataImportJob";
    public static final String refreshJob = "refreshJob";

    @Bean(dataImportJob)
    public RmiProxyFactoryBean dataImportJob() {
        RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
        bean.setServiceUrl("rmi://127.0.0.1:9911/" + dataImportJob);
        bean.setServiceInterface(IDataImportJob.class);
        bean.setLookupStubOnStartup(false);
        bean.setRefreshStubOnConnectFailure(true);
        return bean;
    }

    @Bean(refreshJob)
    public RmiProxyFactoryBean refreshJob() {
        RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
        bean.setServiceUrl("rmi://127.0.0.1:9911/" + refreshJob);
        bean.setServiceInterface(IRefreshJob.class);
        bean.setLookupStubOnStartup(false);
        bean.setRefreshStubOnConnectFailure(true);
        return bean;
    }
}
