package edu.nju.se.teamnamecannotbeempty.backend.dao;

import edu.nju.se.teamnamecannotbeempty.backend.po.Author;
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
public class AuthorDaoTest {
    @Autowired
    private AuthorDao authorDao;

    @Test
    public void findByName() {
        Optional<Author> result = authorDao.findByName("a");
        assertTrue(result.isPresent());
        assertEquals("a", result.get().getName());
    }

    @Test
    public void findByName_notExist() {
        Optional<Author> result = authorDao.findByName("x");
        assertFalse(result.isPresent());
    }
}