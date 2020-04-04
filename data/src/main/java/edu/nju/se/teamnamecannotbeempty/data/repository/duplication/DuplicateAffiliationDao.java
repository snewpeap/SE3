package edu.nju.se.teamnamecannotbeempty.data.repository.duplication;

import edu.nju.se.teamnamecannotbeempty.data.domain.DuplicateAffiliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

public interface DuplicateAffiliationDao extends JpaRepository<DuplicateAffiliation, Long> {
    Streamable<DuplicateAffiliation> findByClearIs(Boolean clear);

    Streamable<DuplicateAffiliation> findBySon_Id(Long sonId);

    boolean existsByFather_IdAndSon_Id(Long fatherId, Long sonId);
}
