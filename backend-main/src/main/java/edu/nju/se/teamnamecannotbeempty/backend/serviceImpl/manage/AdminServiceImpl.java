package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.manage;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.service.manage.AdminService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasItem;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import edu.nju.se.teamnamecannotbeempty.data.domain.Aliasable;
import edu.nju.se.teamnamecannotbeempty.data.domain.IDuplication;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAuthorDao;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public final class AdminServiceImpl implements AdminService {
    private final DuplicateAuthorDao duplicateAuthorDao;
    private final DuplicateAffiliationDao duplicateAffiliationDao;
    private final AuthorDao authorDao;
    private final AffiliationDao affiliationDao;
    private EntityMsg entityMsg;
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
            vos = getAliasVOs(duplicateAuthorDao.findByClear(clear, PageRequest.of(page, SIZE)), type);
        } else if (type == entityMsg.getAffiliationType()) {
            vos = getAliasVOs(duplicateAffiliationDao.findByClear(clear, PageRequest.of(page, SIZE)), type);
        }
        return vos;
    }

    <T extends Aliasable<T>> ArrayList<AliasVO> getAliasVOs(Streamable<? extends IDuplication<T>> iter, int type) {
        HashSetValuedHashMap<AliasVO, AliasItem> projection = new HashSetValuedHashMap<>();
        iter.forEach(dup -> {
            AliasVO vo = new AliasVO(dup.getSon().getName(), dup.getSon().getId(), type, null);
            T actual = dup.getFather().getActual();
            projection.put(vo, new AliasItem(actual.getId(), actual.getName()));
        });
        ArrayList<AliasVO> vos = new ArrayList<>(projection.size());
        projection.keys().forEach(p -> vos.add(new AliasVO(p.getName(), p.getSonId(), type, new ArrayList<>(projection.get(p)))));
        return vos;
    }

    @Override
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
