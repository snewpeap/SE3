package edu.nju.se.teamnamecannotbeempty.backend.service.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;

import java.util.List;

public interface EntityService {

    /**
     * 获取学术实体
     * @param id 学术实体ID
     * @param type 学术实体种类
     * @return AcademicEntityVO 学术实体的VO
     */
    AcademicEntityVO getAcademicEntity(long id, int type);

    /**
     * 获取学术实体的基础关系图（限定了连接距离，好像是2）
     * @param id 学术实体ID
     * @param type 学术实体种类
     * @return GraphVO 关系图的VO
     */
    GraphVO getBasicGraph(long id, int type);

    /**
     * 获取学术实体的完整的关系图
     * @param id 学术实体ID
     * @param type 学术实体种类
     * @return GraphVO 关系图的VO
     */
    GraphVO getCompleteGraph(long id, int type);

    /**
     * 获取学术实体代表作
     * @param year 对应的年份或者所有年份, -1代表全部年份
     * @param termId 对应的研究兴趣ID， -1l代表全部研究兴趣
     * @return 代表作列表
     */
    List<SimplePaperVO> getSignificantPaper(long id, int type, int year, long termId);

}
