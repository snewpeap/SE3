import edu.nju.se.teamnamecannotbeempty.data.DataConfig;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAuthor;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAuthorDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("daotest")
@ContextConfiguration(classes = DataConfig.class)
public class DuplicateAuthorDaoTest {
    @Autowired
    private DuplicateAuthorDao duplicateAuthorDao;
    @Autowired
    private AuthorDao authorDao;

    @Test
    public void existsByFather_IdAndSon_Id() {
        Author father = new Author();
        father.setName("father");
        Author son = new Author();
        son.setName("son");
        authorDao.save(father);
        authorDao.save(son);
        DuplicateAuthor dup = new DuplicateAuthor(father, son);
        duplicateAuthorDao.save(dup);
        assertTrue(duplicateAuthorDao.existsByFather_IdAndSon_Id(dup.getFather().getId(), dup.getSon().getId()));
    }
}