package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.Link;
import edu.nju.se.teamnamecannotbeempty.backend.vo.Node;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.ConferenceDao;
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
public class CompleteGraphFetch {

    private final AffiliationDao affiliationDao;
    private final AuthorDao authorDao;
    private final ConferenceDao conferenceDao;
    private final TermPopDao termPopDao;
    private final EntityMsg entityMsg;
    private final PaperPopDao paperPopDao;
    private final AuthorPopDao authorPopDao;
    private final AffiPopDao affiPopDao;
    private final FetchForCache fetchForCache;

    @Autowired
    public CompleteGraphFetch(AffiliationDao affiliationDao, AuthorDao authorDao,
                              ConferenceDao conferenceDao, EntityMsg entityMsg,
                              TermPopDao termPopDao, PaperPopDao paperPopDao,
                              AffiPopDao affiPopDao, AuthorPopDao authorPopDao,
                              FetchForCache fetchForCache) {
        this.affiliationDao = affiliationDao;
        this.authorDao = authorDao;
        this.conferenceDao = conferenceDao;
        this.entityMsg = entityMsg;
        this.termPopDao = termPopDao;
        this.paperPopDao = paperPopDao;
        this.affiPopDao = affiPopDao;
        this.authorPopDao = authorPopDao;
        this.fetchForCache = fetchForCache;
    }

    @Cacheable(value = "getCompleteGraph", key = "#p0+'_'+#p1", unless = "#result=null")
    public GraphVO getCompleteGraph(long id, int type) {
        if (type == entityMsg.getAuthorType()) return authorCompleteGraph(id);
        else if (type == entityMsg.getAffiliationType()) return affiliationCompleteGraph(id);
        else if (type == entityMsg.getConferenceType()) return conferenceCompleteGraph(id);
        else return null;
    }

    private GraphVO authorCompleteGraph(long id) {
        List<Paper> paperList = fetchForCache.getAllPapersByAuthor(id);
        List<Author> authorList = paperList.stream().flatMap(paper -> paper.getAa().stream().map(
                Author_Affiliation::getAuthor)).collect(Collectors.toList());
        List<Node> nodes = generateAuthorNode(authorList);
        List<Link> links = paperList.stream().flatMap(paper -> paper.getAa().stream()
                .filter(author_affiliation -> id !=
                        author_affiliation.getAuthor().getActual().getId()).map(
                        author_affiliation -> new Link(paper.getId(), entityMsg.getPaperType(),
                                author_affiliation.getAuthor().getActual().getId(),
                                entityMsg.getAuthorType(), -1)
                )).collect(Collectors.toList());
        //去NA
        List<Affiliation> affiliationList = affiliationDao.getAffiliationsByAuthor(id).stream()
                .filter(affiliation -> !affiliation.getActual().getName().equals("NA")).
                        collect(Collectors.toList());

        List<Author> authorList1 = affiliationList.stream().flatMap(affiliation ->
                authorDao.getAuthorsByAffiliation(affiliation.getId()).stream()).
                collect(Collectors.toList());
        nodes.addAll(generateAuthorNode(authorList1));
        List<Link> links1 = affiliationList.stream().flatMap(affiliation ->
                authorDao.getAuthorsByAffiliation(affiliation.getActual().getId()).stream().filter(author ->
                        id != author.getActual().getId())
                        .map(author -> new Link(affiliation.getActual().getId(),
                                entityMsg.getAffiliationType(), author.getActual().getId(),
                                entityMsg.getAuthorType(), -1))).
                collect(Collectors.toList());
        links.addAll(links1);

        List<Term.Popularity> termPopList = termPopDao.getTermPopByAuthorID(id);
        List<Author> authorList2 = termPopList.stream().flatMap(
                termPop -> authorDao.getAuthorsByKeyword(termPop.getTerm().getId()).stream()
        ).collect(Collectors.toList());
        nodes.addAll(generateAuthorNode(authorList2));

        List<Link> links2 = termPopList.stream().flatMap(
                termPop -> authorDao.getAuthorsByKeyword(termPop.getTerm().getId()).stream()
                        .filter(author -> id != author.getActual().getId()).map(
                                author -> new Link(termPop.getTerm().getId(),
                                        entityMsg.getTermType(), author.getActual().getId(),
                                        entityMsg.getAuthorType(),
                                        paperPopDao.getWeightByAuthorOnKeyword(author.getActual().getId(),
                                        termPop.getTerm().getId()))
                        )).collect(Collectors.toList());
        links.addAll(links2);

        List<Node> nodeList = nodes.stream().distinct().filter(node -> id != node.getEntityId()).
                collect(Collectors.toList());
        nodeList.forEach(node -> node.setPopularity(addPopInNode(node)));
        Optional<Author.Popularity> authorPopOp = authorPopDao.findByAuthor_Id(id);
        String name = null;
        double pop = -1;
        if (authorPopOp.isPresent()) {
            name = authorPopOp.get().getAuthor().getName();
            pop = authorPopOp.get().getPopularity();
        }
        return new GraphVO(id, entityMsg.getAuthorType(), name, nodeList, links, pop);
    }

