package uz.depos.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.depos.app.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
