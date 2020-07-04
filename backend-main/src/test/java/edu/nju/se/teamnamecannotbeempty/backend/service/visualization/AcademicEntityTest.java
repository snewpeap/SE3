package edu.nju.se.teamnamecannotbeempty.backend.service.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization.AcademicEntityFetch;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization.FetchForCache;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityVO;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.*;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AcademicEntityTest {

    @Mock
    private AffiliationDao affiliationDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private ConferenceDao conferenceDao;
    @Mock
    private PaperDao paperDao;
    @Mock
    private TermPopDao termPopDao;
    @Mock
    private EntityMsg entityMsg;
    @Mock
    private PaperPopDao paperPopDao;
    @Mock
    private TermDao termDao;
    @Mock
    private FetchForCache fetchForCache;
    @InjectMocks
    private AcademicEntityFetch academicEntityFetch;

    @Before
    public void setup(){
        when(entityMsg.getAuthorType()).thenReturn(1);
        when(entityMsg.getAffiliationType()).thenReturn(2);
        when(entityMsg.getConferenceType()).thenReturn(3);
        when(affiliationDao.getByAlias_Id(anyLong())).thenReturn(new ArrayList<>());
        when(authorDao.getByAlias_Id(anyLong())).thenReturn(new ArrayList<>());

        Author author1 = new Author(); author1.setId(1L); author1.setName("Author1");
        Author author2 = new Author(); author2.setId(2L); author2.setName("Author2");
        Author author3 = new Author(); author3.setId(3L); author3.setName("Author3");

        Affiliation affiliation1 = new Affiliation(); affiliation1.setId(1L); affiliation1.setName("affiliation1");
        Affiliation affiliation2 = new Affiliation(); affiliation2.setId(2L); affiliation2.setName("affiliation2");
        Affiliation affiliation3 = new Affiliation(); affiliation3.setId(3L); affiliation3.setName("affiliation3");

        Conference conference1 = mock(Conference.class); when(conference1.getId()).thenReturn(1L);
        when(conference1.buildName()).thenReturn("conference1");
        Conference conference2 = mock(Conference.class); when(conference2.getId()).thenReturn(2L);
        when(conference2.buildName()).thenReturn("conference2");
        Conference conference3 = mock(Conference.class); when(conference3.getId()).thenReturn(3L);
        when(conference3.buildName()).thenReturn("conference3");

        Term term1 = new Term(); term1.setId(1L); term1.setContent("term1");
        Term.Popularity termPopularity1 = new Term.Popularity(); termPopularity1.setTerm(term1);
        Term term2 = new Term(); term2.setId(1L); term2.setContent("term2");
        Term.Popularity termPopularity2 = new Term.Popularity(); termPopularity2.setTerm(term1);
        Term term3 = new Term(); term3.setId(1L); term3.setContent("term3");
        Term.Popularity termPopularity3 = new Term.Popularity(); termPopularity3.setTerm(term1);

        Conference conference = new Conference(); conference.setName("conference1"); conference.setYear_highlight("");
        conference.setId(1L);
        Paper paper = new Paper(); paper.setId(1L); paper.setTitle("paper1"); paper.setConference(conference);
        paper.setAa(new ArrayList<>());paper.setAuthor_keywords(new ArrayList<>());
        Paper.Popularity paperPop = new Paper.Popularity(); paperPop.setPaper(paper);

        when(affiliationDao.getAffiliationsByAuthor(anyLong())).thenReturn(Arrays.asList(affiliation1,affiliation2,affiliation3));
        when(affiliationDao.getAffiliationsByConference(anyLong())).thenReturn(Arrays.asList(affiliation1,affiliation2,affiliation3));
        when(conferenceDao.getConferencesByAuthor(anyLong())).thenReturn(Arrays.asList(conference1,conference2,conference3));
        when(conferenceDao.getConferencesByAffiliation(anyLong())).thenReturn(Arrays.asList(conference1,conference2,conference3));
        when(termPopDao.getTermPopByAuthorID(anyLong())).thenReturn(Arrays.asList(termPopularity1,termPopularity2,termPopularity3));
        when(termPopDao.getTermPopByAffiID(anyLong())).thenReturn(Arrays.asList(termPopularity1,termPopularity2,termPopularity3));
        when(termPopDao.getTermPopByConferenceID(anyLong())).thenReturn(Arrays.asList(termPopularity1,termPopularity2,termPopularity3));
        when(authorDao.getAuthorsByAffiliation(anyLong())).thenReturn(Arrays.asList(author1,author2,author3));
        when(authorDao.getAuthorsByConference(anyLong())).thenReturn(Arrays.asList(author1,author2,author3));

        when(paperPopDao.getWeightByAffiOnKeyword(anyLong(),anyLong())).thenReturn(1.0);
        when(paperPopDao.getWeightByAffiOnKeyword(anyLong(),anyLong())).thenReturn(1.0);
        when(paperPopDao.getWeightByConferenceOnKeyword(anyLong(),anyLong())).thenReturn(1.0);
        when(paperPopDao.findTopPapersByAuthorId(anyLong())).thenReturn(Collections.singletonList(paperPop));
        when(paperPopDao.findTopPapersByAffiId(anyLong())).thenReturn(Collections.singletonList(paperPop));
        when(paperPopDao.findTopPapersByConferenceId(anyLong())).thenReturn(Collections.singletonList(paperPop));
        Optional<Author> optionalAuthor = Optional.of(author1);
        Optional<Affiliation> optionalAffiliation = Optional.of(affiliation1);
        Optional<Conference> optionalConference = Optional.of(conference1);
        when(authorDao.findById(anyLong())).thenReturn(optionalAuthor);
        when(affiliationDao.findById(anyLong())).thenReturn(optionalAffiliation);
        when(conferenceDao.findById(anyLong())).thenReturn(optionalConference);

    }

    @Test
    public void authorEntityTest(){
        AcademicEntityVO academicEntityVO = academicEntityFetch.getAcademicEntity(1L,1);
        Assert.assertEquals(academicEntityVO.getType(), 1);
        Assert.assertEquals(academicEntityVO.getName(), "Author1");
    }

    @Test
    public void affiliationEntityTest(){
        AcademicEntityVO academicEntityVO = academicEntityFetch.getAcademicEntity(1L,2);
        Assert.assertEquals(academicEntityVO.getType(), 2);
        Assert.assertEquals(academicEntityVO.getName(), "affiliation1");
    }

    @Test
    public void conferenceEntityTest(){
        AcademicEntityVO academicEntityVO = academicEntityFetch.getAcademicEntity(1L,3);
        Assert.assertEquals(academicEntityVO.getType(), 3);
        Assert.assertEquals(academicEntityVO.getName(), "conference1");
    }


}
