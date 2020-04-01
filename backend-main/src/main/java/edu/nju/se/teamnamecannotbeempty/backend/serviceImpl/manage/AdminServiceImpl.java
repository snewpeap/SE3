package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.manage;

import edu.nju.se.teamnamecannotbeempty.backend.service.manage.AdminService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Override
    public List<AliasVO> getDataError() {
        return null;
    }

    @Override
    public ResponseVO operateDataAlias(long recordId, boolean isAlias, int type) {
        return null;
    }

    @Override
    public List<AliasVO> getDataOperated() {
        return null;
    }

    @Override
    public ResponseVO undoOperate(long recordId, int type) {
        return null;
    }
}
