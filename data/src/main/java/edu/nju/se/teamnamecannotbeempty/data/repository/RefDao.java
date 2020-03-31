package edu.nju.se.teamnamecannotbeempty.data.repository;

import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefDao extends JpaRepository<Ref, Long> {
}
