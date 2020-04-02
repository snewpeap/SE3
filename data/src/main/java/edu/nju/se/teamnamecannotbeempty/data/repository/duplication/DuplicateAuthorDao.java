package edu.nju.se.teamnamecannotbeempty.data.repository.duplication;

import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuplicateAuthorDao extends JpaRepository<DuplicateAuthor, Long> {
}
