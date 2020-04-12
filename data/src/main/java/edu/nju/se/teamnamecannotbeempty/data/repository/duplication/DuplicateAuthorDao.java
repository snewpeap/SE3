package edu.nju.se.teamnamecannotbeempty.data.repository.duplication;

import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAuthor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import java.util.Date;
import java.util.List;

public interface DuplicateAuthorDao extends JpaRepository<DuplicateAuthor, Long> {

    List<DuplicateAuthor> findByClear(Boolean clear, Pageable pageable);

    Streamable<DuplicateAuthor> findBySon_Id(Long id);

    List<DuplicateAuthor> findBySon_IdAndClear(Long id, Boolean clear);

    boolean existsByFather_IdAndSon_Id(Long fatherId, Long sonId);

    Streamable<DuplicateAuthor> findByUpdatedAtAfter(Date date);

    @Query(nativeQuery = true,
    value = "select distinct son_id from duplicate_author where clear = ?3 order by son_id limit ?1,?2")
    List<Long> findIdsPage(int from, int to, boolean clear);
}
