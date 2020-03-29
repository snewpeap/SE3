package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.sortmode;

import edu.nju.se.teamnamecannotbeempty.backend.service.search.SortMode;
import org.apache.lucene.search.Sort;
import org.springframework.stereotype.Component;

@Component("Relevance")
public class Relevance implements SortMode {

    @Override
    public Sort getSort() {
        return Sort.RELEVANCE;
    }
}
