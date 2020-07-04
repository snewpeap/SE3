package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;

import java.util.Collection;
import java.util.HashMap;

public class ToAuthor extends AbstractCsvConverter {
    private static final HashMap<String, Author> saveMap = new HashMap<>();

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

    public static Collection<Author> getSaveCollection() {
        return saveMap.values();
    }

}
