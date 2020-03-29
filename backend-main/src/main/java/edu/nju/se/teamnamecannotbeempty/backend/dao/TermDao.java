package edu.nju.se.teamnamecannotbeempty.backend.dao;

import edu.nju.se.teamnamecannotbeempty.backend.po.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TermDao extends JpaRepository<Term, Long> {
    Optional<Term> findByContent(String content);

    Optional<Term> findByContentIgnoreCase(String content);
}
