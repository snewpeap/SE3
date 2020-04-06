package edu.nju.se.teamnamecannotbeempty.backend;

import edu.nju.se.teamnamecannotbeempty.backend.config.HibernateSearchConfig;
import edu.nju.se.teamnamecannotbeempty.data.DataConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackageClasses = {BackendApplication.class, DataConfig.class})
@EnableCaching
@EnableAsync
@EnableScheduling
@Import({DataConfig.class, HibernateSearchConfig.class})
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
