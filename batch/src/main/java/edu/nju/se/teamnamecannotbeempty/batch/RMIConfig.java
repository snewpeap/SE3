package edu.nju.se.teamnamecannotbeempty.batch;

import edu.nju.se.teamnamecannotbeempty.api.TestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

@Configuration
public class RMIConfig {

    @Bean
    public RmiServiceExporter rmiServiceExporter(TestServiceImpl testService) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("testService");
        rmiServiceExporter.setServiceInterface(TestService.class);
        rmiServiceExporter.setService(testService);
        rmiServiceExporter.setRegistryPort(9911);
        return rmiServiceExporter;
    }
}
