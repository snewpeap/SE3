package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search;

import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchService;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SortMode;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.sortmode.Relevance;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.hibernate.Session;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.util.List;

public class SearchServiceHibernateImpl implements SearchService {
    private final EntityManager entityManager;

    @Autowired
    public SearchServiceHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void hibernateSearchInit() {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Page<Paper> search(String keywords, SearchMode mode, Pageable pageable) {
        return search(keywords, mode, pageable, AppContextProvider.getBean(Relevance.class));
    }

    @Override
    public Page<Paper> search(List<String> keywords, SearchMode mode, Pageable pageable, SortMode sortMode) {
        Assert.notNull(keywords, "关键字列表不能为null");
        String combined = String.join(" ", keywords);
        return search(combined, mode, pageable, sortMode);
    }

    @Override
    public Page<Paper> search(String keywords, SearchMode mode, Pageable pageable, SortMode sortMode) {
        Assert.notNull(keywords, "查询不能为null");
        Assert.notNull(mode, "SearchMode不能为null");
        Assert.notNull(pageable, "Pageable不能为null");
        Assert.notNull(sortMode, "SortMode不能为null");

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Paper.class).get();
        Query luceneQuery = mode.getFieldsBaseOnKeyword(queryBuilder).matching(keywords).createQuery();
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Paper.class);

        fullTextQuery.setSort(sortMode.getSort());

        int total = fullTextQuery.getResultSize();
        int firstResultIndex = pageable.isUnpaged() ? 0 : pageable.getPageNumber() * pageable.getPageSize();
        int maxResult = pageable.isUnpaged() ? total : pageable.getPageSize();
        fullTextQuery.setFirstResult(firstResultIndex);
        fullTextQuery.setMaxResults(maxResult);

        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<b><span style=\"color: #b04c50; \">", "</span></b>");
        Highlighter highlighter = new Highlighter(formatter, new QueryScorer(luceneQuery));

        List<Paper> result = fullTextQuery.getResultList();

        for (Paper paper : result) {
            mode.highlight(
                    highlighter,
//                    new Analyzer() {
//                        @Override
//                        protected TokenStreamComponents createComponents(String fieldName) {
//                            return new TokenStreamComponents(new LetterTokenizer() {
//                                @Override
//                                protected int normalize(int c) {
//                                    return Character.toLowerCase(c);
//                                }
//
//                                @Override
//                                protected boolean isTokenChar(int c) {
//                                    return !Character.isSpaceChar(c);
//                                }
//                            });
//                        }
//                    },
                    Search.getFullTextEntityManager(entityManager).getSearchFactory().getAnalyzer("noStopWords"),
                    paper
            );
            entityManager.unwrap(Session.class).evict(paper);
        }

        return new PageImpl<>(result, pageable, total);
    }
}