    private GraphVO affiliationCompleteGraph(long id) {
        List<Term.Popularity> termPopList = termPopDao.getTermPopByAffiID(id);
        List<Link> links = termPopList.stream().flatMap(
                termPop -> affiliationDao.getAffiliationsByKeyword(termPop.getTerm().getId()).stream()
                        //去NA
                        .filter(affiliation -> (id != affiliation.getActual().getId() &&
                                !affiliation.getActual().getName().equals("NA")))
                        .map(affiliation -> new Link(termPop.getTerm().getId(), entityMsg.getTermType(),
                                affiliation.getActual().getId(), entityMsg.getAffiliationType(),
                                paperPopDao.getWeightByAffiOnKeyword(
                                        affiliation.getActual().getId(), termPop.getTerm().getId()))))
                .collect(Collectors.toList());
        List<Affiliation> affiliationList = termPopList.stream().flatMap(
                termPop -> affiliationDao.getAffiliationsByKeyword(termPop.getTerm().getId()).stream()
                        .filter(affiliation -> !affiliation.getActual().getName().equals("NA")) //去NA
        ).collect(Collectors.toList());
        List<Node> preNodes = generateAffiliationNode(affiliationList);
        List<Node> nodes = preNodes.stream().filter(node -> id != node.getEntityId()).distinct()
                .collect(Collectors.toList());
        nodes.forEach(node -> node.setPopularity(addPopInNode(node)));
        Optional<Affiliation.Popularity> affiPopOp = affiPopDao.findByAffiliation_Id(id);
        String name = null;
        double pop = -1;
        if (affiPopOp.isPresent()) {
            name = affiPopOp.get().getAffiliation().getName();
            pop = affiPopOp.get().getPopularity();
        }
        return new GraphVO(id, entityMsg.getAffiliationType(), name, nodes, links, pop);
    }

    private GraphVO conferenceCompleteGraph(long id) {
        List<Paper> paperList = fetchForCache.getAllPapersByConference(id);
        List<Link> links = paperList.stream().map(paper ->
                new Link(paper.getId(), entityMsg.getPaperType(),
                        paper.getAa().get(0).getAuthor().getActual().getId(),
                        entityMsg.getAuthorType(), -1))
                .collect(Collectors.toList());

        List<Link> links1 = paperList.stream()
                //去NA
                .filter(paper -> !paper.getAa().get(0).getAffiliation().getActual().getName().equals("NA"))
                .map(paper ->
                        new Link(paper.getId(), entityMsg.getPaperType(),
                                paper.getAa().get(0).getAffiliation().getActual().getId(),
                                entityMsg.getAffiliationType(), -1))
                .collect(Collectors.toList());
        links.addAll(links1);

        List<Author_Affiliation> author_affiliationList = paperList.stream().map(
                paper -> paper.getAa().get(0)).collect(Collectors.toList());
        List<Author> authorList = author_affiliationList.stream().map(Author_Affiliation::getAuthor)
                .collect(Collectors.toList());
        List<Node> nodes = generateAuthorNode(authorList);
        List<Affiliation> affiliationList = author_affiliationList.stream()
                .filter(author_affiliation ->
                        !author_affiliation.getAffiliation().getActual().getName().equals("NA"))
                .map(Author_Affiliation::getAffiliation).collect(Collectors.toList());
        nodes.addAll(generateAffiliationNode(affiliationList));

        List<Long> affiIdList = affiliationList.stream().map(affiliation ->
                affiliation.getActual().getId()).
                distinct().collect(Collectors.toList());

        List<Link> links2 = paperList.stream().flatMap(paper ->
                affiliationDao.getAffiliationsByAuthor(paper.getAa().get(0).getAuthor().getActual().getId())
                        .stream().filter(affiliation -> isAffiliationInNodes(affiIdList,
                        affiliation.getActual().getId()))
                        .map(affiliation -> new Link(paper.getAa().get(0).getAuthor().getActual().getId(),
                                entityMsg.getAuthorType(),
                                affiliation.getActual().getId(), entityMsg.getAffiliationType(), -1))
        ).collect(Collectors.toList());

        links.addAll(links2);

        List<Node> reNodes = nodes.stream().distinct().collect(Collectors.toList());
        List<Link> reLinks = links.stream().distinct().collect(Collectors.toList());

        return new GraphVO(id, entityMsg.getConferenceType(), conferenceDao.findById(id).
                orElseGet(Conference::new).getName(), reNodes, reLinks, -1.0);
    }

    private List<Node> generateAffiliationNode(List<Affiliation> affiliations) {
        return affiliations.stream().map(
                affiliation -> new Node(affiliation.getActual().getId(),
                        affiliation.getActual().getName(), entityMsg.getAffiliationType()))
                .collect(Collectors.toList());
    }

    private List<Node> generateAuthorNode(List<Author> authors) {
        return authors.stream().map(
                author -> new Node(author.getActual().getId(), author.getActual().getName(),
                        entityMsg.getAuthorType()))
                .collect(Collectors.toList());
    }

    private boolean isAffiliationInNodes(List<Long> affiIdList, long id) {
        return affiIdList.contains(id);
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
