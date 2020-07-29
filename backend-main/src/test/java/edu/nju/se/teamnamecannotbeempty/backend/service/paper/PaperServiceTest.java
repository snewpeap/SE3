package edu.nju.se.teamnamecannotbeempty.backend.service.paper;

import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.PaperMsg;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchService;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SortMode;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.paper.PaperServiceImpl;
import edu.nju.se.teamnamecannotbeempty.backend.vo.*;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.*", "io.*", "org.*", "ch.*", "javax.*"})
@PrepareForTest({AppContextProvider.class, PageRequest.class})
@SpringBootTest
@ActiveProfiles("test")
public class PaperServiceTest {

    @Mock
    private PaperDao paperDao;
    @Mock
    private PaperMsg paperMsg;
    @Mock
    private SearchService searchService;
    @InjectMocks
    private PaperServiceImpl paperService;

    private Page paperPage;
    private Optional<Paper> optionalPaper;

    @Before
    public void setup() {
        SearchMode searchMode = mock(SearchMode.class);
        SortMode sortMode = mock(SortMode.class);
        paperPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        PowerMockito.mockStatic(AppContextProvider.class);
        PowerMockito.mockStatic(PageRequest.class);

        PowerMockito.when(AppContextProvider.getBean(anyString())).thenReturn(searchMode).thenReturn(sortMode);

        when(searchService.search(anyString(), any(), any(), any())).thenReturn(paperPage);
        when(paperMsg.getMismatchId()).thenReturn("找不到ID所对应的论文");


    }

    @Test
    public void testSearch() {

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
        Affiliation affiliation1 = new Affiliation(); affiliation1.setName("NanJingDaXue"); affiliation1.setId(1L);
        Affiliation affiliation2 = new Affiliation(); affiliation2.setName("BeiJingDaXue"); affiliation2.setId(2L);
        Affiliation affiliation3 = new Affiliation(); affiliation3.setName("TianJinDaXue"); affiliation3.setId(3L);
        Author author1 = new Author(); author1.setName("YangYang"); author1.setId(1L);
        Author author2 = new Author(); author2.setName("RiRi"); author2.setId(2L);
        Author author3 = new Author(); author3.setName("DongDong"); author3.setId(3L);

        when(author_affiliation1.getAffiliation()).thenReturn(affiliation1);
        when(author_affiliation2.getAffiliation()).thenReturn(affiliation2);
        when(author_affiliation3.getAffiliation()).thenReturn(affiliation3);

        when(author_affiliation1.getAuthor()).thenReturn(author1);
        when(author_affiliation2.getAuthor()).thenReturn(author2);
        when(author_affiliation3.getAuthor()).thenReturn(author3);
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
        when(paper1.getConference().getId()).thenReturn(1L);
        when(paper2.getConference().getId()).thenReturn(2L);
        when(paper3.getConference().getId()).thenReturn(3L);
        when(paper1.getConference().getYear_highlight()).thenReturn("2121");
        when(paper2.getConference().getYear_highlight()).thenReturn("2121");
        when(paper3.getConference().getYear_highlight()).thenReturn("2121");

        when(paperPage.getContent()).thenReturn(Arrays.asList(paper1, paper2, paper3));

        Author_SimpleAffiliationVO author_simpleAffiliationVO1 = new Author_SimpleAffiliationVO("YangYang",1L, "NanJingDaXue",1L);
        Author_SimpleAffiliationVO author_simpleAffiliationVO2 = new Author_SimpleAffiliationVO("RiRi",2L, "BeiJingDaXue",2L);
        Author_SimpleAffiliationVO author_simpleAffiliationVO3 = new Author_SimpleAffiliationVO("DongDong",3L, "TianJinDaXue",3L);
        List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS1 = Arrays.asList(author_simpleAffiliationVO1, author_simpleAffiliationVO2, author_simpleAffiliationVO3);
        List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS2 = Arrays.asList(author_simpleAffiliationVO1, author_simpleAffiliationVO3);
        List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS3 = Arrays.asList(author_simpleAffiliationVO2, author_simpleAffiliationVO3);
        List<String> key1 = Arrays.asList("Soft", "Hard");
        List<String> key2 = Collections.singletonList("Soft");


        SimplePaperVO simplePaperVO1 = new SimplePaperVO(paper1.getId(), paper1.getTitle(), author_simpleAffiliationVOS1, paper1.getConference().getName(),paper1.getConference().getId(), "2121", key1);
        SimplePaperVO simplePaperVO2 = new SimplePaperVO(paper2.getId(), paper2.getTitle(), author_simpleAffiliationVOS2, paper2.getConference().getName(),paper2.getConference().getId(), "2121", key2);
        SimplePaperVO simplePaperVO3 = new SimplePaperVO(paper3.getId(), paper3.getTitle(), author_simpleAffiliationVOS3, paper3.getConference().getName(),paper3.getConference().getId(), "2121", new ArrayList<>());
        List<SimplePaperVO> simplePaperVOList = Arrays.asList(simplePaperVO1, simplePaperVO2, simplePaperVO3);
        List<SimplePaperVO> result = paperService.search("1", "1", 1, "!", 1);
        Assert.assertEquals(simplePaperVOList, result);

    }

