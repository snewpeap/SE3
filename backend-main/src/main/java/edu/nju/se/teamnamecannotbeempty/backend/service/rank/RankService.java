package edu.nju.se.teamnamecannotbeempty.backend.service.rank;


import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;

/**
 * 定义排名相关的业务逻辑接口
 *
 * @author 赖宝光
 * @date 2020.2.25
 */
public interface RankService {

    /**
     * 返回用户所需查看的排行信息
     *
     * @param mode       用户请求的排行类型:'Paper-Cited', 'Author-Cited', 'Author-Paper', 'Affiliation-Paper', 'Publication-Paper', 'Keyword-Paper'
     * @param pageNumber 发出请求的页号
     * @param descend    返回的排行是否降序排序
     * @param startYear  参与排行的对象不得早于该年份
     * @param endYear    参与排行的对象不得晚于该年份
     * @return RankVO 返回的排行对象VO，包含一个列表
     */
    ResponseVO getRank(String mode, Integer pageNumber, boolean descend, int startYear, int endYear);
}
