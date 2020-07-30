package edu.nju.se.teamnamecannotbeempty.data.repository.popularity;

import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorPopDao extends JpaRepository<Author.Popularity, Long> {
    /**
     * 获得按照热度（活跃度）降序排列的前20名
     *
     * @return 返回至多20条记录，按照热度（活跃度）排序
     * @前置条件 无
     * @后置条件 无
     */
    default List<Author.Popularity> findTop20ByOrderByPopularityDesc() {
        return findTop20ByYearIsNullOrderByPopularityDesc();
    }

    List<Author.Popularity> findTop20ByYearIsNullOrderByPopularityDesc();

    Optional<Author.Popularity> getByAuthor_IdAndYearIsNull(Long AuthorId);

    /**
     * 通过作者ID查找作者某年的热度
     *
     * @param authorId 作者id
     * @param year 年份
     * @return 作者该年度热度（若有）
     */
    Optional<Author.Popularity> getByAuthor_IdAndYear(Long authorId, Integer year);

    /**
     * 通过作者ID查找作者的总热度
     *
     * @param AuthorId 作者id
     * @return 作者的总热度
     */
    default Optional<Author.Popularity> findByAuthor_Id(Long AuthorId) {
        return getByAuthor_IdAndYearIsNull(AuthorId);
    }

    List<Author.Popularity> getAllByAuthor_Id(Long id);
}
