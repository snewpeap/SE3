package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.manage;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.service.manage.AdminService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasItem;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import edu.nju.se.teamnamecannotbeempty.data.domain.Aliasable;
import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAffiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAuthor;
import edu.nju.se.teamnamecannotbeempty.data.domain.IDuplication;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAuthorDao;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final DuplicateAuthorDao duplicateAuthorDao;
    private final DuplicateAffiliationDao duplicateAffiliationDao;
    private final AuthorDao authorDao;
    private final AffiliationDao affiliationDao;
    private final EntityMsg entityMsg;
    public static final int SIZE = 10;

    @Autowired
    public AdminServiceImpl(DuplicateAuthorDao duplicateAuthorDao, DuplicateAffiliationDao duplicateAffiliationDao, EntityMsg entityMsg, AuthorDao authorDao, AffiliationDao affiliationDao) {
        this.duplicateAuthorDao = duplicateAuthorDao;
        this.duplicateAffiliationDao = duplicateAffiliationDao;
        this.entityMsg = entityMsg;
        this.authorDao = authorDao;
        this.affiliationDao = affiliationDao;
    }

    @Override
    public List<AliasVO> getDataError(int page, int type) {
        return getData(page, type, false);
    }

    @Override
    public List<AliasVO> getDataOperated(int page, int type) {
        return getData(page, type, true);
    }

    private List<AliasVO> getData(int page, int type, boolean clear) {
        List<AliasVO> vos = Collections.emptyList();
        if (type == entityMsg.getAuthorType()) {
            List<DuplicateAuthor> dups = new ArrayList<>();
            duplicateAuthorDao.findIdsPage(SIZE * page, SIZE, clear).forEach(l -> dups.addAll(duplicateAuthorDao.findBySon_IdAndClear(l, clear)));
            vos = getAliasVOs(dups, type);
        } else if (type == entityMsg.getAffiliationType()) {
            List<DuplicateAffiliation> dups = new ArrayList<>();
            duplicateAffiliationDao.findIdsPage(SIZE * page, SIZE, clear).forEach(l -> dups.addAll(duplicateAffiliationDao.findBySon_IdAndClear(l, clear)));
            vos = getAliasVOs(dups, type);
        }
        return vos;
    }

    <T extends Aliasable<T>> ArrayList<AliasVO> getAliasVOs(List<? extends IDuplication<T>> iter, int type) {
        HashSetValuedHashMap<AliasVO, AliasItem> projection = new HashSetValuedHashMap<>();
        iter.forEach(dup -> {
            AliasVO vo = new AliasVO(dup.getSon().getName(), dup.getSon().getId(), type, null);
            T actual = dup.getFather().getActual();
            projection.put(vo, new AliasItem(actual.getId(), actual.getName()));
        });
        ArrayList<AliasVO> vos = new ArrayList<>(projection.size());
        projection.keySet().forEach(p -> vos.add(new AliasVO(p.getName(), p.getSonId(), type, new ArrayList<>(projection.get(p)))));
        return vos;
    }

    @Override
    @CacheEvict(value = {"papersByYear", "getRank", "getPopRank", "getAcademicEntity",
            "getBasicGraph", "getCompleteGraph", "getSignificantPaper"},
            allEntries = true)
    public ResponseVO operateDataAlias(long sonId, long fatherId, int type) {
        ResponseVO responseVO = new ResponseVO(true, "OK", null);
        if (type == entityMsg.getAuthorType() && duplicateAuthorDao.existsByFather_IdAndSon_Id(fatherId, sonId)) {
            duplicateAuthorDao.findBySon_Id(sonId).forEach(dup -> {
                dup.setClear(true);
                duplicateAuthorDao.save(dup);
            });
            authorDao.findById(sonId).ifPresent(author -> {
                author.setAlias(authorDao.findById(fatherId).orElse(null));
                authorDao.saveAndFlush(author);
            });
        } else if (type == entityMsg.getAffiliationType() && duplicateAffiliationDao.existsByFather_IdAndSon_Id(fatherId, sonId)) {
            duplicateAffiliationDao.findBySon_Id(sonId).forEach(dup -> {
                dup.setClear(true);
                duplicateAffiliationDao.save(dup);
            });
            affiliationDao.findById(sonId).ifPresent(affi -> {
                affi.setAlias(affiliationDao.findById(fatherId).orElse(null));
                affiliationDao.save(affi);
            });
        } else {
            responseVO.setSuccess(false);
            responseVO.setMessage("No such record with type " + type);
        }
        return responseVO;
    }

    @Override
    @CacheEvict(value = {"papersByYear", "getRank", "getPopRank", "getAcademicEntity", "getBasicGraph", "getCompleteGraph"},
            allEntries = true)
    public ResponseVO undoOperate(long sonId, int type) {
        ResponseVO responseVO = new ResponseVO(true, "OK", null);
        if (type == entityMsg.getAuthorType()) {
            duplicateAuthorDao.findBySon_Id(sonId).forEach(dup -> {
                dup.setClear(false);
                duplicateAuthorDao.save(dup);
            });
            authorDao.findById(sonId).ifPresent(author -> {
                author.setAlias(null);
                authorDao.saveAndFlush(author);
            });
        } else if (type == entityMsg.getAffiliationType()) {
            duplicateAffiliationDao.findBySon_Id(sonId).forEach(dup -> {
                dup.setClear(false);
                duplicateAffiliationDao.save(dup);
            });
            affiliationDao.findById(sonId).ifPresent(affi -> {
                affi.setAlias(null);
                affiliationDao.save(affi);
            });
        } else {
            responseVO.setSuccess(false);
            responseVO.setMessage("No such record with type " + type);
        }
        return responseVO;
    }
}
