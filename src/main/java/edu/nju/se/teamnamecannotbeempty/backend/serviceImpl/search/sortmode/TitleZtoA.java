package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.sortmode;

import edu.nju.se.teamnamecannotbeempty.backend.service.search.SortMode;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;

@Service
public class TitleZtoA implements SortMode {
    private static Sort sort;

    public TitleZtoA() {
        sort = new Sort(
                new SortField("title", SortField.Type.STRING, true),
                SortField.FIELD_SCORE
        );
    }

    @Override
    public Sort getSort() {
        return sort;
    }
}
