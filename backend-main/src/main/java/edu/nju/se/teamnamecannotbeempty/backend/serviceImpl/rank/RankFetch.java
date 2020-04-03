package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.rank;

import edu.nju.se.teamnamecannotbeempty.backend.vo.RankItem;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankFetch {

    private final PaperDao paperDao;

    @Autowired
    public RankFetch(PaperDao paperDao) {
        this.paperDao = paperDao;
    }

    static class AuthorPaperCitationNum {

        private String author;
        private int paperCitation;

        AuthorPaperCitationNum(String author, int paperCitation) {
            this.author = author;
            this.paperCitation = paperCitation;
        }

        String getAuthor() {
            return author;
        }

        void setAuthor(String author) {
            this.author = author;
        }

        int getPaperCitation() {
            return paperCitation;
        }
    }


    @Cacheable(value = "getRank", key = "#mode+'_'+#startYear+'_'+#endYear", unless = "#result=null")
    @Transactional
    public List<RankItem> getAllResult(String mode, int startYear, int endYear) {
        List<Paper> paperList = paperDao.findAllByConference_YearBetween(startYear, endYear);
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

    private List<RankItem> paperCited(List<Paper> paperList) {
        return paperList.stream().sorted(Comparator.comparingInt(Paper::getCitation))
                .map(paper -> new RankItem(paper.getTitle(), paper.getCitation())).collect(Collectors.toList());
    }

    private List<RankItem> authorCited(List<Paper> paperList) {
        List<AuthorPaperCitationNum> authorPaperCitationNumList = getAuthorPaperCitationNumList(paperList);
        Map<String, Long> authorCitedNums = authorPaperCitationNumList.stream().collect(Collectors.groupingBy(AuthorPaperCitationNum::getAuthor, Collectors.summingLong(AuthorPaperCitationNum::getPaperCitation)));
        return mapToList(authorCitedNums);
    }

    private List<RankItem> authorPaper(List<Paper> paperList) {
        List<AuthorPaperCitationNum> authorPaperCitationNumList = getAuthorPaperCitationNumList(paperList);
        Map<String, Long> authorPaperNums = authorPaperCitationNumList.stream().collect(Collectors.groupingBy(AuthorPaperCitationNum::getAuthor, Collectors.counting()));
        return mapToList(authorPaperNums);
    }

    private List<RankItem> affiliationPaper(List<Paper> paperList) {
        List<Affiliation> affiliationList = paperList.stream().flatMap(paper -> paper.getAa().stream().filter(author_affiliation ->
                (!"".equals(author_affiliation.getAffiliation().getName()) && !"NA".equals(author_affiliation.getAffiliation().getName())))
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(a -> a.getAffiliation().getName() + ";" + a.getAffiliation().getCountry()))), ArrayList::new)).stream()
                .map(Author_Affiliation::getAffiliation)).collect(Collectors.toList());
        Map<String, Long> affiliationPaperNums = affiliationList.stream().collect(Collectors.groupingBy(Affiliation::getName, Collectors.counting()));
        return mapToList(affiliationPaperNums);
    }

    private List<RankItem> publicationPaper(List<Paper> paperList) {
        List<Conference> conferenceList = paperList.stream().map(Paper::getConference).collect(Collectors.toList());
        Map<String, Long> publicationPaperNums = conferenceList.stream().collect(Collectors.groupingBy(Conference::buildName, Collectors.counting()));
        return mapToList(publicationPaperNums);
    }

    private List<RankItem> keywordPaper(List<Paper> paperList) {
        List<Term> termList = paperList.stream().flatMap(paper -> paper.getAuthor_keywords().stream().filter(a -> !"".equals(a.getContent()))).collect(Collectors.toList());
        termList.addAll(paperList.stream().flatMap(paper -> paper.getIeee_terms().stream().filter(a -> !"".equals(a.getContent()))).collect(Collectors.toList()));
        termList.addAll(paperList.stream().flatMap(paper -> paper.getInspec_controlled().stream().filter(a -> !"".equals(a.getContent()))).collect(Collectors.toList()));
        termList.addAll(paperList.stream().flatMap(paper -> paper.getInspec_non_controlled().stream().filter(a -> !"".equals(a.getContent()))).collect(Collectors.toList()));
        termList.addAll(paperList.stream().flatMap(paper -> paper.getMesh_terms().stream().filter(a -> !"".equals(a.getContent()))).collect(Collectors.toList()));
        Map<String, Long> termPaperNums = termList.stream().collect(Collectors.groupingBy(Term::getContent, Collectors.counting()));
        return mapToList(termPaperNums);
    }

    //从Paper的list转为AuthorPaperCitationNum的List
    private List<AuthorPaperCitationNum> getAuthorPaperCitationNumList(List<Paper> paperList) {
        return paperList.stream().
                flatMap(paper -> paper.getAa().stream().filter(author_affiliation -> !"".equals(author_affiliation.getAuthor().getName()))
                        .map(author_affiliation -> new AuthorPaperCitationNum(author_affiliation.getAuthor().getName(), paper.getCitation())))
                .collect(Collectors.toList());
    }

    //将Map转为RankItem的List
    private List<RankItem> mapToList(Map<String, Long> map) {
        return map.entrySet().stream().sorted(Comparator.comparingLong(Map.Entry::getValue))
                .map(e -> new RankItem(e.getKey(), e.getValue().intValue())).collect(Collectors.toList());
    }
}
