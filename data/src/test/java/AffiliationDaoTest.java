
import edu.nju.se.teamnamecannotbeempty.data.DataConfig;
import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
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
public class AffiliationDaoTest {
    @Autowired
    private AffiliationDao affiliationDao;

    @Test
    public void findByName() {
        Optional<Affiliation> result = affiliationDao.findByName("google");
        assertTrue(result.isPresent());
        assertEquals("google", result.get().getName());
    }

    @Test
    public void findByName_notExist() {
        Optional<Affiliation> result = affiliationDao.findByName("facebook");
        assertFalse(result.isPresent());
    }

    @Test
    public void getAffiliationsByAuthor() {
        List<Affiliation> affiliations = affiliationDao.getAffiliationsByAuthor(1L);
        assertNotNull(affiliations);
        assertEquals(1, affiliations.size());
        assertEquals(1L, affiliations.get(0).getId().longValue());
    }

    @Test
    public void getAffiliationsByConference() {
        List<Affiliation> affiliations = affiliationDao.getAffiliationsByConference(1L);
        assertNotNull(affiliations);
        assertEquals(2, affiliations.size());
    }
}