package edu.nju.se.teamnamecannotbeempty.batch;

import edu.nju.se.teamnamecannotbeempty.data.DataConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackageClasses = {BatchApplication.class, DataConfig.class})
@EnableAsync
@Import({DataConfig.class})
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }

}
