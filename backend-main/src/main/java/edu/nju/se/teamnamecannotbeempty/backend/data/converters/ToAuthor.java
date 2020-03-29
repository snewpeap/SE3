package edu.nju.se.teamnamecannotbeempty.backend.data.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.backend.po.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ToAuthor extends AbstractCsvConverter {
    //    private final AuthorDao authorDao;
    private static ConcurrentHashMap<String, Author> saveMap = new ConcurrentHashMap<>();

    @Override
    public Object convertToRead(String value) {
//        synchronized (authorDao) {
//            Optional<Author> result = authorDao.findByName(value);
//            if (result.isPresent())
//                return result.get();
//            Author author = new Author();
//            author.setName(value);
//            return authorDao.saveAndFlush(author);
//        }
        Author result = saveMap.get(value);
        if (result != null)
            return result;
        Author author = new Author();
        author.setName(value);
        saveMap.put(value, author);
        return author;
    }

    public static List<Author> getSaveList() {
        return new ArrayList<>(saveMap.values());
    }
}
