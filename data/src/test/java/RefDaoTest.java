import edu.nju.se.teamnamecannotbeempty.data.DataConfig;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.RefDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("daotest")
@ContextConfiguration(classes = DataConfig.class)
public class RefDaoTest {
    @Autowired
    private RefDao refDao;
    @Autowired
    private PaperDao paperDao;

    @Test
    public void findByRefereeIsNotNull() {
        Paper paper = new Paper();  paper.setId(10L);   paper.setTitle("t");
        Ref ref = new Ref("a");
        paper.addRef(ref);
        paperDao.save(paper);
        assertEquals(0, refDao.findByRefereeIsNotNull().size());
    }

    @Test
    public void findByLowercaseTitleEqualsAndRefereeIsNull() {
        Paper paper = new Paper();  paper.setId(10L);   paper.setTitle("0");
        Paper paper1 = new Paper(); paper1.setId(100L);  paper1.setTitle("1");
        Paper paper2 = new Paper(); paper2.setId(1000L); paper2.setTitle("2");
        Ref ref = new Ref("2");
        paper.addRef(ref);
        Ref ref1 = new Ref("2");
        ref1.setReferee(paper2);
        paper1.addRef(ref1);
        paperDao.saveAll(Arrays.asList(paper, paper1, paper2));
        assertEquals(1, refDao.findByLowercaseTitleEqualsAndRefereeIsNull("2").size());
    }
}