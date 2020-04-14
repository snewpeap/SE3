package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityItem;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.TermItem;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcademicEntityFetch {

    private final AffiliationDao affiliationDao;
    private final AuthorDao authorDao;
    private final ConferenceDao conferenceDao;
    private final PaperDao paperDao;
    private final TermPopDao termPopDao;
    private final EntityMsg entityMsg;
    private final PaperPopDao paperPopDao;
    private final TermDao termDao;

    @Autowired
    public AcademicEntityFetch(AffiliationDao affiliationDao, AuthorDao authorDao, ConferenceDao conferenceDao,
                               PaperDao paperDao, EntityMsg entityMsg,
                               TermPopDao termPopDao, PaperPopDao paperPopDao, TermDao termDao) {
        this.affiliationDao = affiliationDao;
        this.authorDao = authorDao;
        this.conferenceDao = conferenceDao;
        this.paperDao = paperDao;
        this.entityMsg = entityMsg;
        this.termPopDao = termPopDao;
        this.paperPopDao = paperPopDao;
        this.termDao = termDao;
    }

    @Cacheable(value = "getAcademicEntity", key = "#p0+'_'+#p1")
    @Transactional
    public AcademicEntityVO getAcademicEntity(long id, int type) {
        AcademicEntityVO academicEntityVO = null;
        if (type == entityMsg.getAuthorType()) academicEntityVO = authorsEntity(id);
        else if (type == entityMsg.getAffiliationType()) academicEntityVO = affiliationEntity(id);
        else if (type == entityMsg.getConferenceType()) academicEntityVO = conferenceEntity(id);
        return academicEntityVO;
    }

    private AcademicEntityVO authorsEntity(long id) {

        List<Long> aliasIdList = getAllAliasIdsOfAuthor(id, new ArrayList<>()).stream().distinct()
                .collect(Collectors.toList());
        List<Affiliation> affiliationList = aliasIdList.stream().flatMap(aliasId -> affiliationDao.getAffiliationsByAuthor(aliasId).stream())
                .distinct().collect(Collectors.toList());
        List<Conference> conferenceList = aliasIdList.stream().flatMap(aliasId -> conferenceDao.getConferencesByAuthor(aliasId).stream())
                .distinct().collect(Collectors.toList());

        List<AcademicEntityItem> affiEntityItems = generateAffiEntityItems(affiliationList);
        List<AcademicEntityItem> conferenceEntityItems = generateConferenceEntityItems(conferenceList);

        HashMap<Long, Double> termHashMap = new HashMap<>();
        aliasIdList.forEach(aliasId -> termPopDao.getTermPopByAuthorID(aliasId).forEach(
                termPop -> {
                    Long termId = termPop.getTerm().getId();
                    if (termHashMap.containsKey(termId)) {
                        Double d = termHashMap.get(termId);
                        termHashMap.put(termId, d + paperPopDao.getWeightByAuthorOnKeyword(aliasId, termPop.getTerm().getId()));
                    } else {
                        termHashMap.put(termId, paperPopDao.getWeightByAuthorOnKeyword(aliasId, termPop.getTerm().getId()));
                    }
                }));
        List<TermItem> termItems = termHashMap.entrySet().stream().map(
                en -> new TermItem(en.getKey(), termDao.findById(en.getKey()).orElseGet(Term::new).getContent(), en.getValue())
        ).collect(Collectors.toList());

//        List<Term.Popularity> termPopularityList = termPopDao.getTermPopByAuthorID(id);
////        List<TermItem> termItems = termPopularityList.stream().map(
////                termPopularity -> new TermItem(termPopularity.getTerm().getId(), termPopularity.getTerm().getContent(),
////                        paperPopDao.getWeightByAuthorOnKeyword(id, termPopularity.getTerm().getId()))
////        ).collect(Collectors.toList());

        List<Paper.Popularity> paperPopList = aliasIdList.stream().flatMap(aliasId -> paperPopDao.findTopPapersByAuthorId(aliasId).stream())
                .distinct().collect(Collectors.toList());
        List<SimplePaperVO> simplePaperVOS = generateTopPapers(paperPopList);

        int sumCitation = aliasIdList.stream().mapToInt(aliasId -> (int) paperDao.getCitationByAuthorId(aliasId)).sum();

        return new AcademicEntityVO(entityMsg.getAuthorType(), id, authorDao.findById(id).orElseGet(Author::new).getActual().getName(),
                sumCitation, null, affiEntityItems, conferenceEntityItems, termItems,
                simplePaperVOS);
    }

    private AcademicEntityVO affiliationEntity(long id) {

        List<Long> aliasIdList = getAllAliasIdsOfAffi(id, new ArrayList<>()).stream().distinct()
                .collect(Collectors.toList());
        List<Author> authorList = aliasIdList.stream().flatMap(aliasId -> authorDao.getAuthorsByAffiliation(aliasId).stream())
                .distinct().collect(Collectors.toList());
        List<Conference> conferenceList = aliasIdList.stream().flatMap(aliasId -> conferenceDao.getConferencesByAffiliation(aliasId).stream())
                .distinct().collect(Collectors.toList());

        List<AcademicEntityItem> authorEntityItems = generateAuthorEntityItems(authorList);
        List<AcademicEntityItem> conferenceEntityItems = generateConferenceEntityItems(conferenceList);

        HashMap<Long, Double> termHashMap = new HashMap<>();
        aliasIdList.forEach(aliasId -> termPopDao.getTermPopByAffiID(aliasId).forEach(
                termPop -> {
                    Long termId = termPop.getTerm().getId();
                    if (termHashMap.containsKey(termId)) {
                        Double d = termHashMap.get(termId);
                        termHashMap.put(termId, d + paperPopDao.getWeightByAffiOnKeyword(aliasId, termPop.getTerm().getId()));
                    } else {
                        termHashMap.put(termId, paperPopDao.getWeightByAffiOnKeyword(aliasId, termPop.getTerm().getId()));
                    }
                }));
        List<TermItem> termItems = termHashMap.entrySet().stream().map(
                en -> new TermItem(en.getKey(), termDao.findById(en.getKey()).orElseGet(Term::new).getContent(), en.getValue())
        ).collect(Collectors.toList());

//        List<Term.Popularity> termPopularityList = termPopDao.getTermPopByAffiID(id);
//        List<TermItem> termItems = termPopularityList.stream().map(
//                termPopularity -> new TermItem(termPopularity.getTerm().getId(), termPopularity.getTerm().getContent(),
//                        paperPopDao.getWeightByAffiOnKeyword(id, termPopularity.getTerm().getId()))
//        ).collect(Collectors.toList());

        List<Paper.Popularity> paperPopList = aliasIdList.stream().flatMap(aliasId -> paperPopDao.findTopPapersByAffiId(aliasId).stream())
                .distinct().collect(Collectors.toList());
        List<SimplePaperVO> simplePaperVOS = generateTopPapers(paperPopList);

        int sumCitation = aliasIdList.stream().mapToInt(aliasId -> (int) paperDao.getCitationByAffiId(aliasId)).sum();

        return new AcademicEntityVO(entityMsg.getAffiliationType(), id, affiliationDao.findById(id).
                orElseGet(Affiliation::new).getActual().getName(),
                sumCitation, authorEntityItems, null, conferenceEntityItems, termItems,
                simplePaperVOS);
    }

    private AcademicEntityVO conferenceEntity(long id) {

        List<AcademicEntityItem> authorEntityItems = generateAuthorEntityItems(authorDao.getAuthorsByConference(id));
        List<AcademicEntityItem> affiEntityItems = generateAffiEntityItems(affiliationDao.getAffiliationsByConference(id));
        List<Term.Popularity> termPopularityList = termPopDao.getTermPopByConferenceID(id);

        List<TermItem> termItems = termPopularityList.stream().map(
                termPopularity -> new TermItem(termPopularity.getTerm().getId(), termPopularity.getTerm().getContent(),
                        paperPopDao.getWeightByConferenceOnKeyword(id, termPopularity.getTerm().getId()))
        ).collect(Collectors.toList());

        List<SimplePaperVO> simplePaperVOS = generateTopPapers(paperPopDao.findTopPapersByConferenceId(id));

        return new AcademicEntityVO(entityMsg.getConferenceType(), id, conferenceDao.findById(id).
                orElseGet(Conference::new).buildName(), -1,
                authorEntityItems, affiEntityItems, null, termItems, simplePaperVOS);
    }

    private List<AcademicEntityItem> generateAuthorEntityItems(List<Author> authors) {
        List<AcademicEntityItem> academicEntityItems = authors.stream().map(
                author -> new AcademicEntityItem(entityMsg.getAuthorType(), author.getActual().getId(), author.getActual().getName()))
                .collect(Collectors.toList());
        return academicEntityItems.size() > 15 ? academicEntityItems.subList(0, 15) : academicEntityItems;
    }

    private List<AcademicEntityItem> generateAffiEntityItems(List<Affiliation> affiliations) {
        List<AcademicEntityItem> academicEntityItems = affiliations.stream()
                .filter(affiliation -> !affiliation.getActual().getName().equals("NA"))
                .map(
                        affiliation -> new AcademicEntityItem(entityMsg.getAffiliationType(), affiliation.getActual().getId(),
                                affiliation.getActual().getName()))
                .collect(Collectors.toList());
        return academicEntityItems.size() > 15 ? academicEntityItems.subList(0, 15) : academicEntityItems;
    }

    private List<AcademicEntityItem> generateConferenceEntityItems(List<Conference> conferences) {
        List<AcademicEntityItem> academicEntityItems = conferences.stream().map(
                conference -> new AcademicEntityItem(entityMsg.getConferenceType(), conference.getId(), conference.buildName()))
                .collect(Collectors.toList());
        return academicEntityItems.size() > 15 ? academicEntityItems.subList(0, 15) : academicEntityItems;
    }

    private List<SimplePaperVO> generateTopPapers(List<Paper.Popularity> paperPopularityList) {
        List<SimplePaperVO> simplePaperVOS = paperPopularityList.stream()
                .map(paprePopularity -> new SimplePaperVO(paprePopularity.getPaper()))
                .collect(Collectors.toList());
        return simplePaperVOS.size() > 5 ? simplePaperVOS.subList(0, 5) : simplePaperVOS;
    }

    private List<Long> getAllAliasIdsOfAuthor(long id, List<Long> results) {
        results.add(id);
        List<Author> aliasList = authorDao.getByAlias_Id(id);
        if (aliasList == null || aliasList.isEmpty()) {
            return results;
        }
        for (Author author : aliasList) {
            results.addAll(getAllAliasIdsOfAffi(author.getId(), new ArrayList<>()));
        }
        return results;
    }

    private List<Long> getAllAliasIdsOfAffi(long id, List<Long> results) {
        results.add(id);
        List<Affiliation> aliasList = affiliationDao.getByAlias_Id(id);
        if (aliasList == null || aliasList.isEmpty()) {
            return results;
        }
        for (Affiliation affiliation : aliasList) {
            results.addAll(getAllAliasIdsOfAffi(affiliation.getId(), new ArrayList<>()));
        }
        return results;
    }


}
