package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.mode;

import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.MatchNoDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.SimpleQueryStringMatchingContext;
import org.hibernate.search.query.dsl.TermMatchingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

import static edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.SearchMappingFactory.*;

@Component("Advanced")
public class AdvancedSearch extends SearchMode {
    private final FullTextEntityManager fullTextEntityManager;
    private static final HashMap<String, String> fieldMapping = new HashMap<>();
    private final ThreadLocal<String> currentQuery = new ThreadLocal<>();
    public static final Logger logger = LoggerFactory.getLogger(AdvancedSearch.class);

    public static final String USER_QUERY_TITLE = "Title";
    public static final String USER_QUERY_AUTHOR = "Author";
    public static final String USER_QUERY_AFFILIATION = "Affiliation";
    public static final String USER_QUERY_CONFERENCE = "Publication";
    public static final String USER_QUERY_KEYWORD = "Keyword";
    public static final String USER_QUERY_YEAR = "Year";

    static {
        fieldMapping.put(USER_QUERY_TITLE, getFieldName_title());
        fieldMapping.put(USER_QUERY_AUTHOR, getFieldName_author());
        fieldMapping.put(USER_QUERY_AFFILIATION, getFieldName_affiliation());
        fieldMapping.put(USER_QUERY_CONFERENCE, getFieldName_conference());
        fieldMapping.put(USER_QUERY_KEYWORD, getFieldName_authorKeywords());
        fieldMapping.put(USER_QUERY_YEAR, getFieldName_searchYear());
    }

    @Autowired
    public AdvancedSearch(EntityManager entityManager) {
        fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    }

    @Override
    public TermMatchingContext getFieldsBaseOnKeyword(QueryBuilder queryBuilder) {
        return queryBuilder.keyword().onField(getFieldName_title());
    }

    @Override
    public SimpleQueryStringMatchingContext getFieldsBaseOnSQS(QueryBuilder queryBuilder) {
        return queryBuilder.simpleQueryString().onField(getFieldName_title());
    }

    @Override
    public Query buildQuery(QueryBuilder queryBuilder, String searchString) {
        String availableQuery = searchString;
        for (Map.Entry<String, String> entry : fieldMapping.entrySet()) {
            String from = entry.getKey();
            String to = entry.getValue();
            availableQuery = availableQuery.replace(from + ":", to + ":");
        }
        Analyzer analyzer = fullTextEntityManager.getSearchFactory().getAnalyzer("noStopWords");
        QueryParser parser = new QueryParser(getFieldName_title(), analyzer);
        Query query = new MatchNoDocsQuery();
        try {
            query = parser.parse(availableQuery);
            currentQuery.set(searchString);
        } catch (ParseException e) {
            logger.warn(String.format("Search string \"%s\" can't parse to query", searchString));
        }
        return query;
    }

    @Override
    public void highlight(Highlighter highlighter, Analyzer analyzer, Paper paper) {
        String query = currentQuery.get();
        if (query != null) {
            for (String field : fieldMapping.keySet()) {
                if (query.contains(field + ":"))
                    highlight(highlighter, analyzer, paper, field);
            }
            currentQuery.remove();
        }
    }

    private void highlight(Highlighter highlighter, Analyzer analyzer, Paper paper, String field) {
        switch (field) {
            case USER_QUERY_TITLE:
                highlightTitle(paper, highlighter, analyzer);
                break;
            case USER_QUERY_AUTHOR:
                highlightAuthor(paper, highlighter, analyzer);
                break;
            case USER_QUERY_AFFILIATION:
                highlightAffiliation(paper, highlighter, analyzer);
                break;
            case USER_QUERY_CONFERENCE:
                highlightConference(paper, highlighter, analyzer);
                break;
            case USER_QUERY_KEYWORD:
                highlightKeyword(paper, highlighter, analyzer);
                break;
            case USER_QUERY_YEAR:
                highlightYear(paper, highlighter, analyzer);
            default:
                break;
        }
    }
}
