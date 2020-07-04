package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SignificantPaperFetch {

    private final PaperDao paperDao;
    private final EntityMsg entityMsg;
    private final FetchForCache fetchForCache;
    private final AcademicEntityFetch academicEntityFetch;

    @Autowired
    public SignificantPaperFetch(PaperDao paperDao, EntityMsg entityMsg,
                                 FetchForCache fetchForCache,
                                 AcademicEntityFetch academicEntityFetch) {
        this.paperDao = paperDao;
        this.entityMsg = entityMsg;
        this.fetchForCache = fetchForCache;
        this.academicEntityFetch = academicEntityFetch;
    }


    @Cacheable(value = "getSignificantPaper", key = "#p0+'_'+#p1+'_'+#p2+'_'+#p3")
    public List<SimplePaperVO> getSignificantPaper(long id, int type, int year, long termId) {
        if (type == entityMsg.getAuthorType()) return getPapersOfAuthor(id,year,termId);
        else if (type == entityMsg.getAffiliationType()) return getPapersOfAffi(id,year,termId);
        else if (type == entityMsg.getConferenceType()) return getPapersOfConference(id,year,termId);
        return new ArrayList<>(1);
    }

    private List<SimplePaperVO> getPapersOfAuthor(long id, int year, long termId){
        //获取作者的别名列表
        List<Long> aliasIdList = academicEntityFetch.getAllAliasIdsOfAuthor(id, new ArrayList<>()).stream().distinct()
                .collect(Collectors.toList());
        //获取作者的全部论文
        List<Paper> allPapers=new ArrayList<>();
        aliasIdList.forEach(aliasId-> allPapers.addAll(paperDao.findByAuthorId(aliasId)));
        return convertPaper(allPapers,year,termId);
    }

    private List<SimplePaperVO> getPapersOfAffi(long id, int year, long termId){
        //获取机构的别名列表
        List<Long> aliasIdList = academicEntityFetch.getAllAliasIdsOfAffi(id, new ArrayList<>()).stream().distinct()
                .collect(Collectors.toList());
        //获取机构的全部论文
        List<Paper> allPapers=new ArrayList<>();
        aliasIdList.forEach(aliasId-> allPapers.addAll(paperDao.findByAffiId(aliasId)));
        return convertPaper(allPapers,year,termId);
    }

    private List<SimplePaperVO> getPapersOfConference(long id, int year, long termId){
        return convertPaper(paperDao.findByConferenceID(id),year,termId);
    }

    private List<SimplePaperVO> convertPaper(List<Paper> allPapers, int year, long termId){
        allPapers = allPapers.stream().distinct().collect(Collectors.toList());
        if(year==-1&&termId==-1) return allPapers.stream().
                map(SimplePaperVO::new).collect(Collectors.toList());
        if(termId==-1) return allPapers.stream().filter(paper -> paper.getYear()==year).map(
                SimplePaperVO::new).collect(Collectors.toList());
        if(year==-1) return allPapers.stream().filter(
                paper -> fetchForCache.getTermPopByPaperID(paper.getId()).stream().map(
                        termPop->termPop.getTerm().getId()).
                        collect(Collectors.toList()).contains(termId)).map(
                SimplePaperVO::new).collect(Collectors.toList());
        return allPapers.stream().filter(paper -> paper.getYear()==year).filter(
                paper -> fetchForCache.getTermPopByPaperID(paper.getId()).stream().map(
                        termPop->termPop.getTerm().getId()).
                        collect(Collectors.toList()).contains(termId)).map(
                SimplePaperVO::new).collect(Collectors.toList());
    }
}
