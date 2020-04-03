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
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAuthorDao;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private final DuplicateAuthorDao duplicateAuthorDao;
    private final DuplicateAffiliationDao duplicateAffiliationDao;
    private EntityMsg entityMsg;

    @Autowired
    public AdminServiceImpl(DuplicateAuthorDao duplicateAuthorDao, DuplicateAffiliationDao duplicateAffiliationDao, EntityMsg entityMsg) {
        this.duplicateAuthorDao = duplicateAuthorDao;
        this.duplicateAffiliationDao = duplicateAffiliationDao;
        this.entityMsg = entityMsg;
    }

    @Override
    public List<AliasVO> getDataError() {
        ArrayList<AliasVO> vos = getAliasVOs(duplicateAuthorDao.findByClearIs(false), entityMsg.getAuthorType());
        vos.addAll(getAliasVOs(duplicateAffiliationDao.findByClearIs(false), entityMsg.getAffiliationType()));
        return vos;
    }

    @Override
    public List<AliasVO> getDataOperated() {
        ArrayList<AliasVO> vos = getAliasVOs(duplicateAuthorDao.findByClearIs(true), entityMsg.getAuthorType());
        vos.addAll(getAliasVOs(duplicateAffiliationDao.findByClearIs(true), entityMsg.getAffiliationType()));
        return vos;
    }

    private <T extends Aliasable<T>> ArrayList<AliasVO> getAliasVOs(Streamable<? extends IDuplication<T>> iter, int type) {
        ArrayListValuedHashMap<AliasVO, AliasItem> projection = new ArrayListValuedHashMap<>();
        iter.forEach(dup -> {
            AliasVO vo = new AliasVO(dup.getSon().getName(), dup.getSon().getId(), type, null);
            Aliasable<T> aliasable = dup.getFather().getActual();
            projection.put(vo, new AliasItem(dup.getId(), aliasable.getId(), aliasable.getName()));
        });
        ArrayList<AliasVO> vos = new ArrayList<>(projection.size());
        projection.keys().forEach(p -> vos.add(new AliasVO(p.getOrigin(), p.getOriginId(), type, projection.get(p))));
        return vos;
    }

    @Override
    public ResponseVO operateDataAlias(long recordId, boolean isAlias, int type) {
        ResponseVO responseVO = new ResponseVO(true, "OK", null);
        if (type == entityMsg.getAuthorType()) {
            Optional<DuplicateAuthor> result = duplicateAuthorDao.findById(recordId);
            if (result.isPresent()) {
                DuplicateAuthor dup = result.get();
                duplicateAuthorDao.saveAndFlush((DuplicateAuthor) modifyDup(dup, isAlias ? dup.getFather() : dup.getSon()));
            }
        } else if (type == entityMsg.getAffiliationType()) {
            Optional<DuplicateAffiliation> result = duplicateAffiliationDao.findById(recordId);
            if (result.isPresent()) {
                DuplicateAffiliation dup = result.get();
                duplicateAffiliationDao.saveAndFlush((DuplicateAffiliation) modifyDup(dup, isAlias ? dup.getFather() : dup.getSon()));
            }
        } else {
            responseVO.setSuccess(false);
            responseVO.setMessage("No such record with type " + type);
        }
        return responseVO;
    }

    @Override
    public ResponseVO undoOperate(long recordId, int type) {
        ResponseVO responseVO = new ResponseVO(true, "OK", null);
        if (type == entityMsg.getAuthorType()) {
            Optional<DuplicateAuthor> result = duplicateAuthorDao.findById(recordId);
            if (result.isPresent()) {
                DuplicateAuthor dup = result.get();
                duplicateAuthorDao.saveAndFlush((DuplicateAuthor) modifyDup(dup, null));
            }
        } else if (type == entityMsg.getAffiliationType()) {
            Optional<DuplicateAffiliation> result = duplicateAffiliationDao.findById(recordId);
            if (result.isPresent()) {
                DuplicateAffiliation dup = result.get();
                duplicateAffiliationDao.saveAndFlush((DuplicateAffiliation) modifyDup(dup, null));
            }
        } else {
            responseVO.setSuccess(false);
            responseVO.setMessage("No such record with type " + type);
        }
        return responseVO;
    }

    private <T extends Aliasable<T>> IDuplication<T> modifyDup(IDuplication<T> dup, T alias) {
        dup.getSon().setAlias(alias);
        dup.setClear(alias != null);
        return dup;
    }
}
