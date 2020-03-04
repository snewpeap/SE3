package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.rank;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.RankMsg;
import edu.nju.se.teamnamecannotbeempty.backend.dao.PaperDao;
import edu.nju.se.teamnamecannotbeempty.backend.po.*;
import edu.nju.se.teamnamecannotbeempty.backend.service.rank.RankService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.RankItem;
import edu.nju.se.teamnamecannotbeempty.backend.vo.RankVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankServiceImpl implements RankService {

    private final PaperDao paperDao;
    private final RankMsg rankMsg;

    @Autowired
    public RankServiceImpl(PaperDao paperDao, RankMsg rankMsg) {
        this.paperDao = paperDao;
        this.rankMsg = rankMsg;
    }

    class AuthorPaperCitationNum {

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

    @Override
    @Transactional
    public ResponseVO getRank(String mode, Integer pageNumber, boolean descend, int startYear, int endYear) {
        if(pageNumber==null||pageNumber<=0) pageNumber=1;
        List<Paper> paperList = paperDao.findAllByConference_YearBetween(startYear,endYear);
        List<RankItem> rankItemList = new ArrayList<>();
        //'Paper-Cited', 'Author-Cited', 'Author-Paper', 'Affiliation-Paper', 'Publication-Paper', 'Keyword-Paper'
        switch (mode){
            case "Paper-Cited":
                rankItemList = paperCited(paperList,descend);
                break;
            case "Author-Cited":
                rankItemList=authorCited(paperList,descend);
                break;
            case "Author-Paper":
                rankItemList=authorPaper(paperList,descend);
                break;
            case "Affiliation-Paper":
                rankItemList=affiliationPaper(paperList,descend);
                break;
            case "Publication-Paper":
                rankItemList=publicationPaper(paperList,descend);
                break;
            case "Keyword-Paper":
                rankItemList=keywordPaper(paperList,descend);
                break;
             default:
                 break;
        }
        int len = rankItemList.size();
        ResponseVO responseVO;
        if(pageNumber*rankMsg.getEachNum()-len>rankMsg.getEachNum()){
            responseVO = ResponseVO.fail(rankMsg.getMismatchPageNumber());
            return responseVO;
        }
        int totalPage = len%rankMsg.getEachNum()==0? len/rankMsg.getEachNum():len/rankMsg.getEachNum()+1;
        int resultStart = (pageNumber-1)*rankMsg.getEachNum();
        int resultLen = len-resultStart>=rankMsg.getEachNum()?rankMsg.getEachNum():len-resultStart;
        List<RankItem> result = rankItemList.subList(resultStart,resultStart+resultLen);
        responseVO = ResponseVO.success();
        responseVO.setContent(new RankVO(totalPage,result));
        return responseVO;
    }

    private List<RankItem> paperCited(List<Paper> paperList, boolean descend){
        return descend? paperList.stream().sorted(Comparator.comparingInt(Paper::getCitation).reversed())
                 .map(paper -> new RankItem(paper.getTitle(), paper.getCitation())).collect(Collectors.toList())
        :paperList.stream().sorted(Comparator.comparingInt(Paper::getCitation))
                 .map(paper -> new RankItem(paper.getTitle(), paper.getCitation())).collect(Collectors.toList());
    }

    private List<RankItem> authorCited(List<Paper> paperList, boolean descend){
        List<AuthorPaperCitationNum> authorPaperCitationNumList =  getAuthorPaperCitationNumList(paperList);
        Map<String, Long> authorCitedNums = authorPaperCitationNumList.stream().collect(Collectors.groupingBy(AuthorPaperCitationNum::getAuthor,Collectors.summingLong(AuthorPaperCitationNum::getPaperCitation)));
        return mapToList(authorCitedNums, descend);
    }

    private List<RankItem> authorPaper(List<Paper> paperList, boolean descend){
        List<AuthorPaperCitationNum> authorPaperCitationNumList =  getAuthorPaperCitationNumList(paperList);
        Map<String, Long> authorPaperNums = authorPaperCitationNumList.stream().collect(Collectors.groupingBy(AuthorPaperCitationNum::getAuthor,Collectors.counting()));
        return mapToList(authorPaperNums, descend);
    }

    private List<RankItem> affiliationPaper(List<Paper> paperList, boolean descend){
        List<Affiliation> affiliationList = paperList.stream().flatMap(paper -> paper.getAa().stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()->
                new TreeSet<>(Comparator.comparing(a->a.getAffiliation().getName()+";"+a.getAffiliation().getCountry()))),ArrayList::new)).stream()
                                                .map(Author_Affiliation::getAffiliation)).collect(Collectors.toList());
        Map<String,Long> affiliationPaperNums = affiliationList.stream().collect(Collectors.groupingBy(Affiliation::getName,Collectors.counting()));
        return mapToList(affiliationPaperNums,descend);
    }

    private List<RankItem> publicationPaper(List<Paper> paperList, boolean descend){
        List<Conference> conferenceList = paperList.stream().map(Paper::getConference).collect(Collectors.toList());
        Map<String,Long> publicationPaperNums = conferenceList.stream().collect(Collectors.groupingBy(Conference::buildName,Collectors.counting()));
        return mapToList(publicationPaperNums,descend);
    }

    private List<RankItem> keywordPaper(List<Paper> paperList, boolean descend){
        List<Term> termList = paperList.stream().flatMap(paper -> paper.getAuthor_keywords().stream()).collect(Collectors.toList());
        termList.addAll(paperList.stream().flatMap(paper -> paper.getIeee_terms().stream()).collect(Collectors.toList()));
        termList.addAll(paperList.stream().flatMap(paper -> paper.getInspec_controlled().stream()).collect(Collectors.toList()));
        termList.addAll(paperList.stream().flatMap(paper -> paper.getInspec_non_controlled().stream()).collect(Collectors.toList()));
        termList.addAll(paperList.stream().flatMap(paper -> paper.getMesh_terms().stream()).collect(Collectors.toList()));
        Map<String,Long> termPaperNums = termList.stream().collect(Collectors.groupingBy(Term::getContent,Collectors.counting()));
        return mapToList(termPaperNums,descend);
    }

    //从Paper的list转为AuthorPaperCitationNum的List
    private  List<AuthorPaperCitationNum> getAuthorPaperCitationNumList(List<Paper> paperList){
        return paperList.stream().
                flatMap(paper -> paper.getAa().stream().
                        map(author_affiliation -> new AuthorPaperCitationNum(author_affiliation.getAuthor().getName(),paper.getCitation())))
                .collect(Collectors.toList());
    }

    //将Map转为RankItem的List
    private List<RankItem> mapToList(Map<String, Long> map, boolean descend){
        List<RankItem> rankItemList =  map.entrySet().stream().sorted(Comparator.comparingLong(Map.Entry::getValue))
                .map(e->new RankItem(e.getKey(),e.getValue().intValue())).collect(Collectors.toList());
        if(descend) Collections.reverse(rankItemList);
        return rankItemList;
    }
}
