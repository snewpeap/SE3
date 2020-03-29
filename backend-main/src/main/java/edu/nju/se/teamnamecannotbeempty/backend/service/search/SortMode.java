package edu.nju.se.teamnamecannotbeempty.backend.service.search;

import org.apache.lucene.search.Sort;

public interface SortMode {
    Sort getSort();
}
