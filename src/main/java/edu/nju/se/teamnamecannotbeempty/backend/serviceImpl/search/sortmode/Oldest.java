package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.sortmode;

import edu.nju.se.teamnamecannotbeempty.backend.service.search.SortMode;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;

@Service("Oldest")
public class Oldest implements SortMode {
    private static Sort sort;

    public Oldest() {
        sort = new Sort(
                new SortField("conference.year", SortField.Type.INT),
                SortField.FIELD_SCORE
        );
    }

    @Override
    public Sort getSort() {
        return sort;
    }

}
