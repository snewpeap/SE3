package edu.nju.se.teamnamecannotbeempty.backend.data;

import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.NeedParseCSV;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create",
        "javax.persistence.schema-generation.scripts.action=none"},
        includeFilters = {
                @Filter(
                        type = FilterType.REGEX,
                        pattern = {
                                "edu.nju.se.teamnamecannotbeempty.backend.data.*",
                                "edu.nju.se.teamnamecannotbeempty.data.*"
                        }
                ),
                @Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {AppContextProvider.class, NeedParseCSV.class}
                ),
        })
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class FromCSVOpenCSVImplTest {
    @Autowired
    private FromCSVOpenCSVImpl openCSV;

    private InputStream ok, badPDFLink, noAuthor;

    @Before
    public void setUp() {
        ok = getClass().getResourceAsStream("/ok.csv");
        badPDFLink = getClass().getResourceAsStream("/badPDFLink.csv");
        noAuthor = getClass().getResourceAsStream("/noAuthor.csv");
    }

    @Test
    public void convert() {
        List<Paper> papers = openCSV.convert(ok);
        assertNotNull(papers);
        assertEquals(3, papers.size());
        assertThat(papers, hasItem(
                allOf(
                        hasProperty("title", is("java")),
                        hasProperty("citation", is(0)),
                        hasProperty("reference", is(2)),
                        hasProperty("ieee_terms", hasSize(3))
                )
        ));
        assertThat(papers, hasItem(
                allOf(
                        hasProperty("title", is("c++")),
                        hasProperty("citation", is(3)),
                        hasProperty("reference", is(0)),
                        hasProperty("inspec_controlled", hasSize(3))
                )
        ));
    }

    @Test
    public void badPdfLink() {
        List<Paper> papers = openCSV.convert(badPDFLink);
        assertTrue(papers.isEmpty());
    }

    @Test
    public void noAuthor() {
        List<Paper> papers = openCSV.convert(noAuthor);
        assertTrue(papers.isEmpty());
    }

}