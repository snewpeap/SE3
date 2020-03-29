package edu.nju.se.teamnamecannotbeempty.backend.dao.popularity;

import edu.nju.se.teamnamecannotbeempty.backend.po.Term;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TermPopDao extends CrudRepository<Term.Popularity, Long> {
    /**
     * 获得按照热度（活跃度）降序排列的前20名
     *
     * @return 返回至多20条记录，按照热度（活跃度）排序
     * @前置条件 无
     * @后置条件 无
     */
    List<Term.Popularity> findTop20ByOrderByPopularityDesc();
}
