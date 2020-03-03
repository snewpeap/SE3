package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.mode;

import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.SimpleQueryStringMatchingContext;
import org.hibernate.search.query.dsl.TermMatchingContext;
import org.springframework.stereotype.Component;

@Component("Keyword")
public class SearchByKeywords implements SearchMode {
    @Override
    public TermMatchingContext getFieldsBaseOnKeyword(QueryBuilder queryBuilder) {
        return queryBuilder.keyword().onFields(
                Paper.getFieldName_authorKeywords(),
                Paper.getFieldName_ieeeTerms(),
                Paper.getFieldName_inspecControlled(),
                Paper.getFieldName_inspecNonControlled()
        );
    }

    @Override
    public SimpleQueryStringMatchingContext getFieldsBaseOnSQS(QueryBuilder queryBuilder) {
        return queryBuilder.simpleQueryString().onFields(
                Paper.getFieldName_authorKeywords(),
                Paper.getFieldName_ieeeTerms(),
                Paper.getFieldName_inspecControlled(),
                Paper.getFieldName_inspecNonControlled()
        );
    }
}
