package edu.nju.se.teamnamecannotbeempty.backend.service.search;

import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.mode.*;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.sortmode.*;
import edu.nju.se.teamnamecannotbeempty.data.HibernateSearchConfig;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import org.hibernate.search.exception.EmptyQueryException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DataJpaTest(includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.*"
        ),
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {HibernateSearchConfig.class, AppContextProvider.class}
        ),
        @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "edu.nju.se.teamnamecannotbeempty.data.*"
        )
})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchServiceTest {
    @Autowired
    private SearchService searchService;

    @Before
    public void setUp() {
        System.out.println("set up");
    }

    @Test
    public void testSearch_searchByTitle_noPage_relevance() {
        Page<Paper> result = null;
        for (int i = 0; i < 5; i++) {
            result = searchService.search(
                    "data",
                    AppContextProvider.getBean(SearchByTitle.class),
                    Pageable.unpaged()
            );
        }
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
    }

    @Test
    public void testSearch_searchByTitle_noPage_newest() {
        Page<Paper> result = searchService.search(
                "mining",
                AppContextProvider.getBean(SearchByTitle.class),
                Pageable.unpaged(),
                AppContextProvider.getBean(Newest.class)
        );
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        List<Paper> papers = result.toList();
        assertThat(papers.get(0).getConference().getYear(), greaterThan(papers.get(1).getConference().getYear()));
    }

    @Test
    public void testSearch_searchByTitle_noPage_oldest() {
        Page<Paper> result = searchService.search(
                "mining",
                AppContextProvider.getBean(SearchByTitle.class),
                Pageable.unpaged(),
                AppContextProvider.getBean(Oldest.class)
        );
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        List<Paper> papers = result.toList();
        assertThat(papers.get(0).getConference().getYear(), lessThan(papers.get(1).getConference().getYear()));
    }

    @Test
    public void testSearch_searchByTitle_noPage_AtoZ() {
        Page<Paper> result = searchService.search(
                "mining",
                AppContextProvider.getBean(SearchByTitle.class),
                Pageable.unpaged(),
                AppContextProvider.getBean(TitleAtoZ.class)
        );
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        List<Paper> papers = result.toList();
//        assertThat(papers.get(0).getTitle(), lessThan(papers.get(1).getTitle()));
    }

    @Test
    public void testSearch_searchByTitle_noPage_ZtoA() {
        Page<Paper> result = searchService.search(
                "mining",
                AppContextProvider.getBean(SearchByTitle.class),
                Pageable.unpaged(),
                AppContextProvider.getBean(TitleZtoA.class)
        );
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        List<Paper> papers = result.toList();
//        assertThat(papers.get(0).getTitle(), greaterThan(papers.get(1).getTitle()));
    }

    @Test
    public void testSearch_searchByConference_noPage_relevance() {
        Page<Paper> result = searchService.search(
                "ase 2000",
                AppContextProvider.getBean(SearchByConference.class),
                Pageable.unpaged()
        );
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        result.forEach(paper -> Assert.assertEquals(2000, paper.getConference().getYear().intValue()));
    }

    @Test
    public void testSearch_searchByConference_noPage_relevance_5() {
        Page<Paper> result = null;
        for (int i = 0; i < 5; i++) {
            result = searchService.search(
                    "ase",
                    AppContextProvider.getBean(SearchByConference.class),
                    Pageable.unpaged()
            );
        }
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        result.forEach(paper -> Assert.assertTrue(paper.getConference().getName().contains("ase")));
    }

    @Test
    public void testSearch_searchByAuthor_noPage_relevance() {
        Page<Paper> result = searchService.search(
                "a",
                AppContextProvider.getBean(SearchByAuthor.class),
                Pageable.unpaged()
        );
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        result.forEach(paper -> assertThat(
                paper.getAa(), hasItem(
                        hasProperty("author",
                                hasProperty("name", containsString("a"))
                        )
                )
        ));
    }

    @Test
    public void testSearch_searchByAffiliation_noPage_relevance() {
        Page<Paper> result = searchService.search(
                "nju",
                AppContextProvider.getBean(SearchByAffiliation.class),
                Pageable.unpaged()
        );
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        result.forEach(paper -> assertThat(
                paper.getAa(), hasItem(
                        hasProperty("affiliation",
                                hasProperty("name", containsString("nju"))
                        )
                )
        ));
    }

    @Test
    public void testSearch_searchByAuthorKeyword_noPage_relevance() {
        Page<Paper> result = searchService.search(
                "data",
                AppContextProvider.getBean(SearchByKeywords.class),
                Pageable.unpaged()
        );
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        result.forEach(paper -> assertThat(
                paper.getAuthor_keywords(), hasItem(
                        hasProperty("content", containsString("data"))
                )
        ));
    }

    @Test
    public void testSearch_searchByAuthorKeywords_noPage_relevance() {
        Page<Paper> result = searchService.search(
                "data mining",
                AppContextProvider.getBean(SearchByKeywords.class),
                Pageable.unpaged()
        );
        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getNumberOfElements());
    }

    @Test
    public void testSearch_searchBy2AuthorKeywords_2Pages_relevance() {
        Page<Paper> result = searchService.search(
                "data mining",
                AppContextProvider.getBean(SearchByKeywords.class),
                PageRequest.of(0, 2)
        );
        assertNotNull(result);
        assertEquals(2, result.getTotalPages());
        assertEquals(2, result.getNumberOfElements());
        result = searchService.search(
                "data mining",
                AppContextProvider.getBean(SearchByKeywords.class),
                PageRequest.of(1, 2)
        );
        assertNotNull(result);
        assertEquals(1, result.getNumberOfElements());
    }

    @Test
    public void testSearch_fuzzySearch_noPage_relevance() {
        Page<Paper> result = searchService.search(
                "data nju",
                AppContextProvider.getBean(FuzzySearch.class),
                Pageable.unpaged()
        );
        assertNotNull(result);
        System.out.println("=====fuzzy search results=====");
        result.forEach(System.out::println);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearch_null() {
        Page<Paper> result = searchService.search(
                null,
                AppContextProvider.getBean(SearchByTitle.class),
                Pageable.unpaged()
        );
        assertNotNull(result);
    }

    @Test(expected = EmptyQueryException.class)
    public void testSearch_emptyString() {
        Page<Paper> result = searchService.search(
                "",
                AppContextProvider.getBean(SearchByTitle.class),
                Pageable.unpaged()
        );
        assertNotNull(result);
    }

    @Test
    public void testSearch_bySingleKeywordAsList_noPage_relevance() {
        List<String> keywords = Collections.singletonList("data");
        Page<Paper> result1 = searchService.search(
                keywords,
                AppContextProvider.getBean(SearchByTitle.class),
                Pageable.unpaged(),
                AppContextProvider.getBean(Relevance.class)
        );
        Page<Paper> result2 = searchService.search(
                "data",
                AppContextProvider.getBean(SearchByTitle.class),
                Pageable.unpaged()
        );
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1.getTotalElements(), result2.getTotalElements());
        result1.forEach(System.out::println);
        System.out.println();
        result2.forEach(System.out::println);
    }
}
