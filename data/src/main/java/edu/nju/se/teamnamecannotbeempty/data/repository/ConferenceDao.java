package edu.nju.se.teamnamecannotbeempty.data.repository;

import edu.nju.se.teamnamecannotbeempty.data.domain.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConferenceDao extends JpaRepository<Conference, Long> {
    /**
     * 通过会议名和会议年份获取一条会议记录
     *
     * @param name 会议名，取自"ASE"或"ICSE"
     * @param year 年份
     * @return 用Optional包装的会议对象
     * @前置条件 参数都不为null
     * @后置条件 如果有与参数所给的id对应的论文数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     */
    Optional<Conference> findByNameAndYear(String name, Integer year);

    /**
     * 通过作者的id来获取他参加过的会议
     *
     * @param id 作者的id
     * @return 参加过的会议的列表
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select distinct p.conference from Paper p inner join p.aa as aa where aa.author.id = ?1")
    List<Conference> getConferencesByAuthor(Long id);

    /**
     * 通过机构的id来获取机构参加过的会议
     *
     * @param id 机构的id
     * @return 机构参加过的会议的列表
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query("select distinct p.conference from Paper p inner join p.aa as aa where aa.affiliation.id = ?1")
    List<Conference> getConferencesByAffiliation(Long id);
}
