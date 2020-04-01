package edu.nju.se.teamnamecannotbeempty.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Configuration
public class HibernateSearchConfig {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }

}
