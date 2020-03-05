package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.mode;

import edu.nju.se.teamnamecannotbeempty.backend.po.Author_Affiliation;
import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.SimpleQueryStringMatchingContext;
import org.hibernate.search.query.dsl.TermMatchingContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("Author")
public class SearchByAuthor implements SearchMode {
    @Override
    public TermMatchingContext getFieldsBaseOnKeyword(QueryBuilder queryBuilder) {
        return queryBuilder.keyword().onField(Paper.getFieldName_author());
    }

    @Override
    public SimpleQueryStringMatchingContext getFieldsBaseOnSQS(QueryBuilder queryBuilder) {
        return queryBuilder.simpleQueryString().onField(Paper.getFieldName_author());
    }

    @Override
    public Paper highlight(Highlighter highlighter, Analyzer analyzer, Paper paper) {
        for (Author_Affiliation aa : paper.getAa()) {
            String hl = null;
            try {
                hl = highlighter.getBestFragment(analyzer, Paper.getFieldName_author(), aa.getAuthor().getName());
            } catch (IOException | InvalidTokenOffsetsException e) {
                e.printStackTrace();
            } finally {
                if (hl != null)
                    aa.getAuthor().setName(hl);
            }
        }
        return paper;
    }
}
