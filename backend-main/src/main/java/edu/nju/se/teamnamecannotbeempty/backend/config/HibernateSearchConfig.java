package edu.nju.se.teamnamecannotbeempty.backend.config;

import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.SearchServiceHibernateImpl;
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
    SearchServiceHibernateImpl searchServiceHibernate() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SearchServiceHibernateImpl searchService_hibernate = new SearchServiceHibernateImpl(entityManager);
        searchService_hibernate.hibernateSearchInit();
        return searchService_hibernate;
    }
}
