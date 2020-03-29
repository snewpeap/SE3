package edu.nju.se.teamnamecannotbeempty.backend.dao;

import edu.nju.se.teamnamecannotbeempty.backend.po.Ref;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefDao extends JpaRepository<Ref, Long> {
}
