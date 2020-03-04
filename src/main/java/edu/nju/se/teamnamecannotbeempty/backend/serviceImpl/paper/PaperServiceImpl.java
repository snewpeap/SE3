package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.paper;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.PaperMsg;
import edu.nju.se.teamnamecannotbeempty.backend.dao.PaperDao;
import edu.nju.se.teamnamecannotbeempty.backend.po.Author_Affiliation;
import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import edu.nju.se.teamnamecannotbeempty.backend.po.Term;
import edu.nju.se.teamnamecannotbeempty.backend.service.paper.PaperService;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchService;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SortMode;
import edu.nju.se.teamnamecannotbeempty.backend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaperServiceImpl implements PaperService {

    private final SearchService searchService;
    private final PaperDao paperDao;
    private final PaperMsg paperMsg;

    @Autowired
    public PaperServiceImpl(PaperDao paperDao, PaperMsg paperMsg, SearchService searchService) {
        this.paperDao = paperDao;
        this.paperMsg = paperMsg;
        this.searchService = searchService;
    }

    @Override
    public List<SimplePaperVO> search(String text, String mode, Integer pageNumber, String sortMode, int perPage) {
        SearchMode searchMode = (SearchMode)ApplicationContextUtil.getBean(mode);
        Pageable pageable = PageRequest.of(pageNumber-1,perPage);
        SortMode sort = (SortMode)ApplicationContextUtil.getBean(sortMode);
        Page<Paper> paperPage = searchService.search(text,searchMode,pageable,sort);
        List<Paper> paperList = paperPage.getContent();
        List<SimplePaperVO> simplePaperVOList = new ArrayList<>();
        for (Paper paper:paperList){
            List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS = new ArrayList<>();
            List<Author_Affiliation> author_affiliations = paper.getAa();
            for(Author_Affiliation author_affiliation:author_affiliations){
                author_simpleAffiliationVOS.add(new Author_SimpleAffiliationVO(author_affiliation.getAuthor().getName(),
                        author_affiliation.getAffiliation().getName()));
            }
            List<String> keywords = new ArrayList<>();
            List<Term> termList = paper.getAuthor_keywords();
            for(Term term:termList){
                keywords.add(term.getContent());
            }
            simplePaperVOList.add(new SimplePaperVO(paper.getId(),paper.getTitle(), author_simpleAffiliationVOS, paper.getConference().getName(), keywords));
        }
        return simplePaperVOList;
    }


    @Override
    @Transactional
    public ResponseVO getPaper(long id) {
        Optional<Paper> optionalPaper = paperDao.findById(id);
        ResponseVO responseVO;
        if(!optionalPaper.isPresent()){
            responseVO = ResponseVO.fail();
            responseVO.setMessage(paperMsg.getMismatchId());
            return responseVO;
        }
        Paper paper = optionalPaper.get();
        List<Author_AffiliationVO> author_affiliationVOS = new ArrayList<>();
        List<Author_Affiliation> author_affiliations = paper.getAa();
        for(Author_Affiliation author_affiliation:author_affiliations){
            author_affiliationVOS.add(new Author_AffiliationVO(author_affiliation.getAuthor().getName(),
                    author_affiliation.getAffiliation().getName(), author_affiliation.getAffiliation().getCountry()));
        }
        List<Term> termList_keywords = paper.getAuthor_keywords();
        List<Term> termList_IEEE = paper.getIeee_terms();
        List<Term> termList_control = paper.getInspec_controlled();
        List<Term> termList_noncontrol = paper.getInspec_non_controlled();
        List<String> keywords;
        List<String> ieees;
        List<String> controls;
        List<String> noncontrols;
        keywords = termList_keywords.stream().map(Term::getContent).collect(Collectors.toList());
        ieees = termList_IEEE.stream().map(Term::getContent).collect(Collectors.toList());
        controls = termList_control.stream().map(Term::getContent).collect(Collectors.toList());
        noncontrols = termList_noncontrol.stream().map(Term::getContent).collect(Collectors.toList());
        responseVO = ResponseVO.success();
        responseVO.setContent(new PaperVO(paper.getId(),paper.getTitle(),author_affiliationVOS,paper.getConference().getName(),paper.getConference().getYear(),paper.getStart_page(),
                paper.getEnd_page(),paper.getSummary(),paper.getDoi(),paper.getPdf_link().toString(),keywords,ieees,controls,
                noncontrols,paper.getCitation(),paper.getReference(),paper.getPublisher(),paper.getDocument_identifier()));
        return responseVO;
    }
}
