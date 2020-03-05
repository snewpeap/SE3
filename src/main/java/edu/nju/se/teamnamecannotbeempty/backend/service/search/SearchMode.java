package edu.nju.se.teamnamecannotbeempty.backend.service.search;

import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Highlighter;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.SimpleQueryStringMatchingContext;
import org.hibernate.search.query.dsl.TermMatchingContext;

public interface SearchMode {
    TermMatchingContext getFieldsBaseOnKeyword(QueryBuilder queryBuilder);

    SimpleQueryStringMatchingContext getFieldsBaseOnSQS(QueryBuilder queryBuilder);

    Paper highlight(Highlighter highlighter, Analyzer analyzer, Paper paper);
}
