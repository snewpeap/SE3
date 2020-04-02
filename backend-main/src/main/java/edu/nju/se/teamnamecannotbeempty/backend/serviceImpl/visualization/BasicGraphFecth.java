package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.Link;
import edu.nju.se.teamnamecannotbeempty.backend.vo.Node;
import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BasicGraphFecth {

    private final AffiliationDao affiliationDao;
    private final AuthorDao authorDao;
    private final ConferenceDao conferenceDao;
    private final PaperDao paperDao;
    private final TermDao termDao;
    private final TermPopDao termPopDao;
    private final EntityMsg entityMsg;
    private final PaperPopDao paperPopDao;

    @Autowired
    public BasicGraphFecth(AffiliationDao affiliationDao, AuthorDao authorDao, ConferenceDao conferenceDao,
                           PaperDao paperDao, TermDao termDao, EntityMsg entityMsg,
                           TermPopDao termPopDao, PaperPopDao paperPopDao) {
        this.affiliationDao = affiliationDao;
        this.authorDao = authorDao;
        this.conferenceDao = conferenceDao;
        this.paperDao = paperDao;
        this.termDao = termDao;
        this.entityMsg = entityMsg;
        this.termPopDao = termPopDao;
        this.paperPopDao = paperPopDao;
    }

    @Cacheable(value = "getBasicGraph", key = "#p0+'_'+#p1", unless = "#result=null")
    public GraphVO getBasicGraph(long id, int type) {
        if (type == entityMsg.getAuthorType()) return AuthorBasicGraph(id);
        else if (type == entityMsg.getAffiliationType()) return AffiliationBasicGraph(id);
        else if (type == entityMsg.getConferenceType()) return ConferenceBasicGraph(id);
        else if (type == entityMsg.getTermType()) return TermBasicGraph(id);
        else return null;
    }

    private GraphVO AuthorBasicGraph(long id) {
        List<Node> nodes = generatePaperNode(paperPopDao.findTopPapersByAuthorId(id));
        nodes.addAll(generateAffiliationNode(affiliationDao.getAffiliationsByAuthor(id)));
        List<Link> links = generateLinksWithoutWeight(id, entityMsg.getAuthorType(), nodes);
        List<Term.Popularity> termPopularities = termPopDao.getTermPopByAuthorID(id);
        nodes.addAll(generateTermNode(termPopularities));
        links.addAll(generateLinksWithWeightInAuthor(id, termPopularities));
        return new GraphVO(id, entityMsg.getAuthorType(), authorDao.findById(id).get().getName(), nodes, links);
    }


    //默认机构的研究方向是其辖下作者的研究方向的并集
    private GraphVO AffiliationBasicGraph(long id) {
        List<Author> authors = authorDao.getAuthorsByAffiliation(id);
        List<Node> nodes = generateAuthorNode(authors);
        List<Link> links = generateLinksWithoutWeight(id, entityMsg.getAffiliationType(), nodes);
        List<Term.Popularity> termPopularityList = termPopDao.getTermPopByAffiID(id);
        nodes.addAll(generateTermNode(termPopularityList));
        links.addAll(generateLinksWithWeightInAffi(id, termPopularityList));
        List<Link> links1 = authors.stream().flatMap(author -> termPopDao.getTermPopByAuthorID(author.getActual().getId()).stream()
                .map(termPop -> new Link(author.getActual().getId(), entityMsg.getAuthorType(), termPop.getTerm().getId(),
                        entityMsg.getTermType(), paperPopDao.getWeightByAuthorOnKeyword(author.getActual().getId(), termPop.getTerm().getId()))))
                .collect(Collectors.toList());
        links.addAll(links1);
        return new GraphVO(id, entityMsg.getAffiliationType(), affiliationDao.findById(id).get().getName(), nodes, links);
    }

    private GraphVO ConferenceBasicGraph(long id) {
        List<Node> nodes = generatePaperNode(paperPopDao.findTopPapersByConferenceId(id));
        List<Link> links = generateLinksWithoutWeight(id, entityMsg.getConferenceType(), nodes);
        return new GraphVO(id, entityMsg.getConferenceType(), conferenceDao.findById(id).get().buildName(), nodes, links);
    }

    //默认论文的研究方向也属于作者的研究方向
    private GraphVO TermBasicGraph(long id) {
        List<Author> authors = authorDao.getAuthorsByKeyword(id);
        List<Paper> papers = paperDao.getPapersByKeyword(id);
        List<Node> nodes = generateAuthorNode(authors);
        nodes.addAll(papers.stream().map(paper ->
                new Node(paper.getId(), paper.getTitle(), entityMsg.getPaperType())).collect(Collectors.toList()));

        List<Link> links = papers.stream().flatMap(paper -> paper.getAa().stream().map(
                author_affiliation -> new Link(paper.getId(), entityMsg.getPaperType(), author_affiliation.getAuthor().getActual().getId(),
                        entityMsg.getAuthorType(), -1)
        )).collect(Collectors.toList());

        List<Link> links1 = authors.stream().map(author -> new Link(id, entityMsg.getTermType(),
                author.getActual().getId(), entityMsg.getAuthorType(), paperPopDao.getWeightByAuthorOnKeyword(
                author.getActual().getId(), id))).collect(Collectors.toList());
        links.addAll(links1);

        List<Link> links2 = papers.stream().map(paper -> new Link(id, entityMsg.getTermType(),
                paper.getId(), entityMsg.getPaperType(), paperPopDao.getByPaper_Id(paper.getId()).get().getPopularity()))
                .collect(Collectors.toList());
        links.addAll(links2);

        return new GraphVO(id, entityMsg.getTermType(), termDao.findById(id).get().getContent(), nodes, links);
    }

    private List<Link> generateLinksWithWeightInAuthor(long sourceId, List<Term.Popularity> termPopularityList) {
        return termPopularityList.stream().map(termPopularity ->
                new Link(sourceId, entityMsg.getAuthorType(), termPopularity.getTerm().getId(), entityMsg.getTermType(),
                        paperPopDao.getWeightByAuthorOnKeyword(sourceId, termPopularity.getTerm().getId())))
                .collect(Collectors.toList());
    }

    private List<Link> generateLinksWithWeightInAffi(long sourceId, List<Term.Popularity> termPopularityList) {

        return termPopularityList.stream().map(termPopularity ->
                new Link(sourceId, entityMsg.getAffiliationType(), termPopularity.getTerm().getId(), entityMsg.getTermType(),
                        paperPopDao.getWeightByAffiOnKeyword(sourceId, termPopularity.getTerm().getId())))
                .collect(Collectors.toList());
    }

    private List<Link> generateLinksWithoutWeight(long sourceId, int sourceType, List<Node> targeNodes) {
        return targeNodes.stream().map(
                targeNode -> new Link(sourceId, sourceType, targeNode.getId(), targeNode.getType(), -1))
                .collect(Collectors.toList());
    }


    List<Node> generateAuthorNode(List<Author> authors) {
        return authors.stream().map(
                author -> new Node(author.getActual().getId(), author.getName(), entityMsg.getAuthorType()))
                .collect(Collectors.toList());
    }

    private List<Node> generateTermNode(List<Term.Popularity> terms) {
        return terms.stream().map(
                term -> new Node(term.getTerm().getId(), term.getTerm().getContent(), entityMsg.getTermType()))
                .collect(Collectors.toList());
    }

    private List<Node> generatePaperNode(List<Paper.Popularity> papers) {
        return papers.stream().map(
                paper -> new Node(paper.getPaper().getId(), paper.getPaper().getTitle(), entityMsg.getPaperType()))
                .collect(Collectors.toList());
    }

    List<Node> generateAffiliationNode(List<Affiliation> affiliations) {
        return affiliations.stream().map(
                affiliation -> new Node(affiliation.getActual().getId(), affiliation.getName(), entityMsg.getAffiliationType()))
                .collect(Collectors.toList());
    }
}
