package edu.nju.se.teamnamecannotbeempty.backend.dao;

import edu.nju.se.teamnamecannotbeempty.backend.BackendApplication;
import edu.nju.se.teamnamecannotbeempty.backend.po.Author_Affiliation;
import edu.nju.se.teamnamecannotbeempty.backend.po.Conference;
import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import edu.nju.se.teamnamecannotbeempty.backend.po.Term;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest(classes = BackendApplication.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class PaperDaoTest {
    @Autowired
    private PaperDao paperDao;

    @Test
    @Transactional
    public void findById_normal() {
        Optional<Paper> result = paperDao.findById(3L);
        assertTrue(result.isPresent());
        Paper paper = result.get();
        assertEquals("data mining", paper.getTitle());
        Conference conference = paper.getConference();
        assertNotNull(conference);
        assertEquals("ase", conference.getName());
        assertEquals(1, conference.getOrdno().intValue());
        assertEquals(2000, conference.getYear().intValue());
        List<Author_Affiliation> aa = paper.getAa();
        assertNotNull(aa);
        assertEquals(2, aa.size());
        aa.forEach(System.out::println);
        List<Term> terms = paper.getAuthor_keywords();
        assertNotNull(terms);
        assertEquals(2, terms.size());
        terms.forEach(System.out::println);
        System.out.println(paper);
    }

    @Test
    public void findById_notFound() {
        Optional<Paper> result = paperDao.findById(10L);
        assertFalse(result.isPresent());
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void findById_null() {
        Optional<Paper> result = paperDao.findById(null);
        assertFalse(result.isPresent());
    }

    @Test
    public void findByYears_allMatch_farFromBorder() {
        List<Paper> papers = paperDao.findAllByConference_YearBetween(1000, 3000);
        assertNotNull(papers);
        assertEquals(3, papers.size());
    }

    @Test
    public void findByYears_allMatch_LBorder() {
        List<Paper> papers = paperDao.findAllByConference_YearBetween(2000, 3000);
        assertNotNull(papers);
        assertEquals(3, papers.size());
    }

    @Test
    public void findByYears_allMatch_RBorder() {
        List<Paper> papers = paperDao.findAllByConference_YearBetween(1000, 2032);
        assertNotNull(papers);
        assertEquals(3, papers.size());
    }

    @Test
    public void findByYears_noMatch_farFromBorder() {
        List<Paper> papers = paperDao.findAllByConference_YearBetween(2008, 2020);
        assertNotNull(papers);
        assertEquals(0, papers.size());
    }

    @Test
    public void findByYears_partly_oneYear() {
        List<Paper> papers = paperDao.findAllByConference_YearBetween(2000, 2000);
        assertNotNull(papers);
        assertEquals(2, papers.size());
        papers.forEach(paper -> Assert.assertEquals(2000, paper.getConference().getYear().intValue()));
    }

    @Test
    public void findByYears_fromGtTo() {
        List<Paper> papers = paperDao.findAllByConference_YearBetween(3000, 2000);
        assertNotNull(papers);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void findByYears_oneNull() {
        List<Paper> papers = paperDao.findAllByConference_YearBetween(1000, null);
        assertNotNull(papers);
        assertEquals(3, papers.size());
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void findByYears_allNull() {
        List<Paper> papers = paperDao.findAllByConference_YearBetween(null, null);
        assertNotNull(papers);
        assertEquals(3, papers.size());
    }
}
