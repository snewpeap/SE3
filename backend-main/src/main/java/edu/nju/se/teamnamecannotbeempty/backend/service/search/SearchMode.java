package edu.nju.se.teamnamecannotbeempty.backend.service.search;

import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.SearchMappingFactory;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.SimpleQueryStringMatchingContext;
import org.hibernate.search.query.dsl.TermMatchingContext;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class SearchMode {

    public abstract TermMatchingContext getFieldsBaseOnKeyword(QueryBuilder queryBuilder);

    @SuppressWarnings("unused")
    public abstract SimpleQueryStringMatchingContext getFieldsBaseOnSQS(QueryBuilder queryBuilder);

    public Query buildQuery(QueryBuilder queryBuilder, String searchString) {
        return getFieldsBaseOnKeyword(queryBuilder).matching(searchString).createQuery();
    }

    public abstract void highlight(Highlighter highlighter, Analyzer analyzer, Paper paper);

    protected void highlightTitle(Paper paper, Highlighter highlighter, Analyzer analyzer) {
        String hl = null;
        try {
            hl = highlighter.getBestFragment(analyzer, SearchMappingFactory.getFieldName_title(), paper.getTitle());
        } catch (IOException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            if (hl != null) {
                paper.setTitle(hl);
            }
        }
    }

    protected void highlightAuthor(Paper paper, Highlighter highlighter, Analyzer analyzer) {
        LinkedList<Author_Affiliation> highlightPriorList = new LinkedList<>();
        List<Author_Affiliation> paperAa = paper.getAa();
        for (Author_Affiliation aa : paperAa) {
            Author author = aa.getAuthor();
            Author copy = new Author();
            BeanUtils.copyProperties(author, copy);
            String hl = null;
            try {
                hl = highlighter.getBestFragment(analyzer, SearchMappingFactory.getFieldName_author(), copy.getName());
            } catch (IOException | InvalidTokenOffsetsException e) {
                e.printStackTrace();
            } finally {
                if (hl != null) {
                    copy.setName(hl);
                    aa.setAuthor(copy);
                    highlightPriorList.addFirst(aa);
                } else {
                    highlightPriorList.addLast(aa);
                }
            }
        }
        for (int i = 0; i < highlightPriorList.size(); i++) {
            paperAa.set(i, highlightPriorList.get(i));
        }
    }

    protected void highlightAffiliation(Paper paper, Highlighter highlighter, Analyzer analyzer) {
        LinkedList<Author_Affiliation> highlightPriorList = new LinkedList<>();
        List<Author_Affiliation> paperAa = paper.getAa();
        for (Author_Affiliation aa : paperAa) {
            Affiliation affiliation = aa.getAffiliation();
            Affiliation copy = new Affiliation();
            BeanUtils.copyProperties(affiliation, copy);
            String hl = null;
            try {
                hl = highlighter.getBestFragment(analyzer, SearchMappingFactory.getFieldName_affiliation(), copy.getName());
            } catch (IOException | InvalidTokenOffsetsException e) {
                e.printStackTrace();
            } finally {
                if (hl != null) {
                    copy.setName(hl);
                    aa.setAffiliation(copy);
                    highlightPriorList.addFirst(aa);
                } else {
                    highlightPriorList.addLast(aa);
                }
            }
        }
        for (int i = 0; i < highlightPriorList.size(); i++) {
            paperAa.set(i, highlightPriorList.get(i));
        }
    }

    protected void highlightConference(Paper paper, Highlighter highlighter, Analyzer analyzer) {
        Conference conference = paper.getConference();
        if (conference == null) return;
        String hl = null;
        Conference copy = new Conference();
        BeanUtils.copyProperties(paper.getConference(), copy);
        try {
            hl = highlighter.getBestFragment(analyzer, SearchMappingFactory.getFieldName_conference(), copy.getName());
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
        LinkedList<Term> highlightPriorList = new LinkedList<>();
        for (Term term : paper.getAuthor_keywords()) {
            Term copy = new Term();
            BeanUtils.copyProperties(term, copy);
            String hl = null;
            try {
                hl = highlighter.getBestFragment(analyzer, SearchMappingFactory.getFieldName_authorKeywords(), copy.getContent());
            } catch (IOException | InvalidTokenOffsetsException e) {
                e.printStackTrace();
            } finally {
                if (hl != null) {
                    copy.setContent(hl);
                    highlightPriorList.addFirst(copy);
                } else {
                    highlightPriorList.addLast(copy);
                }
            }
        }
        paper.setAuthor_keywords(highlightPriorList);
    }

    protected void highlightYear(Paper paper, Highlighter highlighter, Analyzer analyzer) {
        String hlyear = String.valueOf(paper.getYear());
        try {
            hlyear = highlighter.getBestFragment(analyzer, SearchMappingFactory.getFieldName_searchYear(), hlyear);
        } catch (IOException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            if (hlyear != null) {
                paper.setYear_highlight(hlyear);
            }
        }
    }
}
