package edu.nju.se.teamnamecannotbeempty.data.repository;

import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AffiliationDao extends JpaRepository<Affiliation, Long> {
    /**
     * 通过名字获取一条机构记录
     *
     * @param name 机构名字
     * @return 通过Optional包装的Affiliation对象
     * @前置条件 name不为null
     * @后置条件 如果有与参数对应的数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     */
    Optional<Affiliation> findByName(String name);

    /**
     * 通过id获取一条机构记录
     *
     * @param id 机构id
     * @return 通过Optional包装的Affiliation对象
     * @throws org.springframework.dao.InvalidDataAccessApiUsageException，如果id为null
     * @前置条件 id不为null
     * @后置条件 如果有与参数对应的数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     */
    Optional<Affiliation> findById(Long id);

    /**
     * 用作者id查询作者在过的机构（指在机构下发表过论文）
     *
     * @param id 作者的id
     * @return 作者在过的机构
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query(nativeQuery = true,
            value = "select distinct id, country, formatted_name, af_name, alias_id from affiliations " +
                    "inner join paper_aa on affiliations.id = paper_aa.affiliation_id " +
                    "where paper_aa.author_id = ?1")
    List<Affiliation> getAffiliationsByAuthor(Long id);

    /**
     * 查询参加过某会议的机构（有作者在该会议以该机构的名义发表过论文）
     *
     * @param id 会议id
     * @return 参加过会议的机构
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select distinct aa.affiliation from Paper p inner join p.aa aa where p.conference.id = ?1")
    List<Affiliation> getAffiliationsByConference(Long id);

    /**
     * 在某研究方向上发表过论文的机构
     *
     * @param id 研究方向id
     * @return 在该方向上发表过论文的机构
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select distinct aa.affiliation from Paper p inner join p.aa aa " +
            "where exists (select 1 from p.author_keywords ak where ak.id = ?1)")
    List<Affiliation> getAffiliationsByKeyword(Long id);

    @Query("select a from Affiliation a")
    Streamable<Affiliation> getAll();
}
