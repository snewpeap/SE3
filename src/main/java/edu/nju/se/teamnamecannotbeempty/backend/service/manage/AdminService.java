package edu.nju.se.teamnamecannotbeempty.backend.service.manage;

import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;

import java.util.List;

public interface AdminService {

    List<AliasVO> getDataError();

    ResponseVO operateDataAlias(long recordId, boolean isAlias, int type);

    List<AliasVO> getDataOperated();

    ResponseVO undoOperate(long recordId, int type);
}
