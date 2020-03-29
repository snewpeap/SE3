package edu.nju.se.teamnamecannotbeempty.backend.config;

import edu.nju.teannamecannotbeempty.api.TestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class RMIConfig {
    private static final String testService = "testService";

    @Bean(testService)
    @Profile("default")
    public RmiProxyFactoryBean testService() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://127.0.0.1:9911/" + testService);
        rmiProxyFactoryBean.setServiceInterface(TestService.class);
        return rmiProxyFactoryBean;
    }

    @Bean(testService)
    @Profile("test")
    public Object testService_test() {
        return null;
    }
}
