package edu.nju.se.teamnamecannotbeempty.data.repository;

import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorDao extends JpaRepository<Author, Long> {
    /**
     * 通过名字获取一条作者记录
     *
     * @param name 作者名字
     * @return 通过Optional包装的Author对象
     * @前置条件 name不为null
     * @后置条件 如果有与参数对应的数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     */
    Optional<Author> findByName(String name);

    /**
     * 通过名字获取一条作者记录
     *
     * @param id 作者id
     * @return 通过Optional包装的Author对象
     * @throws org.springframework.dao.InvalidDataAccessApiUsageException，如果id为null
     * @前置条件 id不为null
     * @后置条件 如果有与参数对应的数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     */
    Optional<Author> findById(Long id);

    /**
     * 用机构id查询曾经在机构发表过论文的作者，根据活跃度倒序排序
     *
     * @param id 机构的id
     * @return 在id对应的机构发表过论文的作者
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query(nativeQuery = true,
            value = "select distinct authors.id, lower_case_name, au_name, alias_id, popularity from authors " +
                    "inner join paper_aa on authors.id = paper_aa.author_id inner join author_popularity ap on authors.id = ap.author_id " +
                    "where paper_aa.affiliation_id = ?1 order by popularity desc, au_name")
    List<Author> getAuthorsByAffiliation(Long id);

    /**
     * 查询参加过某会议的作者，根据活跃度倒序排序
     *
     * @param id 会议的id
     * @return 参加过该会议的作者（指发表过文章）
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select distinct aa.author, ap.popularity from Paper p " +
            "inner join p.aa aa inner join author_popularity ap on aa.author.id = ap.author.id " +
            "where p.conference.id = ?1 order by ap.popularity desc")
    List<Author> getAuthorsByConference(Long id);

    /**
     * 查询在某研究方向发表过论文的作者
     *
     * @param id 研究方向id
     * @return 在研究方向上发表过论文的作者
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select distinct aa.author from Paper p inner join p.aa aa " +
            "where exists (select 1 from p.author_keywords ak where ak.id = ?1)")
    List<Author> getAuthorsByKeyword(Long id);

    /**
     * 查询名字相似但不是本人的作者
     *
     * @param like 作者名字模板
     * @param id 排除掉该作者id
     * @return 与该作者名字相似的其他作者
     * @前置条件 参数都不为null
     * @后置条件 无
     */
    List<Author> findByLowerCaseNameIsLikeAndIdIsNot(String like, Long id);

    @Query("select a from Author a")
    Streamable<Author> getAll();
}
