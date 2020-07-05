package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import edu.nju.se.teamnamecannotbeempty.data.domain.Conference;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToConference extends AbstractBeanField<Conference, Object> {
    private static final HashMap<String, Conference> saveMap = new HashMap<>();
    private static final Conference na;
    private static final Pattern ordnoPat = Pattern.compile("(\\d*(?:1st|2nd|3rd|[0-9]th)) ");

    static {
        na = new Conference();
        na.setName("NA");
        saveMap.put("NA", na);
    }

    @Override
    protected Object convert(String value) {
        value = value.trim();
        if (!StringUtils.isBlank(value)) {
            Conference result = saveMap.get(value);
            if (result != null) {
                return result;
            }
            Integer ordno = null;
            Matcher matcher = ordnoPat.matcher(value);
            if (matcher.find()) {
                String ordnoStr = matcher.group(1);
                if (!StringUtils.isEmpty(ordnoStr)) {
                    ordno = Integer.parseInt(ordnoStr.substring(0, ordnoStr.length() - 2));
                }
            }

            if ((result = saveMap.get(value)) == null) {
                synchronized (saveMap) {
                    if ((result = saveMap.get(value)) == null) {
                        Conference conference = new Conference();
                        conference.setOrdno(ordno);
                        conference.setYear(findYear(value));
                        conference.setName(value);
                        return saveMap.put(value, conference);
                    }
                }
            }
            return result;
        } else {
            return na;
        }
    }

    private Integer findYear(String value) {
        String[] split = value.split(" ", 0);
        Integer year = null;
        for (String s : split) {
            if (s.length() == 4 && s.charAt(0) != '0' && StringUtils.isNumeric(s)) {
                year = Integer.parseInt(s);
                break;
            }
        }
        return year;
    }

    public static Collection<Conference> getSaveCollection() {
        return saveMap.values();
    }
}
