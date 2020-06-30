package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ToAuthor extends AbstractCsvConverter {
    private static final ConcurrentHashMap<String, Author> saveMap = new ConcurrentHashMap<>();

    @Override
    public Object convertToRead(String value) {
        value = value.trim();
        if (value.isEmpty()) return null;
        String lowercase = value.toLowerCase();
        Author result = saveMap.get(lowercase);
        if (result == null) {
            synchronized (saveMap) {
                if ((result = saveMap.get(lowercase)) == null) {
                    result = new Author();
                    result.setName(value);
                    result.setLowerCaseName(lowercase);
                    saveMap.put(lowercase, result);
                }
            }
        }
        return result;
    }

    public static List<Author> getSaveList() {
        return new ArrayList<>(saveMap.values());
    }

}
