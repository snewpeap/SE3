package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.service.visualization.EntityService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.*;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntityServiceImpl implements EntityService {

    private final AffiliationDao affiliationDao;
    private final AuthorDao authorDao;
    private final ConferenceDao conferenceDao;
    private final PaperDao paperDao;
    private final RefDao refDao;
    private final TermDao termDao;
    private final TermPopDao termPopDao;
    private final EntityMsg entityMsg;
    private final PaperPopDao paperPopDao;
    private final AcademicEntityFecth academicEntityFecth;

    @Autowired
    public EntityServiceImpl(AffiliationDao affiliationDao, AuthorDao authorDao, ConferenceDao conferenceDao,
                             PaperDao paperDao, RefDao refDao, TermDao termDao, EntityMsg entityMsg,
                             TermPopDao termPopDao, PaperPopDao paperPopDao, AcademicEntityFecth academicEntityFecth) {
        this.affiliationDao = affiliationDao;
        this.authorDao = authorDao;
        this.conferenceDao = conferenceDao;
        this.paperDao = paperDao;
        this.refDao = refDao;
        this.termDao = termDao;
        this.entityMsg = entityMsg;
        this.termPopDao = termPopDao;
        this.paperPopDao = paperPopDao;
        this.academicEntityFecth = academicEntityFecth;
    }

    @Override
    public AcademicEntityVO getAcedemicEntity(long id, int type) {
        return academicEntityFecth.getAcedemicEntity(id,type);
    }

    @Override
    public GraphVO getBasicGraph(long id, int type) {

        return null;
    }

    private GraphVO AuthorBasicGraph(long id, int type){
        List<Node> nodes = generateTermNode(termPopDao.getTermPopByAuthorID(id));

        //TODO
        return null;
    }

    private List<Link> generateLinks(int sourceId, int sourceType, List<Node> targeNodes, double value){
        return null;
    }

    private List<Node> generateAuthorNode(List<Author> authors){
        return authors.stream().map(
                author -> new Node(author.getActual().getId(),author.getName(),entityMsg.getAuthorType()))
                .collect(Collectors.toList());
    }

    private List<Node> generateTermNode(List<Term.Popularity> terms){
        return terms.stream().map(
                term -> new Node(term.getTerm().getId(), term.getTerm().getContent(), entityMsg.getTermType()))
                .collect(Collectors.toList());
    }

    private List<Node> generatePaperNode(List<Paper> papers){
        return papers.stream().map(
                paper -> new Node(paper.getId(), paper.getTitle(), entityMsg.getPaperType()))
                .collect(Collectors.toList());
    }

    private List<Node> generateAffiliationNode(List<Affiliation> affiliations){
        return affiliations.stream().map(
                affiliation -> new Node(affiliation.getActual().getId(),affiliation.getName(),entityMsg.getAffiliationType()))
                .collect(Collectors.toList());
    }

    private List<Node> generateConferenceNode(List<Conference> conferences){
        return conferences.stream().map(
                conference -> new Node(conference.getId(),conference.buildName(),entityMsg.getConferenceType()))
                .collect(Collectors.toList());
    }

    @Override
    public GraphVO getCompleteGraph(long id, int type) {
        return null;
    }
}
