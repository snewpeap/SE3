package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ToAffiliation extends AbstractCsvConverter {
    private static ConcurrentHashMap<String, Affiliation> saveMap = new ConcurrentHashMap<>();

    @Override
    public Object convertToRead(String value) {
        if (value.isEmpty()) return null;
        Affiliation result = saveMap.get(value);
        if (result != null)
            return result;
        Affiliation affiliation = new Affiliation();
        affiliation.setName(value);
        saveMap.put(value, affiliation);
        return affiliation;
    }

    public static List<Affiliation> getSaveList() {
        return new ArrayList<>(saveMap.values());
    }
}
