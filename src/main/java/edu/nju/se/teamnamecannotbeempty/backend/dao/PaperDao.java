package edu.nju.se.teamnamecannotbeempty.backend.dao;

import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaperDao extends CrudRepository<Paper, Long> {
    /**
     * 通过论文的id来获取对应的论文
     * @param id 要查找的论文id
     * @return 通过Optional包装的Paper对象
     * @前置条件 参数id不为null
     * @后置条件 如果有与参数所给的id对应的论文数据，则Optional.get可获得该对象；否则Optional.empty==true
     */
    Optional<Paper> findById(Long id);

    /**
     * 使用一定的分页条件获得所有论文数据的某页（好像用不上）
     * @param pageable 分页条件
     * @return 所有论文数据的某页
     * @前置条件 参数pageable不为null
     * @后置条件 按照pageable所指定的条件返回所需结果页
     */
    Page<Paper> findAll(Pageable pageable);
}
