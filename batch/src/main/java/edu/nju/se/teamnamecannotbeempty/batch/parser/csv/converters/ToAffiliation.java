package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ToAffiliation extends AbstractCsvConverter {
    private static final ConcurrentHashMap<String, Affiliation> saveMap = new ConcurrentHashMap<>();
    private static Map<String, String> synonyms = new HashMap<>();

    @Override
    public Object convertToRead(String value) {
        value = value.trim();
        if (value.isEmpty()) return null;
        String formatted = value;
        for (Map.Entry<String, String> entry : synonyms.entrySet()) {
            formatted = formatted.replaceAll(entry.getKey(), entry.getValue());
        }
        Affiliation result = saveMap.get(formatted);
        if (result == null) {
            synchronized (saveMap) {
                if ((result = saveMap.get(formatted)) == null) {
                    result = new Affiliation();
                    result.setName(value);
                    result.setFormattedName(formatted);
                    saveMap.put(formatted, result);
                }
            }
        }
        return result;
    }

    public static List<Affiliation> getSaveList() {
        return new ArrayList<>(saveMap.values());
    }

    static {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        ToAffiliation.class.getResourceAsStream("/synonyms.txt"),
                        StandardCharsets.UTF_8)
        );
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\\.", "\\.");
                String[] ss = line.split(" ");
                synonyms.put(ss[0], ss[1]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            synonyms = Collections.unmodifiableMap(synonyms);
        }
    }
}
