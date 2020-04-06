package edu.nju.se.teamnamecannotbeempty.data.repository.duplication;

import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAuthor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

import java.util.Date;
import java.util.List;

public interface DuplicateAuthorDao extends JpaRepository<DuplicateAuthor, Long> {

    List<DuplicateAuthor> findByClear(Boolean clear, Pageable pageable);

    Streamable<DuplicateAuthor> findBySon_Id(Long id);

    boolean existsByFather_IdAndSon_Id(Long fatherId, Long sonId);

    Streamable<DuplicateAuthor> findByUpdatedAtAfter(Date date);
}
