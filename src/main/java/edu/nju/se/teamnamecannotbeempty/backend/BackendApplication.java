package edu.nju.se.teamnamecannotbeempty.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {
    @Bean
    public Boolean useCSVDataSource() {
        return false;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
