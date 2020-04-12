import edu.nju.se.teamnamecannotbeempty.data.DataConfig;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("daotest")
@ContextConfiguration(classes = DataConfig.class)
public class TermPopDaoTest {
    @Autowired
    private TermPopDao termPopDao;

    @Test
    public void findTop20ByOrderByPopularityDesc() {
    }

    @Test
    @Sql(statements = {"insert into term_popularity(popularity, term_id) VALUES (1.0, 1);"})
    public void getTermPopByAuthorID() {
        List<Term.Popularity> pops = termPopDao.getTermPopByAuthorID(1L);
        assertNotNull(pops);
        assertEquals(1, pops.size());
        assertEquals("data", pops.get(0).getTerm().getContent());
    }
}