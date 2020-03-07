package edu.nju.se.teamnamecannotbeempty.backend.dao;

import edu.nju.se.teamnamecannotbeempty.backend.po.Conference;
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
public class ConferenceDaoTest {
    @Autowired
    private ConferenceDao conferenceDao;

    @Test
    public void findByNameAndYear() {
        Optional<Conference> result = conferenceDao.findByNameAndYear("ase",2000);
        assertTrue(result.isPresent());
        assertEquals("ase", result.get().getName());
        assertEquals(2000, result.get().getYear().intValue());
    }

    @Test
    public void findByNameButWrongYear() {
        Optional<Conference> result = conferenceDao.findByNameAndYear("ase",2010);
        assertFalse(result.isPresent());
    }
}