package edu.nju.se.teamnamecannotbeempty.data.repository.duplication;

import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

public interface DuplicateAuthorDao extends JpaRepository<DuplicateAuthor, Long> {
    Streamable<DuplicateAuthor> findByClearIs(Boolean clear);

    Streamable<DuplicateAuthor> findBySon_Id(Long id);

    boolean existsByFather_IdAndSon_Id(Long fatherId, Long sonId);
}
