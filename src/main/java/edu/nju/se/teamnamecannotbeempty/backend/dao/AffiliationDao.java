package edu.nju.se.teamnamecannotbeempty.backend.dao;

import edu.nju.se.teamnamecannotbeempty.backend.po.Affiliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AffiliationDao extends JpaRepository<Affiliation, Long> {
    /**
     * 通过名字获取一条机构记录
     * @param name 机构名字
     * @return 通过Optional包装的Affiliation对象
     * @前置条件 name不为null
     * @后置条件 如果有与参数对应的数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     */
    Optional<Affiliation> findByName(String name);

    /**
     * 通过id获取一条机构记录
     * @param id 机构id
     * @return 通过Optional包装的Affiliation对象
     * @前置条件 id不为null
     * @后置条件 如果有与参数对应的数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     * @throws org.springframework.dao.InvalidDataAccessApiUsageException，如果id为null
     */
    Optional<Affiliation> findById(Long id);
}
