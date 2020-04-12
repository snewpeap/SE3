import edu.nju.se.teamnamecannotbeempty.data.DataConfig;
import edu.nju.se.teamnamecannotbeempty.data.domain.Conference;
import edu.nju.se.teamnamecannotbeempty.data.repository.ConferenceDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("daotest")
@ContextConfiguration(classes = DataConfig.class)
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

    @Test
    public void getConferencesByAuthor() {
        List<Conference> conferences = conferenceDao.getConferencesByAuthor(1L);
        assertNotNull(conferences);
        assertEquals(1,conferences.size());
        assertEquals(1L, conferences.get(0).getId().longValue());

        conferences = conferenceDao.getConferencesByAuthor(2L);
        assertNotNull(conferences);
        assertEquals(1, conferences.size());
        assertEquals(2L, conferences.get(0).getId().longValue());
    }

    @Test
    public void getConferencesByAffiliation() {
        List<Conference> conferences = conferenceDao.getConferencesByAffiliation(2L);
        assertNotNull(conferences);
        assertEquals(2, conferences.size());
    }
}