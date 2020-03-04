package edu.nju.se.teamnamecannotbeempty.backend.service.rank;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.RankMsg;
import edu.nju.se.teamnamecannotbeempty.backend.dao.PaperDao;
import edu.nju.se.teamnamecannotbeempty.backend.po.*;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.rank.RankServiceImpl;
import edu.nju.se.teamnamecannotbeempty.backend.vo.RankItem;
import edu.nju.se.teamnamecannotbeempty.backend.vo.RankVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RankServiceTest {

    @MockBean
    private PaperDao paperDao;
    @MockBean
    private RankMsg rankMsg;
    @Autowired
    private RankServiceImpl rankService;


    @Before
    public void setup(){
        when(rankMsg.getMismatchPageNumber()).thenReturn("查询的页号与结果数量不匹配");
        when(rankMsg.getEachNum()).thenReturn(10);

        Author author1 = mock(Author.class);
        when(author1.getName()).thenReturn("YangYang");
        Author author2 = mock(Author.class);
        when(author2.getName()).thenReturn("RiRi");
        Author author3 = mock(Author.class);
        when(author3.getName()).thenReturn("DongDong");
        Author author4 = mock(Author.class);
        when(author4.getName()).thenReturn("YueYue");

        Affiliation affiliation1 = mock(Affiliation.class);
        when(affiliation1.getName()).thenReturn("NanJingDaXue");
        Affiliation affiliation2 = mock(Affiliation.class);
        when(affiliation2.getName()).thenReturn("BeiJingDaXue");
        Affiliation affiliation3 = mock(Affiliation.class);
        when(affiliation3.getName()).thenReturn("TianJinDaXue");

        Author_Affiliation aa1 = new Author_Affiliation(); aa1.setAuthor(author1); aa1.setAffiliation(affiliation1);
        Author_Affiliation aa2 = new Author_Affiliation(); aa2.setAuthor(author2); aa2.setAffiliation(affiliation2);
        Author_Affiliation aa3 = new Author_Affiliation(); aa3.setAuthor(author3); aa3.setAffiliation(affiliation3);
        Author_Affiliation aa4 = new Author_Affiliation(); aa4.setAuthor(author4); aa4.setAffiliation(affiliation1);
        List<Author_Affiliation> author_affiliationList1 = Arrays.asList(aa1,aa4);
        List<Author_Affiliation> author_affiliationList2 = Arrays.asList(aa2,aa3,aa4);
        List<Author_Affiliation> author_affiliationList3 = Arrays.asList(aa3,aa4);
        List<Author_Affiliation> author_affiliationList4 = Arrays.asList(aa2,aa3,aa4);

        Conference conference1 = new Conference(); conference1.setYear(2016); conference1.setName("MaLanShan"); conference1.setOrdno(3);
        Conference conference2 = new Conference(); conference2.setYear(2020); conference2.setName("MaLanShan"); conference2.setOrdno(7);

        Term term1 = new Term(); term1.setContent("Lai");  Term term2 = new Term(); term2.setContent("Bao"); Term term3 = new Term(); term3.setContent("Guang");
        Term term4 = new Term(); term4.setContent("666");  Term term5 = new Term(); term5.setContent("888"); Term term6 = new Term(); term6.setContent("999");

        Paper paper1 = mock(Paper.class);
        when(paper1.getTitle()).thenReturn("Title1");
        when(paper1.getCitation()).thenReturn(10);
        when(paper1.getAa()).thenReturn(author_affiliationList1);
        when(paper1.getConference()).thenReturn(conference1);
        when(paper1.getAuthor_keywords()).thenReturn(Arrays.asList(term1,term2,term3));
        when(paper1.getIeee_terms()).thenReturn(Arrays.asList(term4,term5,term6));
        when(paper1.getInspec_controlled()).thenReturn(Arrays.asList(term2,term3));
        when(paper1.getInspec_non_controlled()).thenReturn(Collections.singletonList(term5));
        when(paper1.getMesh_terms()).thenReturn(Collections.singletonList(term2));

        Paper paper2 = mock(Paper.class);
        when(paper2.getTitle()).thenReturn("Title2");
        when(paper2.getCitation()).thenReturn(8);
        when(paper2.getAa()).thenReturn(author_affiliationList2);
        when(paper2.getConference()).thenReturn(conference2);
        when(paper2.getAuthor_keywords()).thenReturn(Arrays.asList(term2,term3));
        when(paper2.getIeee_terms()).thenReturn(Arrays.asList(term1,term6,term3));
        when(paper2.getInspec_controlled()).thenReturn(new ArrayList<>());
        when(paper2.getInspec_non_controlled()).thenReturn(new ArrayList<>());
        when(paper2.getMesh_terms()).thenReturn(Collections.singletonList(term2));

        Paper paper3 = mock(Paper.class);
        when(paper3.getTitle()).thenReturn("Title3");
        when(paper3.getCitation()).thenReturn(7);
        when(paper3.getAa()).thenReturn(author_affiliationList3);
        when(paper3.getConference()).thenReturn(conference2);
        when(paper3.getAuthor_keywords()).thenReturn(new ArrayList<>());
        when(paper3.getIeee_terms()).thenReturn(Arrays.asList(term2,term5));
        when(paper3.getInspec_controlled()).thenReturn(new ArrayList<>());
        when(paper3.getInspec_non_controlled()).thenReturn(new ArrayList<>());
        when(paper3.getMesh_terms()).thenReturn(new ArrayList<>());

        Paper paper4 = mock(Paper.class);
        when(paper4.getTitle()).thenReturn("Title4");
        when(paper4.getCitation()).thenReturn(1);
        when(paper4.getAa()).thenReturn(author_affiliationList4);
        when(paper4.getConference()).thenReturn(conference2);
        when(paper4.getAuthor_keywords()).thenReturn(new ArrayList<>());
        when(paper4.getIeee_terms()).thenReturn(Arrays.asList(term1,term5));
        when(paper4.getInspec_controlled()).thenReturn(new ArrayList<>());
        when(paper4.getInspec_non_controlled()).thenReturn(Arrays.asList(term2,term3));
        when(paper4.getMesh_terms()).thenReturn(new ArrayList<>());

        List<Paper> paperList = Arrays.asList(paper3,paper2,paper1,paper4);
        when(paperDao.findAllByConference_YearBetween(anyInt(),anyInt())).thenReturn(paperList);
    }

    @Test
    public void paperCitedTest_SuccessDescend(){
        RankItem rankItem1 = new RankItem("Title1",10);
        RankItem rankItem2 = new RankItem("Title2",8);
        RankItem rankItem3 = new RankItem("Title3",7);
        RankItem rankItem4 = new RankItem("Title4",1);
        List<RankItem> rankItemList = Arrays.asList(rankItem1,rankItem2,rankItem3,rankItem4);
        RankVO rankVO = new RankVO(1,rankItemList);
        ResponseVO result = rankService.getRank("Paper-Cited",1,true,2222,2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO,result.getContent());
    }

    @Test
    public void paperCitedTest_SuccessAscend(){
        RankItem rankItem1 = new RankItem("Title1",10);
        RankItem rankItem2 = new RankItem("Title2",8);
        RankItem rankItem3 = new RankItem("Title3",7);
        RankItem rankItem4 = new RankItem("Title4",1);
        List<RankItem> rankItemList = Arrays.asList(rankItem4,rankItem3,rankItem2,rankItem1);
        RankVO rankVO = new RankVO(1,rankItemList);
        ResponseVO result = rankService.getRank("Paper-Cited",1,false,2222,2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO,result.getContent());
    }

    @Test
    public void paperCitedTest_False(){
        ResponseVO result = rankService.getRank("Paper-Cited",2,false,2222,2223);
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(rankMsg.getMismatchPageNumber(),result.getMessage());

    }

    @Test
    public void authorCitedTest_SuccessDescend(){
        RankItem rankItem1 = new RankItem("YueYue",26);
        RankItem rankItem2 = new RankItem("DongDong",16);
        RankItem rankItem3 = new RankItem("YangYang",10);
        RankItem rankItem4 = new RankItem("RiRi",9);
        List<RankItem> rankItemList = Arrays.asList(rankItem1,rankItem2,rankItem3,rankItem4);
        RankVO rankVO = new RankVO(1,rankItemList);
        ResponseVO result = rankService.getRank("Author-Cited",1,true,2222,2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO,result.getContent());
    }

    @Test
    public void authorCitedTest_SuccessAscend(){
        RankItem rankItem1 = new RankItem("YueYue",26);
        RankItem rankItem2 = new RankItem("DongDong",16);
        RankItem rankItem3 = new RankItem("YangYang",10);
        RankItem rankItem4 = new RankItem("RiRi",9);
        List<RankItem> rankItemList = Arrays.asList(rankItem4,rankItem3,rankItem2,rankItem1);
        RankVO rankVO = new RankVO(1,rankItemList);
        ResponseVO result = rankService.getRank("Author-Cited",1,false,2222,2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO,result.getContent());
    }

    @Test
    public void authorPaperTest_SuccessDescend(){
        RankItem rankItem1 = new RankItem("YueYue",4);
        RankItem rankItem2 = new RankItem("DongDong",3);
        RankItem rankItem3 = new RankItem("RiRi",2);
        RankItem rankItem4 = new RankItem("YangYang",1);
        List<RankItem> rankItemList = Arrays.asList(rankItem1,rankItem2,rankItem3,rankItem4);
        RankVO rankVO = new RankVO(1,rankItemList);
        ResponseVO result = rankService.getRank("Author-Paper",1,true,2222,2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO,result.getContent());
    }

    @Test
    public void affiliationPaperTest_SuccessDescend(){
        RankItem rankItem1 = new RankItem("NanJingDaXue",4);
        RankItem rankItem2 = new RankItem("TianJinDaXue",3);
        RankItem rankItem3 = new RankItem("BeiJingDaXue",2);
        List<RankItem> rankItemList = Arrays.asList(rankItem1,rankItem2,rankItem3);
        RankVO rankVO = new RankVO(1,rankItemList);
        ResponseVO result = rankService.getRank("Affiliation-Paper",1,true,2222,2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO,result.getContent());
    }

    @Test
    public void publicationPaperTest_SuccessDescend(){
        RankItem rankItem1 = new RankItem("2020 7 MaLanShan",3);
        RankItem rankItem2 = new RankItem("2016 3 MaLanShan",1);
        List<RankItem> rankItemList = Arrays.asList(rankItem1,rankItem2);
        RankVO rankVO = new RankVO(1,rankItemList);
        ResponseVO result = rankService.getRank("Publication-Paper",1,true,2222,2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO,result.getContent());
    }

    @Test
    public void keywordPaperTest_SuccessDescend(){
        RankItem rankItem1 = new RankItem("Bao",7);
        RankItem rankItem2 = new RankItem("Guang",5);
        RankItem rankItem3 = new RankItem("888",4);
        RankItem rankItem4 = new RankItem("Lai",3);
        RankItem rankItem5 = new RankItem("999",2);
        RankItem rankItem6 = new RankItem("666",1);
        List<RankItem> rankItemList = Arrays.asList(rankItem1,rankItem2,rankItem3,rankItem4,rankItem5,rankItem6);
        RankVO rankVO = new RankVO(1,rankItemList);
        ResponseVO result = rankService.getRank("Keyword-Paper",1,true,2222,2223);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(rankVO,result.getContent());
    }


}
