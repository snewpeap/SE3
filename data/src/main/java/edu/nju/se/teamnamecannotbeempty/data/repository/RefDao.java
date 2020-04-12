package edu.nju.se.teamnamecannotbeempty.data.repository;

import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefDao extends JpaRepository<Ref, Long> {
    List<Ref> findByRefereeIsNotNull();

    List<Ref> findByLowercaseTitleEqualsAndRefereeIsNull(String lowercaseTitle);
}
