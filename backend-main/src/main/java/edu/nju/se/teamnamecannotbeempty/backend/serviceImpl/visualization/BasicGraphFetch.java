package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.Link;
import edu.nju.se.teamnamecannotbeempty.backend.vo.Node;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AffiPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AuthorPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BasicGraphFetch {

    private final AffiliationDao affiliationDao;
    private final AuthorDao authorDao;
    private final ConferenceDao conferenceDao;
    private final PaperDao paperDao;
    private final TermDao termDao;
    private final TermPopDao termPopDao;
    private final EntityMsg entityMsg;
    private final PaperPopDao paperPopDao;
    private final AuthorPopDao authorPopDao;
    private final AffiPopDao affiPopDao;
    private final FetchForCache fetchForCache;

    @Autowired
    public BasicGraphFetch(AffiliationDao affiliationDao, AuthorDao authorDao, ConferenceDao conferenceDao,
                           PaperDao paperDao, TermDao termDao, EntityMsg entityMsg,
                           TermPopDao termPopDao, PaperPopDao paperPopDao, AuthorPopDao authorPopDao,
                           AffiPopDao affiPopDao, FetchForCache fetchForCache) {
        this.affiliationDao = affiliationDao;
        this.authorDao = authorDao;
        this.conferenceDao = conferenceDao;
        this.paperDao = paperDao;
        this.termDao = termDao;
        this.entityMsg = entityMsg;
        this.termPopDao = termPopDao;
        this.paperPopDao = paperPopDao;
        this.authorPopDao = authorPopDao;
        this.affiPopDao = affiPopDao;
        this.fetchForCache = fetchForCache;
    }

    @Cacheable(value = "getBasicGraph", key = "#p0+'_'+#p1", unless = "#result=null")
    public GraphVO getBasicGraph(long id, int type) {
        if (type == entityMsg.getAuthorType()) return authorBasicGraph(id);
        else if (type == entityMsg.getAffiliationType()) return affiliationBasicGraph(id);
        else if (type == entityMsg.getConferenceType()) return conferenceBasicGraph(id);
        else if (type == entityMsg.getTermType()) return termBasicGraph(id);
        else return null;
    }

    private GraphVO authorBasicGraph(long id) {
        List<Paper.Popularity> paperPopList = paperPopDao.findTopPapersByAuthorId(id);
        List<Node> nodes = generatePaperNode(paperPopList);
        //去NA
        List<Affiliation> affiliationList = affiliationDao.getAffiliationsByAuthor(id).stream()
                .filter(affiliation -> !affiliation.getActual().getName().equals("NA")).
                        collect(Collectors.toList());

        nodes.addAll(generateAffiliationNode(affiliationList));
        List<Link> links = generateLinksWithoutWeight(id, entityMsg.getAuthorType(), nodes);
        List<Term.Popularity> termPopularities = termPopDao.getTermPopByAuthorID(id);
        nodes.addAll(generateTermNode(termPopularities));
        links.addAll(generateLinksWithWeightInAuthor(id, termPopularities));
        List<Link> links1 = paperPopList.stream().flatMap(paperPop ->
                fetchForCache.getTermPopByPaperID(paperPop.getPaper().getId()).stream()
                        .map(termPop -> new Link(paperPop.getPaper().getId(), entityMsg.getPaperType(),
                                termPop.getTerm().getId(),
                                entityMsg.getTermType(), paperPop.getPopularity()))).
                collect(Collectors.toList());
        links.addAll(links1);
        String centerName = authorDao.findById(id).orElseGet(Author::new).getActual().getName();
        Node centerNode = new Node(id, centerName, entityMsg.getAuthorType());
        nodes.add(centerNode);
        nodes.forEach(node -> node.setPopularity(addPopInNode(node)));
        return new GraphVO(id, entityMsg.getAuthorType(), centerName, nodes, links,
                addPopInNode(centerNode));
    }


    //默认机构的研究方向是其辖下作者的研究方向的并集
    private GraphVO affiliationBasicGraph(long id) {
        List<Author> authors = authorDao.getAuthorsByAffiliation(id);
        List<Node> nodes = generateAuthorNode(authors);
        List<Link> links = generateLinksWithoutWeight(id, entityMsg.getAffiliationType(), nodes);
        List<Term.Popularity> termPopularityList = termPopDao.getTermPopByAffiID(id);
        nodes.addAll(generateTermNode(termPopularityList));
        links.addAll(generateLinksWithWeightInAffi(id, termPopularityList));
        List<Link> links1 = authors.stream().flatMap(author ->
                termPopDao.getTermPopByAuthorID(author.getActual().getId()).stream()
                        .filter(termPop -> termIsBelongToAffiliation(termPop.getTerm().getId(), id))
                        .map(termPop -> new Link(author.getActual().getId(),
                                entityMsg.getAuthorType(), termPop.getTerm().getId(),
                                entityMsg.getTermType(), paperPopDao.getWeightByAuthorOnKeyword(
                                author.getActual().getId(), termPop.getTerm().getId()))))
                .collect(Collectors.toList());
        links.addAll(links1);
        String centerName = affiliationDao.findById(id).orElseGet(Affiliation::new).getActual().getName();
        Node centerNode = new Node(id, centerName, entityMsg.getAffiliationType());
        nodes.add(centerNode);
        nodes.forEach(node -> node.setPopularity(addPopInNode(node)));
        return new GraphVO(id, entityMsg.getAffiliationType(), centerName, nodes,
                links, addPopInNode(centerNode));
    }

    private GraphVO conferenceBasicGraph(long id) {
        List<Node> nodes = fetchForCache.getAllPapersByConference(id).stream().map(
                paper -> new Node(paper.getId(), paper.getTitle(), entityMsg.getPaperType())
        ).collect(Collectors.toList());
        List<Link> links = generateLinksWithoutWeight(id, entityMsg.getConferenceType(), nodes);
        String centerName = conferenceDao.findById(id).orElseGet(Conference::new).getName();
        Node centerNode = new Node(id, centerName, entityMsg.getConferenceType());
        nodes.add(centerNode);
        nodes.forEach(node -> node.setPopularity(addPopInNode(node)));
        return new GraphVO(id, entityMsg.getConferenceType(), centerName, nodes,
                links, addPopInNode(centerNode));
    }

    //默认论文的研究方向也属于作者的研究方向
    private GraphVO termBasicGraph(long id) {
        List<Author> authors = authorDao.getAuthorsByKeyword(id);
        List<Paper> papers = paperDao.getPapersByKeyword(id);
        List<Node> nodes = generateAuthorNode(authors);
        nodes.addAll(papers.stream().map(paper ->
                new Node(paper.getId(), paper.getTitle(),
                        entityMsg.getPaperType())).collect(Collectors.toList()));

        List<Link> links = papers.stream().flatMap(paper -> paper.getAa().stream().map(
                author_affiliation -> new Link(paper.getId(), entityMsg.getPaperType(),
                        author_affiliation.getAuthor().getActual().getId(),
                        entityMsg.getAuthorType(), -1.0)
        )).collect(Collectors.toList());

        List<Link> links1 = authors.stream().map(author -> new Link(id, entityMsg.getTermType(),
                author.getActual().getId(), entityMsg.getAuthorType(),
                paperPopDao.getWeightByAuthorOnKeyword(author.getActual().getId(), id)))
                .collect(Collectors.toList());
        links.addAll(links1);

        List<Link> links2 = papers.stream().map(paper -> new Link(id, entityMsg.getTermType(),
                paper.getId(), entityMsg.getPaperType(), paperPopDao.getByPaper_Id(paper.getId()).
                orElseGet(Paper.Popularity::new).getPopularity()))
                .collect(Collectors.toList());
        links.addAll(links2);

        String centerName = termDao.findById(id).orElseGet(Term::new).getContent();
        Node centerNode = new Node(id, centerName, entityMsg.getTermType());
        nodes.add(centerNode);
        nodes.forEach(node -> node.setPopularity(addPopInNode(node)));
        return new GraphVO(id, entityMsg.getTermType(), centerName, nodes,
                links, addPopInNode(centerNode));
    }

    private List<Link> generateLinksWithWeightInAuthor(long sourceId, List<Term.Popularity> termPopularityList) {
        return termPopularityList.stream().map(termPopularity ->
                new Link(sourceId, entityMsg.getAuthorType(), termPopularity.getTerm().getId(),
                        entityMsg.getTermType(), paperPopDao.getWeightByAuthorOnKeyword(
                        sourceId, termPopularity.getTerm().getId())))
                .collect(Collectors.toList());
    }

    private List<Link> generateLinksWithWeightInAffi(long sourceId,
                                                     List<Term.Popularity> termPopularityList) {

        return termPopularityList.stream().map(termPopularity ->
                new Link(sourceId, entityMsg.getAffiliationType(), termPopularity.getTerm().getId(),
                        entityMsg.getTermType(), paperPopDao.getWeightByAffiOnKeyword(
                        sourceId, termPopularity.getTerm().getId())))
                .collect(Collectors.toList());
    }

    private List<Link> generateLinksWithoutWeight(long sourceId, int sourceType, List<Node> targetNodes) {
        return targetNodes.stream().map(
                targetNode -> new Link(sourceId, sourceType, targetNode.getEntityId(),
                        targetNode.getEntityType(), -1.0))
                .collect(Collectors.toList());
    }


    private List<Node> generateAuthorNode(List<Author> authors) {
        return authors.stream().map(
                author -> new Node(author.getActual().getId(), author.getActual().getName(),
                        entityMsg.getAuthorType())).collect(Collectors.toList());
    }

    private List<Node> generateTermNode(List<Term.Popularity> terms) {
        return terms.stream().map(
                term -> new Node(term.getTerm().getId(), term.getTerm().getContent(),
                        entityMsg.getTermType()))
                .collect(Collectors.toList());
    }

    private List<Node> generatePaperNode(List<Paper.Popularity> papers) {
        return papers.stream().map(
                paper -> new Node(paper.getPaper().getId(), paper.getPaper().getTitle(),
                        entityMsg.getPaperType()))
                .collect(Collectors.toList());
    }

    private List<Node> generateAffiliationNode(List<Affiliation> affiliations) {
        return affiliations.stream().map(
                affiliation -> new Node(affiliation.getActual().getId(),
                        affiliation.getActual().getName(), entityMsg.getAffiliationType()))
                .collect(Collectors.toList());
    }

    private boolean termIsBelongToAffiliation(long termId, long affiId) {
        List<Term.Popularity> termPopList = termPopDao.getTermPopByAffiID(affiId);
        return termPopList.stream().anyMatch(termPop -> termPop.getTerm().getId() == termId);

    }

    private Double addPopInNode(Node node) {
        double pop = -1.0;
        if (node.getEntityType() == entityMsg.getAuthorType()) {
            Optional<Author.Popularity> authorPop = authorPopDao.findByAuthor_Id(node.getEntityId());
            if (authorPop.isPresent()) pop = authorPop.get().getPopularity();
        } else if (node.getEntityType() == entityMsg.getAffiliationType()) {
            Optional<Affiliation.Popularity> affiPop = affiPopDao.findByAffiliation_Id(node.getEntityId());
            if (affiPop.isPresent()) pop = affiPop.get().getPopularity();
        } else if (node.getEntityType() == entityMsg.getPaperType()) {
            Optional<Paper.Popularity> paperPop = paperPopDao.getByPaper_Id(node.getEntityId());
            if (paperPop.isPresent()) pop = paperPop.get().getPopularity();
        } else if (node.getEntityType() == entityMsg.getTermType()) {
            Optional<Term.Popularity> termPop = termPopDao.getDistinctByTerm_Id(node.getEntityId());
            if (termPop.isPresent()) pop = termPop.get().getPopularity();
        }
        return pop;
    }
}
