package edu.nju.se.teamnamecannotbeempty.data.repository.popularity;

import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TermPopDao extends JpaRepository<Term.Popularity, Long> {
    /**
     * 获得按照热度（活跃度）降序排列的前20名
     *
     * @return 返回至多20条记录，按照热度（活跃度）排序
     * @前置条件 无
     * @后置条件 无
     */
    List<Term.Popularity> findTop20ByOrderByPopularityDesc();

    /**
     * 查询一个作者的研究方向及其热度
     * 一个作者的研究方向，是指他发表的论文的研究方向集合之并集
     *
     * @param id 作者id
     * @return 作者的研究方向热度对象列表
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select distinct tp from term_popularity tp where exists (" +
            "select p from Paper p inner join p.author_keywords pas inner join p.aa aa " +
            "where tp.term.id = pas.id and aa.author.id = ?1)")
    List<Term.Popularity> getTermPopByAuthorID(Long id);

    /**
     * 查询一个机构的研究方向及其热度
     * 一个机构的研究方向，是指机构名下的论文的研究方向集合之并集
     *
     * @param id 作者id
     * @return 机构的研究方向热度对象列表
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select distinct tp from term_popularity tp where exists (" +
            "select p from Paper p inner join p.author_keywords pas inner join p.aa aa " +
            "where tp.term.id = pas.id and aa.affiliation.id = ?1)")
    List<Term.Popularity> getTermPopByAffiID(Long id);

    /**
     * 查询一篇论文的研究方向及其热度
     *
     * @param id 论文id
     * @return 论文的研究方向热度对象列表
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select distinct tp from term_popularity tp where exists (" +
            "select p from Paper p inner join p.author_keywords pas " +
            "where tp.term.id = pas.id and p.id = ?1)")
    List<Term.Popularity> getTermPopByPaperID(Long id);

    /**
     * 查询会议的研究方向及其热度
     *
     * @param id 会议id
     * @return 会议的研究方向热度对象列表
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select distinct tp from term_popularity tp where exists (" +
            "select p from Paper p inner join p.author_keywords pas " +
            "where tp.term.id = pas.id and p.conference.id = ?1)")
    List<Term.Popularity> getTermPopByConferenceID(Long id);

    /**
     * 获取一个研究方向的热度
     *
     * @param id 研究方向id
     * @return 通过Optional包装的热度对象
     * @前置条件 id不为null
     * @后置条件 如果有与参数所给的id对应的数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     */
    Optional<Term.Popularity> getDistinctByTerm_Id(Long id);
}
