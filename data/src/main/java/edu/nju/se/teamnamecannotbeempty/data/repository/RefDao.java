package edu.nju.se.teamnamecannotbeempty.data.repository;

import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefDao extends JpaRepository<Ref, Long> {
    /**
     * 查询被引用者非空的引用记录
     *
     * @return 符合条件的引用记录列表
     */
    List<Ref> findByRefereeIsNotNull();

    /**
     * 使用小写的标题查询被引用论文对象非空的引用关系
     *
     * @param lowercaseTitle 小写的标题
     * @return 符合条件的引用记录
     */
    List<Ref> findByLowercaseTitleEqualsAndRefereeIsNull(String lowercaseTitle);

}