    @Test
    public void testGetPaperSuccess() {
        Author author1 = getAuthor((long) 0, "ZhenZhen");
        Author author2 = getAuthor((long) 1, "PiaoLiang");
        Affiliation affiliation1 = getAffiliation((long) 0, "NJU", "NanJing");
        Affiliation affiliation2 = getAffiliation((long) 1, "ZhengXingYiYuan", "TianTangDao");
        Author_Affiliation aa1 = getAuthor_Affiliation(author1, affiliation1);
        Author_Affiliation aa2 = getAuthor_Affiliation(author2, affiliation2);
        Term term1 = getTerm((long) 0, "Beauty");
        Term term2 = getTerm((long) 1, "Handsome");
        List<Term> termList = Arrays.asList(term1, term2);

        Paper paper = new Paper();
        paper.setId((long) 0);
        paper.setTitle("Do You Want To Be More Beautiful?");
        paper.setAa(Arrays.asList(aa1, aa2));
        paper.setConference(getConference());
        Date date = new Date();
        paper.setDate_added_Xplore(date);
        paper.setVolume(33);
        paper.setStart_page(12);
        paper.setEnd_page(23);
        paper.setSummary("I do not want to write test case!");
        paper.setIssn("1001");
        paper.setIsbn("1001");
        paper.setDoi("1001");
        paper.setFunding_info("12138");
        paper.setPdf_link(null);
        paper.setAuthor_keywords(termList);
        paper.setMesh_terms(termList);
        paper.setCitation(1);
        paper.setReference(100);
        optionalPaper = Optional.of(paper);

        when(paperDao.findById(anyLong())).thenReturn(optionalPaper);

        List<String> keywords = Arrays.asList("Beauty", "Handsome");
        Author_AffiliationVO author_affiliationVO1 = new Author_AffiliationVO("ZhenZhen",0L, new AffiliationVO("NJU", "NanJing",0L));
        Author_AffiliationVO author_affiliationVO2 = new Author_AffiliationVO("PiaoLiang", 1L,new AffiliationVO("ZhengXingYiYuan", "TianTangDao", 1L));
        PaperVO paperVO = new PaperVO((long) 0, "Do You Want To Be More Beautiful?", Arrays.asList(author_affiliationVO1, author_affiliationVO2),
                "GKD", 0L,2121, "99th", 12,
                23, "I do not want to write test case!", "1001", null, keywords, new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), 1, 100, null, null);
        ResponseVO responseVO = paperService.getPaper((long) 0);
        Assert.assertTrue(responseVO.isSuccess());
        Assert.assertEquals(paperVO, responseVO.getContent());

    }

    @Test
    public void testGetPaperFail() {
        optionalPaper = Optional.empty();
        when(paperDao.findById(anyLong())).thenReturn(optionalPaper);
        ResponseVO responseVO = paperService.getPaper((long) 0);
        Assert.assertFalse(responseVO.isSuccess());
        Assert.assertEquals("找不到ID所对应的论文", responseVO.getMessage());
    }

    private Author_Affiliation getAuthor_Affiliation(Author author, Affiliation affiliation) {
        Author_Affiliation author_affiliation = new Author_Affiliation();
        author_affiliation.setAuthor(author);
        author_affiliation.setAffiliation(affiliation);
        return author_affiliation;
    }

    private Author getAuthor(long id, String name) {
        Author author = new Author();
        author.setId(id);
        author.setName(name);
        return author;
    }

    private Affiliation getAffiliation(long id, String name, String country) {
        Affiliation affiliation = new Affiliation();
        affiliation.setId(id);
        affiliation.setName(name);
        affiliation.setCountry(country);
        return affiliation;
    }

    private Conference getConference() {
        Conference conference = new Conference();
        conference.setId((long) 0);
        conference.setName("GKD");
        conference.setYear(2121);
        conference.setOrdno(99);
        return conference;
    }

    private Term getTerm(long id, String content) {
        Term term = new Term();
        term.setId(id);
        term.setContent(content);
        return term;
    }
}
