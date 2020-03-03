package edu.nju.se.teamnamecannotbeempty.backend.service.paper;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.PaperMsg;
import edu.nju.se.teamnamecannotbeempty.backend.dao.PaperDao;
import edu.nju.se.teamnamecannotbeempty.backend.po.*;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchService;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SortMode;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.paper.ApplicationContextUtil;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.paper.PaperServiceImpl;
import edu.nju.se.teamnamecannotbeempty.backend.vo.Author_SimpleAffiliationVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PowerMockIgnore({ "com.*", "io.*", "org.*", "ch.*", "javax.validation.*" })
@PrepareForTest({ApplicationContextUtil.class, PageRequest.class})
@SpringBootTest
public class PaperServiceTest {

    @MockBean
    private PaperDao paperDao;
    @MockBean
    private PaperMsg paperMsg;
    @MockBean
    private SearchService searchService;
    @Autowired
    private PaperServiceImpl paperService;

    private Page paperPage;

    @Before
    public void setup(){
        SearchMode searchMode = mock(SearchMode.class);
        SortMode sortMode = mock(SortMode.class);
        paperPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        PowerMockito.mockStatic(ApplicationContextUtil.class);
        PowerMockito.mockStatic(PageRequest.class);

        PowerMockito.when(ApplicationContextUtil.getBean(anyString())).thenReturn(searchMode).thenReturn(sortMode);

        when(searchService.search(anyString(),any(),any(),any())).thenReturn(paperPage);


    }

    @Test
    public void testSearch(){

        Paper paper1 = mock(Paper.class);
        Paper paper2 = mock(Paper.class);
        Paper paper3 = mock(Paper.class);
        Conference conference1 = mock(Conference.class);
        Conference conference2 = mock(Conference.class);
        Conference conference3 = mock(Conference.class);
        Author_Affiliation author_affiliation1 = mock(Author_Affiliation.class);
        Author_Affiliation author_affiliation2 = mock(Author_Affiliation.class);
        Author_Affiliation author_affiliation3 = mock(Author_Affiliation.class);
        Term term1 = mock(Term.class);
        Term term2 = mock(Term.class);
        Affiliation affiliation1 = mock(Affiliation.class);
        Affiliation affiliation2 = mock(Affiliation.class);
        Affiliation affiliation3 = mock(Affiliation.class);
        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);
        Author author3 = mock(Author.class);

        when(author_affiliation1.getAffiliation()).thenReturn(affiliation1);
        when(author_affiliation2.getAffiliation()).thenReturn(affiliation2);
        when(author_affiliation3.getAffiliation()).thenReturn(affiliation3);
        when(author_affiliation1.getAuthor()).thenReturn(author1);
        when(author_affiliation2.getAuthor()).thenReturn(author2);
        when(author_affiliation3.getAuthor()).thenReturn(author3);
        when(author_affiliation1.getAffiliation().getName()).thenReturn("NanJingDaXue");
        when(author_affiliation2.getAffiliation().getName()).thenReturn("BeiJingDaXue");
        when(author_affiliation3.getAffiliation().getName()).thenReturn("TianJinDaXue");
        when(author_affiliation1.getAuthor().getName()).thenReturn("YangYang");
        when(author_affiliation2.getAuthor().getName()).thenReturn("RiRi");
        when(author_affiliation3.getAuthor().getName()).thenReturn("DongDong");
        when(term1.getContent()).thenReturn("Soft");
        when(term2.getContent()).thenReturn("Hard");

        List<Author_Affiliation> aa1 = Arrays.asList(author_affiliation1, author_affiliation2, author_affiliation3);
        List<Author_Affiliation> aa2 = Arrays.asList(author_affiliation1, author_affiliation3);
        List<Author_Affiliation> aa3 = Arrays.asList(author_affiliation2, author_affiliation3);
        List<Term> terms1 = Arrays.asList(term1, term2);
        List<Term> terms2 = Collections.singletonList(term1);
        when(paper1.getId()).thenReturn((long) 0);
        when(paper2.getId()).thenReturn((long) 1);
        when(paper3.getId()).thenReturn((long) 2);
        when(paper1.getAa()).thenReturn(aa1);
        when(paper2.getAa()).thenReturn(aa2);
        when(paper3.getAa()).thenReturn(aa3);
        when(paper1.getAuthor_keywords()).thenReturn(terms1);
        when(paper2.getAuthor_keywords()).thenReturn(terms2);
        when(paper3.getAuthor_keywords()).thenReturn(new ArrayList<>());
        when(paper1.getTitle()).thenReturn("Hi");
        when(paper2.getTitle()).thenReturn("Hello");
        when(paper3.getTitle()).thenReturn("Haha");
        when(paper1.getConference()).thenReturn(conference1);
        when(paper2.getConference()).thenReturn(conference2);
        when(paper3.getConference()).thenReturn(conference3);
        when(paper1.getConference().getName()).thenReturn("IEEE");
        when(paper2.getConference().getName()).thenReturn("HEEE");
        when(paper3.getConference().getName()).thenReturn("VEEE");

        when(paperPage.getContent()).thenReturn(Arrays.asList(paper1, paper2, paper3));

        Author_SimpleAffiliationVO author_simpleAffiliationVO1 = new Author_SimpleAffiliationVO("YangYang","NanJingDaXue");
        Author_SimpleAffiliationVO author_simpleAffiliationVO2 = new Author_SimpleAffiliationVO("RiRi","BeiJingDaXue");
        Author_SimpleAffiliationVO author_simpleAffiliationVO3 = new Author_SimpleAffiliationVO("DongDong","TianJinDaXue");
        List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS1 = Arrays.asList(author_simpleAffiliationVO1,author_simpleAffiliationVO2,author_simpleAffiliationVO3);
        List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS2 = Arrays.asList(author_simpleAffiliationVO1,author_simpleAffiliationVO3);
        List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS3 = Arrays.asList(author_simpleAffiliationVO2,author_simpleAffiliationVO3);
        List<String> key1 = Arrays.asList("Soft","Hard");
        List<String> key2 = Collections.singletonList("Soft");


        SimplePaperVO simplePaperVO1 = new SimplePaperVO(paper1.getId(), paper1.getTitle(),author_simpleAffiliationVOS1, paper1.getConference().getName(),key1);
        SimplePaperVO simplePaperVO2 = new SimplePaperVO(paper2.getId(), paper2.getTitle(),author_simpleAffiliationVOS2, paper2.getConference().getName(),key2);
        SimplePaperVO simplePaperVO3 = new SimplePaperVO(paper3.getId(), paper3.getTitle(),author_simpleAffiliationVOS3, paper3.getConference().getName(),new ArrayList<>());
        List<SimplePaperVO> simplePaperVOList = Arrays.asList(simplePaperVO1,simplePaperVO2,simplePaperVO3);
        List<SimplePaperVO> result = paperService.search("1","1",1,"!",1);
        Assert.assertEquals(simplePaperVOList,result);

    }
}
