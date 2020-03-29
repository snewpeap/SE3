package edu.nju.se.teamnamecannotbeempty.backend.dao.popularity;

import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaperPopDao extends CrudRepository<Paper.Popularity, Long> {
    /**
     * 获得按照PageRank结果降序排列的前20名
     *
     * @return 返回至多20条记录，按照PageRank结果排序
     * @前置条件 无
     * @后置条件 无
     */
    List<Paper.Popularity> findTop20ByOrderByPopularityDesc();
}
