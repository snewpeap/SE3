package edu.nju.se.teamnamecannotbeempty.backend.service.search;

import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Highlighter;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.SimpleQueryStringMatchingContext;
import org.hibernate.search.query.dsl.TermMatchingContext;

public abstract class SearchMode {

    public abstract TermMatchingContext getFieldsBaseOnKeyword(QueryBuilder queryBuilder);

    public abstract SimpleQueryStringMatchingContext getFieldsBaseOnSQS(QueryBuilder queryBuilder);

    public abstract void highlight(Highlighter highlighter, Analyzer analyzer, Paper paper);

}
