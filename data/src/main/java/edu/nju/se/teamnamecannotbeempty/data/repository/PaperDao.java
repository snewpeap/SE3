package edu.nju.se.teamnamecannotbeempty.data.repository;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaperDao extends JpaRepository<Paper, Long> {
    /**
     * 通过论文的id来获取对应的论文
     *
     * @param id 要查找的论文id
     * @return 通过Optional包装的Paper对象
     * @throws org.springframework.dao.InvalidDataAccessApiUsageException，如果id为null
     * @前置条件 参数id不为null
     * @后置条件 如果有与参数所给的id对应的论文数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     */
    Optional<Paper> findById(Long id);

    /**
     * 使用一定的分页条件获得所有论文数据的某页（好像用不上）
     *
     * @param pageable 分页条件
     * @return 所有论文数据的某页
     * @前置条件 参数pageable不为null
     * @后置条件 按照pageable所指定的条件返回所需结果页
     */
    Page<Paper> findAll(Pageable pageable);

    /**
     * 查找在给定会议年份（也即发表年份）区间内的论文
     *
     * @param from 开始年份
     * @param to   截止年份
     * @return 符合条件的论文列表
     * @throws org.springframework.dao.InvalidDataAccessApiUsageException，如果任意参数为null
     * @apiNote 如果from大于true，不会抛出异常，会返回空列表
     */
    @Cacheable(value = "papersByYear", key = "#root.args[0]+'_'+#root.args[1]", unless = "#result = null")
    List<Paper> findAllByConference_YearBetween(Integer from, Integer to);

    @Query("select p from Paper p")
    Streamable<Paper> streamAll();

    /**
     * 获得作者的被引总数
     *
     * @param id 作者id
     * @return 作者的论文的被引总数
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select sum(p.citation) from Paper p inner join p.aa aa where aa.author.id = ?1")
    long getCitationByAuthorId(Long id);

    /**
     * 获得机构的被引总数
     *
     * @param id 机构id
     * @return 机构的论文的被引总数
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select sum(p.citation) from Paper p inner join p.aa aa where aa.affiliation.id = ?1")
    long getCitationByAffiId(Long id);

    @Query("select p from Paper p where exists (select 1 from p.author_keywords ak where ak.id = ?1)")
    List<Paper> getPapersByKeyword(Long id);
}
