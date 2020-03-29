package edu.nju.se.teamnamecannotbeempty.backend.data.converters;

import com.opencsv.bean.AbstractBeanField;
import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.dao.ConferenceDao;
import edu.nju.se.teamnamecannotbeempty.backend.po.Conference;

import java.util.Optional;

public class ToConference extends AbstractBeanField<Conference, Object> {
    private final ConferenceDao conferenceDao;

    public ToConference() {
        conferenceDao = AppContextProvider.getBean(ConferenceDao.class);
    }

    @Override
    protected Object convert(String value) {
        synchronized (conferenceDao) {
            String[] parts = value.split(" ");
            Integer year = Integer.parseInt(parts[0]);
            String name;
            int ordnoIndex = 1;
            if (parts[2].equals("IEEE/ACM")) {
                name = "ASE";
            } else {
                name = "ICSE";
                ordnoIndex += 1;
            }

            Optional<Conference> result = conferenceDao.findByNameAndYear(name, year);
            if (result.isPresent())
                return result.get();
            Conference conference = new Conference();
            conference.setName(name);
            conference.setYear(year);
            conference.setOrdno(Integer.parseInt(parts[ordnoIndex].substring(0, 2)));
            return conferenceDao.saveAndFlush(conference);
        }
    }
}
