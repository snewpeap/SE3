package edu.nju.se.teamnamecannotbeempty.backend;

import edu.nju.se.teamnamecannotbeempty.backend.dao.PaperDao;
import edu.nju.se.teamnamecannotbeempty.backend.data.FromCSVOpenCSVImpl;
import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InitDataSourceTest {
    InitDataSource initDataSource;

    @Before
    public void setUp() throws Exception {
        initDataSource = new InitDataSource(new FromCSVOpenCSVImpl(), Mockito.mock(PaperDao.class));
    }

    @Test
    public void attachJsonInfo() throws Exception {
        List<Paper> papers = getOKList();
        InputStream json = getClass().getResourceAsStream("/ok.json");
        Method attachJsonInfo = initDataSource.getClass().getDeclaredMethod("attachJsonInfo", List.class, InputStream.class);
        attachJsonInfo.setAccessible(true);
        papers = (List<Paper>) attachJsonInfo.invoke(papers, json);
        Paper p1 = null, p10 = null, p100 = null;
        for (Paper paper : papers) {
            switch (paper.getId().intValue()) {
                case 1:
                    p1 = paper;
                    break;
                case 10:
                    p10 = paper;
                    break;
                case 100:
                    p100 = paper;
            }
        }
        assertNotNull(p1);
        assertNotNull(p10);
        assertNotNull(p100);
        assertEquals(0, p1.getRefs().size());
        assertEquals(2, p10.getRefs().size());
        assertThat(p10, hasProperty("refs", hasItem(
                allOf(
                        hasProperty("referee", nullValue()),
                        hasProperty("refTitle", is("c++"))
                )
        )));
        assertThat(p10, hasProperty("refs", hasItem(hasProperty("referee", is(p1)))));
        assertThat(p100, hasProperty("refs", allOf(
                hasItem(hasProperty("referee", is(p1))),
                hasItem(hasProperty("referee", is(p10)))
        )));
        json.close();
    }

    private List<Paper> getOKList() {
        return new ArrayList<Paper>() {{
            Paper a = new Paper();
            a.setId(1L);
            a.setTitle("head");
            Paper b = new Paper();
            b.setId(10L);
            b.setTitle("first");
            Paper c = new Paper();
            c.setId(100L);
            c.setTitle("java");
            add(a);
            add(b);
            add(c);
        }};
    }
}