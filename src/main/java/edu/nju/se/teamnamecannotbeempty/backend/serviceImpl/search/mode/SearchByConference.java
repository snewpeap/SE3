package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.mode;

import edu.nju.se.teamnamecannotbeempty.backend.po.Conference;
import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.SimpleQueryStringMatchingContext;
import org.hibernate.search.query.dsl.TermMatchingContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("Publication")
public class SearchByConference extends SearchMode {
    @Override
    public TermMatchingContext getFieldsBaseOnKeyword(QueryBuilder queryBuilder) {
        return queryBuilder.keyword().onFields(Paper.getFieldName_conference(), Paper.getFieldName_searchYear());
    }

    @Override
    public SimpleQueryStringMatchingContext getFieldsBaseOnSQS(QueryBuilder queryBuilder) {
        return queryBuilder.simpleQueryString().onFields(Paper.getFieldName_conference(), Paper.getFieldName_searchYear());
    }

    @Override
    public void highlight(Highlighter highlighter, Analyzer analyzer, Paper paper) {
        String hl = null, hlyear = String.valueOf(paper.getConference().getYear());
        Conference copy = new Conference();
        BeanUtils.copyProperties(paper.getConference(), copy);
        copy.setYear_highlight(hlyear);
        try {
            hl = highlighter.getBestFragment(analyzer, Paper.getFieldName_conference(), copy.getName());
            hlyear = highlighter.getBestFragment(analyzer, Paper.getFieldName_searchYear(), hlyear);
        } catch (IOException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            if (hl != null) {
                copy.setName(hl);
            }
            if (hlyear != null) {
                copy.setYear_highlight(hlyear);
            }
            paper.setConference(copy);
        }
    }
}
