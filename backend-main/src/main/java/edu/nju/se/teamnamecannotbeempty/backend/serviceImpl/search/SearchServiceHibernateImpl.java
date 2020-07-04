package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search;

import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchService;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SortMode;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.sortmode.Relevance;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.hibernate.Session;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class SearchServiceHibernateImpl implements SearchService {
    private final EntityManager entityManager;
    private final Indexer indexer;

    @Autowired
    public SearchServiceHibernateImpl(EntityManager entityManager, Indexer indexer) {
        this.entityManager = entityManager;
        hibernateSearchInit();
        this.indexer = indexer;
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

        //noinspection unchecked
        List<Paper> result = fullTextQuery.getResultList();

        for (Paper paper : result) {
            entityManager.unwrap(Session.class).evict(paper);
            mode.highlight(
                    highlighter,
                    fullTextEntityManager.getSearchFactory().getAnalyzer("noStopWords"),
                    paper
            );
        }

        return new PageImpl<>(result, pageable, total);
    }

    public void flushIndexes() {
        indexer.flushIndexes();
    }

    @Component
    public static class Indexer {
        private final Searchable searchable;
        private final EntityManager entityManager;

        public Indexer(Searchable searchable, EntityManager entityManager) {
            this.searchable = searchable;
            this.entityManager = entityManager;
        }

        @Async
        public void flushIndexes() {
            long startTime = System.currentTimeMillis();
            final long DEADLINE = 1000 * 60 * 15;
            searchable.startIndexing();
            while (!searchable.importOK()) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (System.currentTimeMillis() - startTime > DEADLINE) {
                    logger.error("Data import job seen to be failed. Abort indexes flushing.");
                    searchable.endIndexing();
                    return;
                }
            }
            try {
                Search.getFullTextEntityManager(entityManager).createIndexer().startAndWait();
            } catch (InterruptedException e) {
                logger.error("Index procedure failed!");
            } finally {
                searchable.endIndexing();
            }
        }
        private final Logger logger = LoggerFactory.getLogger(SearchServiceHibernateImpl.class);
    }
}
