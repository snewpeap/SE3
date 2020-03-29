package edu.nju.se.teamnamecannotbeempty.backend.service.manage;

import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;

import java.util.List;

public interface AdminService {

    /**
     * 返回管理员未处理过的系统发现的对应可能重名的全部实体（作者，机构）
     *
     * @return List<AliasVO> 可能重名的全部实体
     */
    List<AliasVO> getDataError();

    /**
     * 管理员选择某一条重名记录进行处理
     *
     * @param recordId 重名的实体记录的ID
     * @param isAlias  是否重名
     * @param type     实体类型
     * @return ResponseVO 包含处理是否成功消息
     */
    ResponseVO operateDataAlias(long recordId, boolean isAlias, int type);

    /**
     * 返回管理员处理过的系统发现的对应可能重名的全部实体（作者，机构）
     *
     * @return List<AliasVO> 可能重名的全部实体
     */
    List<AliasVO> getDataOperated();

    /**
     * 管理员选择某一条之前处理过的消息进行撤销操作
     *
     * @param recordId 要撤销的重名实体记录ID
     * @param type     实体类型
     * @return ResponseVO 包含撤销是否成功的消息
     */
    ResponseVO undoOperate(long recordId, int type);
}
