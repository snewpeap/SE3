package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;

import java.util.Collection;
import java.util.HashMap;

public class ToTerm extends AbstractCsvConverter {
    private static final HashMap<String, Term> saveMap = new HashMap<>();

    @Override
    public Object convertToRead(String value) {
        value = value.trim();
        if (value.isEmpty()) return null;
        value = value.toLowerCase();
        Term result = saveMap.get(value);
        if (result == null) {
            synchronized (saveMap) {
                if ((result = saveMap.get(value)) == null) {
                    result = new Term();
                    result.setContent(value);
                    saveMap.put(value, result);
                }
            }
        }
        return result;
    }

    public static Collection<Term> getSaveCollection() {
        return saveMap.values();
    }

}
