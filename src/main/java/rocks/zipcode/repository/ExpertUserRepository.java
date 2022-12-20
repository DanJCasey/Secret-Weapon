package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.ExpertUser;

/**
 * Spring Data JPA repository for the ExpertUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpertUserRepository extends JpaRepository<ExpertUser, Long> {}
