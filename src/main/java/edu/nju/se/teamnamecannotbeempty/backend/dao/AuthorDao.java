package edu.nju.se.teamnamecannotbeempty.backend.dao;

import edu.nju.se.teamnamecannotbeempty.backend.po.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorDao extends JpaRepository<Author, Long> {
    /**
     * 通过名字获取一条作者记录
     * @param name 作者名字
     * @return 通过Optional包装的Author对象
     * @前置条件 name不为null
     * @后置条件 如果有与参数对应的数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     */
    Optional<Author> findByName(String name);

    /**
     * 通过名字获取一条作者记录
     * @param id 作者id
     * @return 通过Optional包装的Author对象
     * @前置条件 id不为null
     * @后置条件 如果有与参数对应的数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     * @throws org.springframework.dao.InvalidDataAccessApiUsageException，如果id为null
     */
    Optional<Author> findById(Long id);
}
