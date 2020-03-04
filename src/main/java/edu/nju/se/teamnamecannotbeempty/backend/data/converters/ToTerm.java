package edu.nju.se.teamnamecannotbeempty.backend.data.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.dao.TermDao;
import edu.nju.se.teamnamecannotbeempty.backend.po.Term;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ToTerm extends AbstractCsvConverter {
    private final TermDao termDao;

    public ToTerm() {
        termDao = AppContextProvider.getBean(TermDao.class);
    }

    @Override
    public Object convertToRead(String value) {
        synchronized (termDao) {
            Optional<Term> result = termDao.findByContent(value);
            if (result.isPresent())
                return result.get();
            Term term = new Term();
            term.setContent(value);
            return termDao.saveAndFlush(term);
        }
    }
}
