package edu.nju.se.teamnamecannotbeempty.backend.data.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.backend.po.Affiliation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ToAffiliation extends AbstractCsvConverter {
//    private final AffiliationDao affiliationDao;
    private static ConcurrentHashMap<String, Affiliation> saveMap = new ConcurrentHashMap<>();

    @Override
    public Object convertToRead(String value) {
//        Optional<Affiliation> result = affiliationDao.findByName(value); //TODO 先存在map里，在map里面找
//        if (result.isPresent())
//            return result.get();
//        Affiliation affiliation = new Affiliation();
//        affiliation.setName(value);
//        return affiliationDao.saveAndFlush(affiliation);
        Affiliation result = saveMap.get(value);
        if (result != null)
            return result;
        Affiliation affiliation = new Affiliation();
        affiliation.setName(value);
        saveMap.put(value, affiliation);
        return affiliation;
    }

    public static List<Affiliation> getSaveList() {
        List<Affiliation> saveList = new ArrayList<>(saveMap.values());
        saveMap.clear();
        return saveList;
    }
}
