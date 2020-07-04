package edu.nju.se.teamnamecannotbeempty.backend.service.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization.AcademicEntityFetch;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization.FetchForCache;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization.SignificantPaperFetch;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SignificantPaperFetchTest {
    @Mock
    private PaperDao paperDao;
    @Mock
    private EntityMsg entityMsg;
    @Mock
    private FetchForCache fetchForCache;
    @Mock
    private AcademicEntityFetch academicEntityFetch;
    @InjectMocks
    private SignificantPaperFetch significantPaperFetch;

    @Before
    public void setup(){
        when(entityMsg.getAuthorType()).thenReturn(1);
        when(entityMsg.getAffiliationType()).thenReturn(2);
        when(entityMsg.getConferenceType()).thenReturn(3);
        when(academicEntityFetch.getAllAliasIdsOfAffi(1,new ArrayList<>())).
                thenReturn(Collections.singletonList(1L));
        when(academicEntityFetch.getAllAliasIdsOfAuthor(1, new ArrayList<>())).
                thenReturn(Collections.singletonList(1L));
        Paper paper1=new Paper(); paper1.setYear(1); paper1.setId(1L);
        Paper paper11=new Paper(); paper11.setYear(1); paper11.setId(11L);
        Paper paper2=new Paper(); paper2.setYear(2); paper2.setId(2L);
        Paper paper22=new Paper(); paper22.setYear(2); paper22.setId(22L);
        when(fetchForCache.getAllPapersByAuthor(1L)).thenReturn(Arrays.asList(paper1,paper2,paper11,paper22));
        when(fetchForCache.getAllPapersByAffi(1L)).thenReturn(Arrays.asList(paper1,paper2,paper11,paper22));
        when(fetchForCache.getAllPapersByConference(1L)).thenReturn(Arrays.asList(paper1,paper2,paper11,paper22));
        Term term1 = new Term(); term1.setId(1L);
        Term.Popularity tPop1 = new Term.Popularity(); tPop1.setTerm(term1);
        Term term2 = new Term(); term2.setId(2L);
        Term.Popularity tPop2 = new Term.Popularity(); tPop2.setTerm(term2);
        when(fetchForCache.getTermPopByPaperID(1L)).thenReturn(Collections.singletonList(tPop1));
        when(fetchForCache.getTermPopByPaperID(11L)).thenReturn(Collections.singletonList(tPop1));
        when(fetchForCache.getTermPopByPaperID(2L)).thenReturn(Collections.singletonList(tPop2));
        when(fetchForCache.getTermPopByPaperID(22L)).thenReturn(Collections.singletonList(tPop2));

    }

    @Test
    public void authorPapersTest(){
        List<SimplePaperVO> ans = significantPaperFetch.getSignificantPaper(1,1,-1,-1);
        Assert.assertEquals(4,ans.size());
    }

    @Test
    public void affiPapersTest(){
        List<SimplePaperVO> ans = significantPaperFetch.getSignificantPaper(1,2,-1,1);
        Assert.assertEquals(2,ans.size());
    }

    @Test
    public void confPapersTest(){
        List<SimplePaperVO> ans = significantPaperFetch.getSignificantPaper(1,3,2,1);
        Assert.assertEquals(0,ans.size());
    }
}
