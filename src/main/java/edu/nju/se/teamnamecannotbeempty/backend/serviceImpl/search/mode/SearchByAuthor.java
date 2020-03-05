package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.mode;

import edu.nju.se.teamnamecannotbeempty.backend.po.Author;
import edu.nju.se.teamnamecannotbeempty.backend.po.Author_Affiliation;
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
import java.util.List;

@Component("Author")
public class SearchByAuthor extends SearchMode {
    @Override
    public TermMatchingContext getFieldsBaseOnKeyword(QueryBuilder queryBuilder) {
        return queryBuilder.keyword().onField(Paper.getFieldName_author());
    }

    @Override
    public SimpleQueryStringMatchingContext getFieldsBaseOnSQS(QueryBuilder queryBuilder) {
        return queryBuilder.simpleQueryString().onField(Paper.getFieldName_author());
    }

    @Override
    public void highlight(Highlighter highlighter, Analyzer analyzer, Paper paper) {
        List<Author_Affiliation> paperAa = paper.getAa();
        for (Author_Affiliation aa : paperAa) {
            Author author = aa.getAuthor();
            Author copy = new Author();
            BeanUtils.copyProperties(author, copy);
            String hl = null;
            try {
                hl = highlighter.getBestFragment(analyzer, Paper.getFieldName_author(), copy.getName());
            } catch (IOException | InvalidTokenOffsetsException e) {
                e.printStackTrace();
            } finally {
                if (hl != null) {
                    copy.setName(hl);
                    aa.setAuthor(copy);
                }
            }
        }
    }
}
