package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search;

import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import org.apache.lucene.analysis.charfilter.MappingCharFilterFactory;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.bridge.builtin.IntegerBridge;
import org.hibernate.search.cfg.SearchMapping;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;

@Component
public class SearchMappingFactory {
    public static String getFieldName_title() {
        return "title";
    }

    public static String getFieldName_author() {
        return "aa.author.name";
    }

    public static String getFieldName_affiliation() {
        return "aa.affiliation.name";
    }

    public static String getFieldName_conference() {
        return "conference.name";
    }

    public static String getFieldName_searchYear() {
        return "search_year";
    }

    public static String getFieldName_authorKeywords() {
        return "author_keywords.content";
    }

    @Factory
    public SearchMapping getSearchMapping() {
        final String analyzerName = "noStopWords";
        SearchMapping searchMapping = new SearchMapping();
        searchMapping
                //analyzer definition
                .analyzerDef(analyzerName, StandardTokenizerFactory.class)
                    .filter(StandardFilterFactory.class)
                    .filter(LowerCaseFilterFactory.class)
                    .filter(SnowballPorterFilterFactory.class).param("language", "English")
                    .filter(ASCIIFoldingFilterFactory.class)
                    .charFilter(MappingCharFilterFactory.class).param("mapping", "mapping.txt")
                //Paper as root entity
                .entity(Paper.class).indexed()
                    .property("title", ElementType.METHOD)
                        .field().name("title").analyzer(analyzerName)
                        .field().name("sortTitle").index(Index.NO).analyze(Analyze.NO).sortableField()
                    .property("aa", ElementType.METHOD)
                        .indexEmbedded()
                    .property("conference", ElementType.METHOD)
                        .indexEmbedded()
                    .property("author_keywords", ElementType.METHOD)
                        .indexEmbedded()
                    .property("year", ElementType.METHOD)
                        .field().name("year").analyzer(analyzerName).numericField().sortableField()
                        .field().name("search_year").bridge(IntegerBridge.class).analyzer(KeywordAnalyzer.class)
                //configure aa
                .entity(Author_Affiliation.class)
                    .property("author", ElementType.METHOD)
                        .indexEmbedded().depth(1)
                    .property("affiliation", ElementType.METHOD)
                        .indexEmbedded().depth(1)
                //configure Author
                .entity(Author.class)
                    .property("name", ElementType.METHOD).field().analyzer(analyzerName)
                //configure Affiliation
                .entity(Affiliation.class)
                    .property("name", ElementType.METHOD).field().analyzer(analyzerName)
                //configure Conference
                .entity(Conference.class)
                    .property("name", ElementType.METHOD).field().analyzer(analyzerName)
                //configure Term
                .entity(Term.class)
                    .property("content", ElementType.METHOD).field().analyzer(analyzerName);
        return searchMapping;
    }
}
