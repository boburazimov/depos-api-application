package uz.depos.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Ballot;

/**
 * Spring Data JPA repository for the {@link Ballot} entity.
 */
@Repository
public interface BallotRepository extends JpaRepository<Ballot, Long> {}
