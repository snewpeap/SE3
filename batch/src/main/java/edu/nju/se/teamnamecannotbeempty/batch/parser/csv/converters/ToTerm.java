package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ToTerm extends AbstractCsvConverter {
    //    private final TermDao termDao;
    private static ConcurrentHashMap<String, Term> saveMap = new ConcurrentHashMap<>();

    @Override
    public Object convertToRead(String value) {
//        synchronized (termDao) {
//            Optional<Term> result = termDao.findByContentIgnoreCase(value);
//            if (result.isPresent())
//                return result.get();
//            Term term = new Term();
//            term.setContent(value);
//            return termDao.saveAndFlush(term);
//        }
        value = value.toLowerCase();
        Term result = saveMap.get(value);
        if (result != null)
            return result;
        Term term = new Term();
        term.setContent(value);
        saveMap.put(value, term);
        return term;
    }

    public static List<Term> getSaveList() {
        return new ArrayList<>(saveMap.values());
    }
}
