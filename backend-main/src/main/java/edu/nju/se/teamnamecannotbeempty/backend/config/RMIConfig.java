package edu.nju.se.teamnamecannotbeempty.backend.config;

import edu.nju.teannamecannotbeempty.api.TestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class RMIConfig {
    private static final String testService = "testService";

    @Bean(testService)
    public RmiProxyFactoryBean testService() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://127.0.0.1:9911/" + testService);
        rmiProxyFactoryBean.setServiceInterface(TestService.class);
        return rmiProxyFactoryBean;
    }
}
