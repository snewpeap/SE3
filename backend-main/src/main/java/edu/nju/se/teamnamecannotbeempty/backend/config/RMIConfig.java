package edu.nju.se.teamnamecannotbeempty.backend.config;

import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
import edu.nju.se.teamnamecannotbeempty.api.IRefreshJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

@Configuration
public class RMIConfig {
    private static final String dataImportJob = "dataImportJob";
    public static final String refreshJob = "refreshJob";
    @Autowired
    private RmiUriConfig rmiUriConfig;

    @Bean(dataImportJob)
    public RmiProxyFactoryBean dataImportJob() {
        RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
        bean.setServiceUrl(String.format("rmi://%s:%s/%s", rmiUriConfig.getHost(), rmiUriConfig.getPort(), dataImportJob));
        bean.setServiceInterface(IDataImportJob.class);
        bean.setLookupStubOnStartup(false);
        bean.setRefreshStubOnConnectFailure(true);
        return bean;
    }

    @Bean(refreshJob)
    public RmiProxyFactoryBean refreshJob() {
        RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
        bean.setServiceUrl(String.format("rmi://%s:%s/%s", rmiUriConfig.getHost(), rmiUriConfig.getPort(), refreshJob));
        bean.setServiceInterface(IRefreshJob.class);
        bean.setLookupStubOnStartup(false);
        bean.setRefreshStubOnConnectFailure(true);
        return bean;
    }

    @ConfigurationProperties("rmi")
    @Component
    public static class RmiUriConfig {
        private String host;
        private String port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }
    }
}
