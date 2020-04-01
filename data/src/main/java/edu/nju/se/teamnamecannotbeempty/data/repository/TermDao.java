package edu.nju.se.teamnamecannotbeempty.data.repository;

import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TermDao extends JpaRepository<Term, Long> {
    Optional<Term> findByContent(String content);

    /**
     * 通过术语文本匹配术语，且忽略大小写
     *
     * @param content 术语内容
     * @return Optional包装的术语对象
     * @前置条件 content不为null
     * @后置条件 如果有与参数所给的string对应的数据，则Optional.get可获得该对象；否则Optional.isPresent==false
     */
    Optional<Term> findByContentIgnoreCase(String content);

    /**
     * 获取所有的研究方向（也即作者关键字）
     *
     * @return 作者关键字迭代器
     * @前置条件 无
     * @后置条件 无
     */
    @Query("select distinct p.author_keywords from Paper p")
    Streamable<Term> findAllAuthorKeywords();
}
