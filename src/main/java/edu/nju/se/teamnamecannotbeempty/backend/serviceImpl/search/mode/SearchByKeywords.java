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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component("Keyword")
public class SearchByKeywords extends SearchMode {
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
    public void highlight(Highlighter highlighter, Analyzer analyzer, Paper paper) {
        List<Term> author_keywords = paper.getAuthor_keywords();
        for (int i = 0; i < author_keywords.size(); i++) {
            Term term = author_keywords.get(i);
            Term copy = new Term();
            BeanUtils.copyProperties(term, copy);
            String hl = null;
            try {
                hl = highlighter.getBestFragment(analyzer, Paper.getFieldName_authorKeywords(), copy.getContent());
            } catch (IOException | InvalidTokenOffsetsException e) {
                e.printStackTrace();
            } finally {
                if (hl != null) {
                    copy.setContent(hl);
                    author_keywords.set(i, copy);
                }
            }
        }
    }
}
