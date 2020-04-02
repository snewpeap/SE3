package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ToAuthor extends AbstractCsvConverter {
    private static ConcurrentHashMap<String, Author> saveMap = new ConcurrentHashMap<>();

    @Override
    public Object convertToRead(String value) {
        if (value.isEmpty()) return null;
        Author result = saveMap.get(value);
        if (result != null)
            return result;
        Author author = new Author();
        author.setName(value);
        author.setLowerCaseName(value.toLowerCase().replace(".", ""));
        saveMap.put(value, author);
        return author;
    }

    public static List<Author> getSaveList() {
        return new ArrayList<>(saveMap.values());
    }
}
