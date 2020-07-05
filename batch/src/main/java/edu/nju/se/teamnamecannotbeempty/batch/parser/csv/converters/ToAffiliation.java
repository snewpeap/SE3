package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ToAffiliation extends AbstractCsvConverter {
    private static final HashMap<String, Affiliation> saveMap = new HashMap<>();
    private static final Affiliation na;
    private static Map<String, String> synonyms = new HashMap<>();

    static {
        na = new Affiliation();
        na.setName("NA");
        na.setFormattedName("NA");
        saveMap.put("NA", na);
    }

    @Override
    public Object convertToRead(String value) {
        value = StringUtils.abbreviateMiddle(value.trim(), "...", 254);
        if (StringUtils.isBlank(value) || value.equals(",")) return na;
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

    public static Collection<Affiliation> getSaveCollection() {
        return saveMap.values();
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
                String[] ss = line.split(" ");
                synonyms.put(ss[0].replaceAll("\\.", "\\\\."), ss[1]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            synonyms = Collections.unmodifiableMap(synonyms);
        }
    }
}
