package edu.nju.se.teamnamecannotbeempty.batch.job.generators;

import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAuthor;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAuthorDao;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Component
public class AuthorDupGenerator {
    private final DuplicateAuthorDao duplicateAuthorDao;
    private final AuthorDao authorDao;
    private final AuthorPopGenerator authorPopGenerator;

    @Autowired
    public AuthorDupGenerator(DuplicateAuthorDao duplicateAuthorDao, AuthorDao authorDao, AuthorPopGenerator authorPopGenerator) {
        this.duplicateAuthorDao = duplicateAuthorDao;
        this.authorDao = authorDao;
        this.authorPopGenerator = authorPopGenerator;
    }

    @Async
    public void generateAuthorDup(Future<?> waitForImport) {
        while (waitForImport.isDone()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Generate duplicate authors aborted due to " + e.getMessage());
                return;
            }
        }
        ArrayListValuedHashMap<Author, Author> cache = new ArrayListValuedHashMap<>();
        authorDao.getAll().forEach(author -> {
            String authorName = author.getLowerCaseName();
            String[] parts = authorName.split(" ");
            if (parts.length >= 2) {
                String firstPrefix = String.valueOf(parts[0].charAt(0)).toLowerCase();
                String lastName = parts[parts.length - 1].toLowerCase();
                List<Author> suspects = // 名的首字母相同且姓相同
                        authorDao.findByLowerCaseNameIsLikeAndIdIsNot(firstPrefix + "% " + lastName, author.getId());
                for (Author suspect : suspects) {
                    String suspectName = suspect.getLowerCaseName();
                    if (!cache.containsKey(suspect) ||
                            (cache.containsKey(suspect) && !cache.get(suspect).contains(author))) {
                        // 如果已经存在a-b，b-a不会被加入以防止成环
                        if (isSimilar(parts, suspectName.split(" "))) {
                            cache.put(author, suspect);
                            duplicateAuthorDao.save(new DuplicateAuthor(author, suspect));
                        }
                    }
                }
            }
        });
        logger.info("Done generate duplicate authors");
    }

    private boolean isSimilar(String[] parts, String[] suspectParts) {
        boolean partsIsLess = parts.length < suspectParts.length;
        String[] less = partsIsLess ? parts : suspectParts;
        String[] more = partsIsLess ? suspectParts : parts;
        boolean isSimilar = true;
        for (int i = 1; i < less.length - 1; i++) {
            less[i] = less[i].replace(".", "");
            for (int j = i; j < more.length - 1; j++) {
                if (more[j].startsWith(less[i])) break;
                isSimilar = false;
            }
            if (!isSimilar) break;
        }
        return isSimilar;
    }

    @Async
    public void refresh(Date date) {
        duplicateAuthorDao.findByUpdatedAtAfter(date).forEach(dup -> authorPopGenerator.generatePop(dup.getSon()));
    }

    private static Logger logger = LoggerFactory.getLogger(AuthorDupGenerator.class);
}
