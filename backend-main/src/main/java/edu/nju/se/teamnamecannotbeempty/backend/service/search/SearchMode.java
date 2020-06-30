package edu.nju.se.teamnamecannotbeempty.backend.service.search;

import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.SimpleQueryStringMatchingContext;
import org.hibernate.search.query.dsl.TermMatchingContext;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.List;

public abstract class SearchMode {

    public abstract TermMatchingContext getFieldsBaseOnKeyword(QueryBuilder queryBuilder);

    public abstract SimpleQueryStringMatchingContext getFieldsBaseOnSQS(QueryBuilder queryBuilder);

    public abstract void highlight(Highlighter highlighter, Analyzer analyzer, Paper paper);

    protected void highlightTitle(Paper paper, Highlighter highlighter, Analyzer analyzer) {
        String hl = null;
        try {
            hl = highlighter.getBestFragment(analyzer, Paper.getFieldName_title(), paper.getTitle());
        } catch (IOException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            if (hl != null) {
                paper.setTitle(hl);
            }
        }
    }

    protected void highlightAuthor(Paper paper, Highlighter highlighter, Analyzer analyzer) {
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

    protected void highlightAffiliation(Paper paper, Highlighter highlighter, Analyzer analyzer) {
        for (Author_Affiliation aa : paper.getAa()) {
            Affiliation affiliation = aa.getAffiliation();
            Affiliation copy = new Affiliation();
            BeanUtils.copyProperties(affiliation, copy);
            String hl = null;
            try {
                hl = highlighter.getBestFragment(analyzer, Paper.getFieldName_affiliation(), copy.getName());
            } catch (IOException | InvalidTokenOffsetsException e) {
                e.printStackTrace();
            } finally {
                if (hl != null) {
                    copy.setName(hl);
                    aa.setAffiliation(copy);
                }
            }
        }
    }

    protected void highlightConference(Paper paper, Highlighter highlighter, Analyzer analyzer) {
        String hl = null;
        Conference copy = new Conference();
        BeanUtils.copyProperties(paper.getConference(), copy);
        try {
            hl = highlighter.getBestFragment(analyzer, Paper.getFieldName_conference(), copy.getName());
        } catch (IOException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            if (hl != null) {
                copy.setName(hl);
            }
            paper.setConference(copy);
        }
    }

    protected void highlightKeyword(Paper paper, Highlighter highlighter, Analyzer analyzer) {
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

    protected void highlightYear(Paper paper, Highlighter highlighter, Analyzer analyzer) {
        String hlyear = String.valueOf(paper.getYear());
        try {
            hlyear = highlighter.getBestFragment(analyzer, Paper.getFieldName_searchYear(), hlyear);
        } catch (IOException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            if (hlyear != null) {
                paper.setYear_highlight(hlyear);
            }
        }
    }
}
