package edu.nju.se.teamnamecannotbeempty.backend.service.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;

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
}
