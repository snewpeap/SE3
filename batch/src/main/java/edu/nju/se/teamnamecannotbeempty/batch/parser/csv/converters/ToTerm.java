package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ToTerm extends AbstractCsvConverter {
    private static final ConcurrentHashMap<String, Term> saveMap = new ConcurrentHashMap<>();

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

    public static List<Term> getSaveList() {
        return new ArrayList<>(saveMap.values());
    }

}
