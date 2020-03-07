package edu.nju.se.teamnamecannotbeempty.backend.dao;

import edu.nju.se.teamnamecannotbeempty.backend.po.Affiliation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
class AffiliationDaoTest {
    @Autowired
    private AffiliationDao affiliationDao;

    @Test
    void findByName() {
        Optional<Affiliation> result = affiliationDao.findByName("google");
        assertTrue(result.isPresent());
        assertEquals("google", result.get().getName());
    }

    @Test
    public void findByName_notExist() {
        Optional<Affiliation> result = affiliationDao.findByName("facebook");
        assertFalse(result.isPresent());
    }
}