import edu.nju.se.teamnamecannotbeempty.data.DataConfig;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.TermDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("daotest")
@ContextConfiguration(classes = DataConfig.class)
public class TermDaoTest {
    @Autowired
    private TermDao termDao;

    @Test
    public void findByContent() {
        Optional<Term> result = termDao.findByContent("data");
        assertTrue(result.isPresent());
        assertEquals("data", result.get().getContent());
    }

    @Test
    public void findByContentIgnoreCase() {
        Optional<Term> result = termDao.findByContentIgnoreCase("MINING");
        assertTrue(result.isPresent());
        assertEquals("mining", result.get().getContent());
    }
}