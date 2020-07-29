package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.rank;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization.FetchForCache;
import edu.nju.se.teamnamecannotbeempty.backend.vo.PopRankItem;
import edu.nju.se.teamnamecannotbeempty.backend.vo.RankItem;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AffiPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AuthorPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankFetch {

    private final AffiPopDao affiPopDao;
    private final AuthorPopDao authorPopDao;
    private final TermPopDao termPopDao;
    private final EntityMsg entityMsg;
    private final FetchForCache fetchForCache;

    @Autowired
    public RankFetch(AuthorPopDao authorPopDao, AffiPopDao affiPopDao,
                     TermPopDao termPopDao, EntityMsg entityMsg, FetchForCache fetchForCache) {
        this.affiPopDao = affiPopDao;
        this.authorPopDao = authorPopDao;
        this.termPopDao = termPopDao;
        this.entityMsg = entityMsg;
        this.fetchForCache = fetchForCache;
    }

    static class AuthorPaperCitationNum {

        private final String author;
        private final int paperCitation;

        AuthorPaperCitationNum(String author, int paperCitation) {
            this.author = author;
            this.paperCitation = paperCitation;
        }

        String getAuthor() {
            return author;
        }

        int getPaperCitation() {
            return paperCitation;
        }
    }


    @Cacheable(value = "getRank", key = "#mode+'_'+#startYear+'_'+#endYear", unless = "#result=null")
    public List<RankItem> getAllResult(String mode, int startYear, int endYear) {
        List<Paper> paperList = fetchForCache.findAllByYearBetween(startYear, endYear);
        List<RankItem> rankItemList = new ArrayList<>();

        //'Paper-Cited', 'Author-Cited', 'Author-Paper', 'Affiliation-Paper', 'Publication-Paper', 'Keyword-Paper'
        switch (mode) {
            case "Paper-Cited":
                rankItemList = paperCited(paperList);
                break;
            case "Author-Cited":
                rankItemList = authorCited(paperList);
                break;
            case "Author-Paper":
                rankItemList = authorPaper(paperList);
                break;
            case "Affiliation-Paper":
                rankItemList = affiliationPaper(paperList);
                break;
            case "Publication-Paper":
                rankItemList = publicationPaper(paperList);
                break;
            case "Keyword-Paper":
                rankItemList = keywordPaper(paperList);
                break;
            default:
                break;
        }
        return rankItemList;
    }

    @Cacheable(value = "getPopRank", key = "#p0", unless = "#result=null")
    public List<PopRankItem> getPopRank(int type) {
        if (type == entityMsg.getAuthorType()) return authorPopRank();
        else if (type == entityMsg.getAffiliationType()) return affiliationPopRank();
        else if (type == entityMsg.getTermType()) return termPopRank();
        return new ArrayList<>();
    }

    private synchronized List<RankItem> paperCited(List<Paper> paperList) {
        return paperList.stream().sorted(Comparator.comparingInt(Paper::getCitation))
                .map(paper -> new RankItem(paper.getTitle(),
                        paper.getCitation())).collect(Collectors.toList());
    }

    private synchronized List<RankItem> authorCited(List<Paper> paperList) {
        List<AuthorPaperCitationNum> authorPaperCitationNumList = getAuthorPaperCitationNumList(paperList);
        Map<String, Long> authorCitedNums = authorPaperCitationNumList.stream()
                .collect(Collectors.groupingBy(
                        AuthorPaperCitationNum::getAuthor, Collectors.summingLong(
                                AuthorPaperCitationNum::getPaperCitation)));
        return mapToList(authorCitedNums);
    }

    private synchronized List<RankItem> authorPaper(List<Paper> paperList) {
        List<AuthorPaperCitationNum> authorPaperCitationNumList = getAuthorPaperCitationNumList(paperList);
        Map<String, Long> authorPaperNums = authorPaperCitationNumList.stream().
                collect(Collectors.groupingBy(AuthorPaperCitationNum::getAuthor, Collectors.counting()));
        return mapToList(authorPaperNums);
    }

    private synchronized List<RankItem> affiliationPaper(List<Paper> paperList) {
        List<Affiliation> affiliationList = paperList.stream().flatMap(paper ->
                paper.getAa().stream().filter(author_affiliation ->
                (!"".equals(author_affiliation.getAffiliation().getName())
                        && !"NA".equals(author_affiliation.getAffiliation().getActual().getName())))
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(a ->
                                a.getAffiliation().getActual().getName() + ";" +
                                        a.getAffiliation().getCountry()))), ArrayList::new)).stream()
                .map(Author_Affiliation::getAffiliation)).collect(Collectors.toList());
        Map<String, Long> affiliationPaperNums = affiliationList.stream()
                .collect(Collectors.groupingBy(affiliation ->
                        affiliation.getActual().getName(), Collectors.counting()));
        return mapToList(affiliationPaperNums);
    }

    private synchronized List<RankItem> publicationPaper(List<Paper> paperList) {
        List<Conference> conferenceList = paperList.stream().map(Paper::getConference)
                .collect(Collectors.toList());
        Map<String, Long> publicationPaperNums = conferenceList.stream()
                .filter(conference -> (!"NA".equals(conference.getName())))
                .collect(Collectors.groupingBy(Conference::getName, Collectors.counting()));
        return mapToList(publicationPaperNums);
    }

    private synchronized List<RankItem> keywordPaper(List<Paper> paperList) {
        List<Term> termList = paperList.stream().flatMap(paper ->
                paper.getAuthor_keywords().stream().filter(a -> !"".equals(a.getContent()))).
                collect(Collectors.toList());
//        termList.addAll(paperList.stream().flatMap(paper ->
//                paper.getIeee_terms().stream().filter(a ->
//                        !"".equals(a.getContent()))).collect(Collectors.toList()));
//        termList.addAll(paperList.stream().flatMap(paper ->
//                paper.getInspec_controlled().stream().filter(a ->
//                        !"".equals(a.getContent()))).collect(Collectors.toList()));
//        termList.addAll(paperList.stream().flatMap(paper ->
//                paper.getInspec_non_controlled().stream().filter(a ->
//                        !"".equals(a.getContent()))).collect(Collectors.toList()));
//        termList.addAll(paperList.stream().flatMap(paper ->
//                paper.getMesh_terms().stream().filter(a ->
//                        !"".equals(a.getContent()))).collect(Collectors.toList()));
        Map<String, Long> termPaperNums = termList.stream()
                .collect(Collectors.groupingBy(Term::getContent, Collectors.counting()));
        return mapToList(termPaperNums);
    }

    //从Paper的list转为AuthorPaperCitationNum的List
    private List<AuthorPaperCitationNum> getAuthorPaperCitationNumList(List<Paper> paperList) {
        return paperList.stream().
                flatMap(paper -> paper.getAa().stream().filter(
                        author_affiliation -> !"".equals(
                                author_affiliation.getAuthor().getActual().getName()))
                        .map(author_affiliation -> new AuthorPaperCitationNum(
                                author_affiliation.getAuthor().getActual().getName(),
                                paper.getCitation())))
                .collect(Collectors.toList());
    }

    //将Map转为RankItem的List
    private List<RankItem> mapToList(Map<String, Long> map) {
        return map.entrySet().stream().sorted(Comparator.comparingLong(Map.Entry::getValue))
                .map(e -> new RankItem(e.getKey(), e.getValue().intValue())).collect(Collectors.toList());
    }

    private List<PopRankItem> authorPopRank() {
        List<Author.Popularity> authorPops = authorPopDao.findTop20ByOrderByPopularityDesc();
        return authorPops.stream().map(authorPop ->
                new PopRankItem(authorPop.getAuthor().getActual().getId(),
                        authorPop.getAuthor().getActual().getName(), authorPop.getPopularity()))
                .collect(Collectors.toList());
    }

    private List<PopRankItem> affiliationPopRank() {
        List<Affiliation.Popularity> affiPops = affiPopDao.findTop20ByOrderByPopularityDesc();
        return affiPops.stream().map(affiPop ->
                new PopRankItem(affiPop.getAffiliation().getActual().getId(),
                        affiPop.getAffiliation().getActual().getName(), affiPop.getPopularity()))
                .collect(Collectors.toList());
    }

    private List<PopRankItem> termPopRank() {
        List<Term.Popularity> termPops = termPopDao.findTop20ByOrderByPopularityDesc();
        return termPops.stream().map(termPop ->
                new PopRankItem(termPop.getTerm().getId(), termPop.getTerm().getContent(),
                        termPop.getPopularity()))
                .collect(Collectors.toList());
    }
}
