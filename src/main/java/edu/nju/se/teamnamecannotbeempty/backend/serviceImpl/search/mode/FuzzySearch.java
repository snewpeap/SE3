package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.mode;

import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Highlighter;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.SimpleQueryStringMatchingContext;
import org.hibernate.search.query.dsl.TermMatchingContext;
import org.springframework.stereotype.Component;

@Component("All")
public class FuzzySearch extends SearchMode {
    @Override
    public TermMatchingContext getFieldsBaseOnKeyword(QueryBuilder queryBuilder) {
        return queryBuilder.keyword().onFields(
                        Paper.getFieldName_title(),
                        Paper.getFieldName_author(),
                        Paper.getFieldName_affiliation(),
                        Paper.getFieldName_conference(),
                        Paper.getFieldName_authorKeywords()
                );
    }

    @Override
    public SimpleQueryStringMatchingContext getFieldsBaseOnSQS(QueryBuilder queryBuilder) {
        return queryBuilder.simpleQueryString().onFields(
                Paper.getFieldName_title(),
                Paper.getFieldName_author(),
                Paper.getFieldName_affiliation(),
                Paper.getFieldName_conference(),
                Paper.getFieldName_authorKeywords()
        );
    }

    @Override
    public void highlight(Highlighter highlighter, Analyzer analyzer, Paper paper) {
    }
}
