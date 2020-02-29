package edu.nju.se.teamnamecannotbeempty.backend.service.search;

import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.SimpleQueryStringMatchingContext;
import org.hibernate.search.query.dsl.TermMatchingContext;

public interface SearchMode {
    TermMatchingContext getFieldsBaseOnKeyword(QueryBuilder queryBuilder);

    SimpleQueryStringMatchingContext getFieldsBaseOnSQS(QueryBuilder queryBuilder);
}
