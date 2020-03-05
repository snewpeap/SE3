package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.mode;

import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import edu.nju.se.teamnamecannotbeempty.backend.po.Term;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.SimpleQueryStringMatchingContext;
import org.hibernate.search.query.dsl.TermMatchingContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    @Override
    public Paper highlight(Highlighter highlighter, Analyzer analyzer, Paper paper) {
        for (Term term : paper.getAuthor_keywords()) {
            String hl = null;
            try {
                hl = highlighter.getBestFragment(analyzer, Paper.getFieldName_authorKeywords(), term.getContent());
            } catch (IOException | InvalidTokenOffsetsException e) {
                e.printStackTrace();
            } finally {
                if (hl != null)
                    term.setContent(hl);
            }
        }
        return paper;
    }
}
