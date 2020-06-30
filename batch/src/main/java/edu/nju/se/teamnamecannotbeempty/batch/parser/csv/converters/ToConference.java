package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import edu.nju.se.teamnamecannotbeempty.batch.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.data.domain.Conference;
import edu.nju.se.teamnamecannotbeempty.data.repository.ConferenceDao;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToConference extends AbstractBeanField<Conference, Object> {
    private final ConferenceDao conferenceDao;
    private static final Pattern ordnoPat = Pattern.compile("(\\d*(?:1st|2nd|3rd|[4-9]th)) ");
    private static final Pattern yearPat = Pattern.compile("(\\d{4}) ");

    public ToConference() {
        conferenceDao = AppContextProvider.getBean(ConferenceDao.class);
    }

    @Override
    protected Object convert(String value) {
        value = value.trim();
        synchronized (conferenceDao) {
            if (!StringUtils.isEmpty(value)) {
                Optional<Conference> result = conferenceDao.findByName(value);
                if (result.isPresent())
                    return result.get();
                Conference conference = new Conference();
                Matcher matcher = ordnoPat.matcher(value);
                if (matcher.find()) {
                    String ordnoStr = matcher.group(1);
                    if (!StringUtils.isEmpty(ordnoStr)) {
                        conference.setOrdno(Integer.parseInt(ordnoStr.substring(0, ordnoStr.length() - 2)));
                    }
                }
                matcher = yearPat.matcher(value);
                if (matcher.find()) {
                    String yearStr = matcher.group(1);
                    if (!StringUtils.isEmpty(yearStr)) {
                        conference.setYear(Integer.parseInt(yearStr));
                    }
                }

                conference.setName(value);
                return conferenceDao.saveAndFlush(conference);
            } else {
                return null;
            }
        }
    }
}
