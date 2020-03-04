package edu.nju.se.teamnamecannotbeempty.backend.service.paper;

import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;

import java.util.List;

/**
 *定义论文检索相关的业务逻辑接口
 * @author 赖宝光
 * @date 2020.2.25
 */
public interface PaperService {

    /**
     *根据用户的检索条件返回所需的结果列表
     * @param text 用户输入的检索字段
     * @param mode 用户选择的搜索字段类型:'All','Title','Author','Affiliation','Publication','Keyword'
     * @param pageNumber 发出请求的页号
     * @param sortMode 用户需要的结果列表的排序方式:'Relevance','Newest','Oldest','Title A-Z','Title Z-A'
     * @param perPage 每页的显示数量
     * @return List<SimplePaperVO> 一个论文简略信息的VO对象的List
     */
    List<SimplePaperVO> search(String text, String mode, Integer pageNumber, String sortMode, int perPage);

    /**
     * 根据id返回用户所要查看的论文的详细信息
     * @param id 论文id
     * @return PaperVO 论文详细信息的VO对象
     */
    ResponseVO getPaper(long id);
}
