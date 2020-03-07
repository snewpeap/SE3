package edu.nju.se.teamnamecannotbeempty.backend.data;

import edu.nju.se.teamnamecannotbeempty.backend.po.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create",
        "javax.persistence.schema-generation.scripts.action=none"
},
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "edu.nju.se.teamnamecannotbeempty.backend.data.*"
                ),
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider"
                )
        })
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class FromCSVOpenCSVImplTest {
    @Autowired
    private FromCSVOpenCSVImpl openCSV;

    private InputStream ok, badPDFLink, noAuthor;

    private Author a1, a2, a3;
    private Affiliation nju, google;
    private Conference ase2013;

    @Before
    public void setUp() throws Exception {
        ok = getClass().getResourceAsStream("/ok.csv");
        badPDFLink = getClass().getResourceAsStream("/badPDFLink.csv");
        noAuthor = getClass().getResourceAsStream("/noAuthor.csv");
        a1 = new Author();
        a1.setName("a");
        a2 = new Author();
        a2.setName("b");
        a3 = new Author();
        a3.setName("c");
        nju = new Affiliation();
        nju.setName("nju");
        google = new Affiliation();
        google.setName("google");
        ase2013 = new Conference();
        ase2013.setName("ASE");
        ase2013.setYear(2013);
        ase2013.setOrdno(28);
    }

    @Test
    public void convert() throws MalformedURLException {
        List<Paper> papers = openCSV.convert(ok);
        assertNotNull(papers);
        assertEquals(3, papers.size());
        Paper paper1 = getPaper1();
//        assertThat(papers, hasItem(paper1));
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

    private Paper getPaper1() throws MalformedURLException {
        Paper paper = new Paper();
        paper.setTitle("data mining");
        paper.setAa(new ArrayList<Author_Affiliation>() {{
            add(new Author_Affiliation() {{
                setAuthor(a1);
                setAffiliation(nju);
            }});
            add(new Author_Affiliation() {{
                setAuthor(a2);
                setAffiliation(nju);
            }});
            add(new Author_Affiliation() {{
                setAuthor(a3);
                setAffiliation(google);
            }});
        }});
        paper.setConference(ase2013);
        paper.setStart_page(1);
        paper.setEnd_page(2);
        paper.setSummary("datadatadata");
        paper.setPdf_link(new URL("http://www.baidu.com"));
        paper.setAuthor_keywords(new ArrayList<Term>() {{
            add(new Term() {{
                setContent("a");
            }});
            add(new Term() {{
                setContent("b");
            }});
            add(new Term() {{
                setContent("c");
            }});
        }});
        paper.setCitation(1);
        paper.setReference(1);
        return paper;
    }
}