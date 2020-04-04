package edu.nju.se.teamnamecannotbeempty.backend.service.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization.CompleteGraphFetch;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.ConferenceDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
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
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CompleteGraphTest {

    @Mock
    private AffiliationDao affiliationDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private ConferenceDao conferenceDao;
    @Mock
    private TermPopDao termPopDao;
    @Mock
    private EntityMsg entityMsg;
    @Mock
    private PaperPopDao paperPopDao;
    @InjectMocks
    private CompleteGraphFetch completeGraphFetch;


    @Before
    public void setup(){
        when(entityMsg.getAffiliationType()).thenReturn(2);
        when(entityMsg.getAuthorType()).thenReturn(1);
        when(entityMsg.getConferenceType()).thenReturn(3);
        when(entityMsg.getTermType()).thenReturn(4);
        when(entityMsg.getPaperType()).thenReturn(5);
    }

    @Test
    public void authorCompleteGraphTest(){
        Author author1 = new Author(); author1.setId(1L); author1.setName("author1");
        Optional<Author> optionalAuthor = Optional.of(author1);
        when(authorDao.findById(1L)).thenReturn(optionalAuthor);
        Paper paper = new Paper(); paper.setId(1L); paper.setTitle("paper1");
        Paper.Popularity paperPop = new Paper.Popularity(); paperPop.setPaper(paper);
        when(paperPopDao.findTopPapersByAuthorId(1L)).thenReturn(Collections.singletonList(paperPop));
        Author author2 = new Author(); author2.setId(2L); author2.setName("author2");
        Author author3 = new Author(); author3.setId(3L); author3.setName("author3");
        Author author4 = new Author(); author4.setId(4L); author4.setName("author4");
        Author_Affiliation author_affiliation1 = new Author_Affiliation(); author_affiliation1.setAuthor(author1);
        Author_Affiliation author_affiliation2 = new Author_Affiliation(); author_affiliation2.setAuthor(author2);
        Author_Affiliation author_affiliation3 = new Author_Affiliation(); author_affiliation3.setAuthor(author3);
        paper.setAa(Arrays.asList(author_affiliation1,author_affiliation2,author_affiliation3));
        Affiliation affiliation1 = new Affiliation(); affiliation1.setId(1L); affiliation1.setName("affiliation1");
        when(affiliationDao.getAffiliationsByAuthor(1L)).thenReturn(Collections.singletonList(affiliation1));
        when(authorDao.getAuthorsByAffiliation(1L)).thenReturn(Arrays.asList(author1,author3,author4));
        Term term1 = new Term(); term1.setContent("term1"); term1.setId(1L);
        Term.Popularity termPop = new Term.Popularity(); termPop.setTerm(term1);
        when(termPopDao.getTermPopByAuthorID(1L)).thenReturn(Collections.singletonList(termPop));
        when(authorDao.getAuthorsByKeyword(1L)).thenReturn(Arrays.asList(author2,author3));
        when(paperPopDao.getWeightByAuthorOnKeyword(anyLong(),anyLong())).thenReturn(4.0);
        GraphVO graphVO = completeGraphFetch.getCompleteGraph(1L, 1);
        Assert.assertEquals(3,graphVO.getNodes().size());
        Assert.assertEquals(6,graphVO.getLinks().size());
    }

    @Test
    public void affiliationCompleteGraphTest(){
        Affiliation affiliation1 = new Affiliation(); affiliation1.setId(1L); affiliation1.setName("affiliation1");
        Optional<Affiliation> optionalAffiliation = Optional.of(affiliation1);
        when(affiliationDao.findById(1L)).thenReturn(optionalAffiliation);
        Term term1 = new Term(); term1.setContent("term1"); term1.setId(1L);
        Term.Popularity termPop = new Term.Popularity();
        termPop.setTerm(term1);
        when(termPopDao.getTermPopByAffiID(1L)).thenReturn(Collections.singletonList(termPop));

        Affiliation affiliation2 = new Affiliation(); affiliation2.setId(2L); affiliation2.setName("affiliation2");
        when(affiliationDao.getAffiliationsByKeyword(1L)).thenReturn(Arrays.asList(affiliation1,affiliation2));
        when(paperPopDao.getWeightByAffiOnKeyword(2L,1L)).thenReturn(2.0);
        GraphVO graphVO = completeGraphFetch.getCompleteGraph(1L, 2);
        Assert.assertEquals(1,graphVO.getNodes().size());
        Assert.assertEquals(1,graphVO.getLinks().size());
    }

    @Test
    public void conferenceCompleteGraphTest(){
        Conference conference1 = new Conference(); conference1.setId(1L);
        Optional<Conference> optionalConference = Optional.of(conference1);
        when(conferenceDao.findById(1L)).thenReturn(optionalConference);
        Paper paper = new Paper(); paper.setId(1L); paper.setTitle("paper1"); paper.setConference(conference1);
        Paper.Popularity paperPop = new Paper.Popularity(); paperPop.setPaper(paper);
        when(paperPopDao.findTopPapersByConferenceId(1L)).thenReturn(Collections.singletonList(paperPop));
        Author author1 = new Author(); author1.setId(1L); author1.setName("author1");
        Affiliation affiliation1 = new Affiliation(); affiliation1.setId(1L); affiliation1.setName("affiliation1");
        Author_Affiliation aa1 = new Author_Affiliation(); aa1.setAuthor(author1); aa1.setAffiliation(affiliation1);
        paper.setAa(Collections.singletonList(aa1));
        when(affiliationDao.getAffiliationsByAuthor(1L)).thenReturn(Collections.singletonList(affiliation1));
        when(paperPopDao.findTopPapersByConferenceId(1L)).thenReturn(Collections.singletonList(paperPop));

        GraphVO graphVO=completeGraphFetch.getCompleteGraph(1L,3);
        Assert.assertEquals(2,graphVO.getNodes().size());
        Assert.assertEquals(3,graphVO.getLinks().size());
        Assert.assertEquals(graphVO.getId(),"30000000001");

    }
}
