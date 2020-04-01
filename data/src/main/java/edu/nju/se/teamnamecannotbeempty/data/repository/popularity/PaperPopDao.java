package edu.nju.se.teamnamecannotbeempty.data.repository.popularity;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaperPopDao extends CrudRepository<Paper.Popularity, Long> {
    /**
     * 获得按照PageRank结果降序排列的前20名
     *
     * @return 返回至多20条记录，按照热度结果排序
     * @前置条件 无
     * @后置条件 无
     */
    List<Paper.Popularity> findTop20ByOrderByPopularityDesc();

    /**
     * 查找某作者的代表作（即按论文热度排序）
     *
     * @param id 作者id
     * @return 按热度降序的作者论文
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select pp from paper_popularity pp inner join pp.paper p inner join p.aa aa " +
            "where aa.author.id = ?1 order by pp.popularity desc")
    List<Paper.Popularity> findTopPapersByAuthorId(Long id);

    /**
     * 查找机构的代表作
     *
     * @param id 机构id
     * @return 按热度降序的机构论文
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select pp from paper_popularity pp inner join pp.paper p inner join p.aa aa " +
            "where aa.affiliation.id = ?1 order by pp.popularity desc")
    List<Paper.Popularity> findTopPapersByAffiId(Long id);

    /**
     * 查找某个会议的最热门文章
     *
     * @param id 会议id
     * @return 按热度降序的会议论文
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select pp from paper_popularity pp where pp.paper.conference.id = ?1 order by pp.popularity desc")
    List<Paper.Popularity> findTopPapersByConferenceId(Long id);

    /**
     * 获得作者所发表论文的热度之和
     *
     * @param id 作者id
     * @return 作者所发表论文的热度之和
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select sum(pp.popularity) from paper_popularity pp inner join pp.paper p inner join p.aa aa " +
            "where aa.author.id = ?1")
    double getPopSumByAuthorId(Long id);

    /**
     * 获得机构的论文热度之和
     *
     * @param id 机构id
     * @return 机构论文热度之和
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select sum(pp.popularity) from paper_popularity pp inner join pp.paper p inner join p.aa aa " +
            "where aa.affiliation.id = ?1")
    double getPopSumByAffiId(Long id);

    /**
     * 获得研究方向的论文热度之和
     *
     * @param id 机构id
     * @return 研究方向论文热度之和
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select sum(pp.popularity) from paper_popularity pp inner join pp.paper p inner join p.author_keywords ak " +
            "where ak.id = ?1")
    double getPopSumByAuthorKeywordId(Long id);
}
