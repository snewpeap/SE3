package edu.nju.se.teamnamecannotbeempty.backend.service.rank;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.RankMsg;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.rank.RankFecth;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.rank.RankServiceImpl;
import edu.nju.se.teamnamecannotbeempty.backend.vo.RankItem;
import edu.nju.se.teamnamecannotbeempty.backend.vo.RankVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RankServiceTest {

    @Mock
    private PaperDao paperDao;
    @Mock
    private RankMsg rankMsg;
    @Mock
    private RankFecth rankFecth;
    @InjectMocks
    private RankServiceImpl rankService;


    @Before
    public void setup() {
        when(rankMsg.getMismatchPageNumber()).thenReturn("查询的页号与结果数量不匹配");
        when(rankMsg.getEachNum()).thenReturn(10);

        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);
        Author author3 = mock(Author.class);
        Author author4 = mock(Author.class);

        Affiliation affiliation1 = mock(Affiliation.class);
        Affiliation affiliation2 = mock(Affiliation.class);
        Affiliation affiliation3 = mock(Affiliation.class);

        Author_Affiliation aa1 = new Author_Affiliation();
        aa1.setAuthor(author1);
        aa1.setAffiliation(affiliation1);
        Author_Affiliation aa2 = new Author_Affiliation();
        aa2.setAuthor(author2);
        aa2.setAffiliation(affiliation2);
        Author_Affiliation aa3 = new Author_Affiliation();
        aa3.setAuthor(author3);
        aa3.setAffiliation(affiliation3);
        Author_Affiliation aa4 = new Author_Affiliation();
        aa4.setAuthor(author4);
        aa4.setAffiliation(affiliation1);
        List<Author_Affiliation> author_affiliationList1 = Arrays.asList(aa1, aa4);
        List<Author_Affiliation> author_affiliationList2 = Arrays.asList(aa2, aa3, aa4);
        List<Author_Affiliation> author_affiliationList3 = Arrays.asList(aa3, aa4);
        List<Author_Affiliation> author_affiliationList4 = Arrays.asList(aa2, aa3, aa4);

        Conference conference1 = new Conference();
        conference1.setYear(2016);
        conference1.setName("MaLanShan");
        conference1.setOrdno(3);
        Conference conference2 = new Conference();
        conference2.setYear(2020);
        conference2.setName("MaLanShan");
        conference2.setOrdno(7);

        Term term1 = new Term();
        term1.setContent("Lai");
        Term term2 = new Term();
        term2.setContent("Bao");
        Term term3 = new Term();
        term3.setContent("Guang");
        Term term4 = new Term();
        term4.setContent("666");
        Term term5 = new Term();
        term5.setContent("888");
        Term term6 = new Term();
        term6.setContent("999");

        Paper paper1 = mock(Paper.class);

        Paper paper2 = mock(Paper.class);


        Paper paper3 = mock(Paper.class);


        Paper paper4 = mock(Paper.class);


        List<Paper> paperList = Arrays.asList(paper3, paper2, paper1, paper4);
    }

    @Test
    public void paperCitedTest_SuccessDescend() {
        RankItem rankItem1 = new RankItem("Title1", 10);
        RankItem rankItem2 = new RankItem("Title2", 8);
        RankItem rankItem3 = new RankItem("Title3", 7);
        RankItem rankItem4 = new RankItem("Title4", 1);
        List<RankItem> rankItemList = Arrays.asList(rankItem1, rankItem2, rankItem3, rankItem4);
        RankVO rankVO = new RankVO(1, rankItemList);
        when(rankFecth.getAllResult("Paper-Cited",2222,2223)).
                thenReturn(Arrays.asList(rankItem4, rankItem3, rankItem2, rankItem1));
        ResponseVO result = rankService.getRank("Paper-Cited", 1, true, 2222, 2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO, result.getContent());
    }

    @Test
    public void paperCitedTest_SuccessAscend() {
        RankItem rankItem1 = new RankItem("Title1", 10);
        RankItem rankItem2 = new RankItem("Title2", 8);
        RankItem rankItem3 = new RankItem("Title3", 7);
        RankItem rankItem4 = new RankItem("Title4", 1);
        List<RankItem> rankItemList = Arrays.asList(rankItem4, rankItem3, rankItem2, rankItem1);
        RankVO rankVO = new RankVO(1, rankItemList);
        when(rankFecth.getAllResult("Paper-Cited",2222,2223)).
                thenReturn(rankItemList);
        ResponseVO result = rankService.getRank("Paper-Cited", 1, false, 2222, 2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO, result.getContent());
    }

    @Test
    public void paperCitedTest_False() {
        ResponseVO result = rankService.getRank("Paper-Cited", 2, false, 2222, 2223);
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(rankMsg.getMismatchPageNumber(), result.getMessage());
    }

    @Test
    public void authorCitedTest_SuccessDescend() {
        RankItem rankItem1 = new RankItem("YueYue", 26);
        RankItem rankItem2 = new RankItem("DongDong", 16);
        RankItem rankItem3 = new RankItem("YangYang", 10);
        RankItem rankItem4 = new RankItem("RiRi", 9);
        List<RankItem> rankItemList = Arrays.asList(rankItem1, rankItem2, rankItem3, rankItem4);
        RankVO rankVO = new RankVO(1, rankItemList);
        when(rankFecth.getAllResult("Author-Cited",2222,2223)).
                thenReturn(Arrays.asList(rankItem4, rankItem3, rankItem2, rankItem1));
        ResponseVO result = rankService.getRank("Author-Cited", 1, true, 2222, 2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO, result.getContent());
    }

    @Test
    public void authorCitedTest_SuccessAscend() {
        RankItem rankItem1 = new RankItem("YueYue", 26);
        RankItem rankItem2 = new RankItem("DongDong", 16);
        RankItem rankItem3 = new RankItem("YangYang", 10);
        RankItem rankItem4 = new RankItem("RiRi", 9);
        List<RankItem> rankItemList = Arrays.asList(rankItem4, rankItem3, rankItem2, rankItem1);
        RankVO rankVO = new RankVO(1, rankItemList);
        when(rankFecth.getAllResult("Author-Cited",2222,2223)).
                thenReturn(Arrays.asList(rankItem4, rankItem3, rankItem2, rankItem1));
        ResponseVO result = rankService.getRank("Author-Cited", 1, false, 2222, 2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO, result.getContent());
    }

    @Test
    public void authorPaperTest_SuccessDescend() {
        RankItem rankItem1 = new RankItem("YueYue", 4);
        RankItem rankItem2 = new RankItem("DongDong", 3);
        RankItem rankItem3 = new RankItem("RiRi", 2);
        RankItem rankItem4 = new RankItem("YangYang", 1);
        List<RankItem> rankItemList = Arrays.asList(rankItem1, rankItem2, rankItem3, rankItem4);
        RankVO rankVO = new RankVO(1, rankItemList);
        when(rankFecth.getAllResult("Author-Paper",2222,2223)).
                thenReturn(Arrays.asList(rankItem4, rankItem3, rankItem2, rankItem1));
        ResponseVO result = rankService.getRank("Author-Paper", 1, true, 2222, 2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO, result.getContent());
    }

    @Test
    public void affiliationPaperTest_SuccessDescend() {
        RankItem rankItem1 = new RankItem("NanJingDaXue", 4);
        RankItem rankItem2 = new RankItem("TianJinDaXue", 3);
        RankItem rankItem3 = new RankItem("BeiJingDaXue", 2);
        List<RankItem> rankItemList = Arrays.asList(rankItem1, rankItem2, rankItem3);
        RankVO rankVO = new RankVO(1, rankItemList);
        when(rankFecth.getAllResult("Affiliation-Paper",2222,2223)).
                thenReturn(Arrays.asList(rankItem3, rankItem2, rankItem1));
        ResponseVO result = rankService.getRank("Affiliation-Paper", 1, true, 2222, 2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO, result.getContent());
    }

    @Test
    public void publicationPaperTest_SuccessDescend() {
        RankItem rankItem1 = new RankItem("2020 7 MaLanShan", 3);
        RankItem rankItem2 = new RankItem("2016 3 MaLanShan", 1);
        List<RankItem> rankItemList = Arrays.asList(rankItem1, rankItem2);
        RankVO rankVO = new RankVO(1, rankItemList);
        when(rankFecth.getAllResult("Publication-Paper",2222,2223)).
                thenReturn(Arrays.asList(rankItem2, rankItem1));
        ResponseVO result = rankService.getRank("Publication-Paper", 1, true, 2222, 2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO, result.getContent());
    }

    @Test
    public void keywordPaperTest_SuccessDescend() {
        RankItem rankItem1 = new RankItem("Bao", 7);
        RankItem rankItem2 = new RankItem("Guang", 5);
        RankItem rankItem3 = new RankItem("888", 4);
        RankItem rankItem4 = new RankItem("Lai", 3);
        RankItem rankItem5 = new RankItem("999", 2);
        RankItem rankItem6 = new RankItem("666", 1);
        List<RankItem> rankItemList = Arrays.asList(rankItem1, rankItem2, rankItem3, rankItem4, rankItem5, rankItem6);
        RankVO rankVO = new RankVO(1, rankItemList);
        when(rankFecth.getAllResult("Keyword-Paper",2222,2223)).
                thenReturn(Arrays.asList(rankItem6, rankItem5,rankItem4, rankItem3, rankItem2, rankItem1));
        ResponseVO result = rankService.getRank("Keyword-Paper", 1, true, 2222, 2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO, result.getContent());
    }


}
