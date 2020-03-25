package edu.nju.se.teamnamecannotbeempty.backend.dao;

import edu.nju.se.teamnamecannotbeempty.backend.po.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConferenceDao extends JpaRepository<Conference, Long> {
    /**
     * 通过会议名和会议年份获取一条会议记录
     * @param name 会议名，取自"ASE"或"ICSE"
     * @param year 年份
     * @return 用Optional包装的会议对象
     * @前置条件 参数都不为null
     * @后置条件 如果有与参数所给的id对应的论文数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     */
    Optional<Conference> findByNameAndYear(String name, Integer year);
}
