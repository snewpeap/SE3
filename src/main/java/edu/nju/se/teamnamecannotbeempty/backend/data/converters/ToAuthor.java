package edu.nju.se.teamnamecannotbeempty.backend.data.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.dao.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.backend.po.Author;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ToAuthor extends AbstractCsvConverter {
    private final AuthorDao authorDao;

    public ToAuthor() {
        authorDao = AppContextProvider.getBean(AuthorDao.class);
    }

    @Override
    public Object convertToRead(String value) {
        synchronized (authorDao) {
            Optional<Author> result = authorDao.findByName(value);
            if (result.isPresent())
                return result.get();
            Author author = new Author();
            author.setName(value);
            return authorDao.saveAndFlush(author);
        }
    }
}
