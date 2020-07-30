package edu.nju.se.teamnamecannotbeempty.data.repository.popularity;

import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AffiPopDao extends JpaRepository<Affiliation.Popularity, Long> {
    /**
     * 获得按照热度（活跃度）降序排列的前20名
     *
     * @return 返回至多20条记录，按照热度（活跃度）排序
     * @前置条件 无
     * @后置条件 无
     */
    default List<Affiliation.Popularity> findTop20ByOrderByPopularityDesc() {
        return findTop20ByYearIsNullOrderByPopularityDesc();
    }

    List<Affiliation.Popularity> findTop20ByYearIsNullOrderByPopularityDesc();

    Optional<Affiliation.Popularity> getByAffiliation_IdAndYearIsNull(Long id);

    /**
     * 通过机构ID查找机构某年的热度
     *
     * @param affiId 机构id
     * @param year 年份
     * @return 机构该年度热度（若有）
     */
    Optional<Affiliation.Popularity> getByAffiliation_IdAndYear(Long affiId, Integer year);

    /**
     * 通过机构ID查找该机构的总热度
     *
     * @param id 机构id
     * @return 机构的总热度
     */
    default Optional<Affiliation.Popularity> findByAffiliation_Id(Long id) {
        return getByAffiliation_IdAndYearIsNull(id);
    }

    List<Affiliation.Popularity> getAllByAffiliation_Id(Long id);
}
