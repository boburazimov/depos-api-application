package uz.depos.app.repository;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Meeting;

/**
 * Spring Data JPA repository for the {@link Meeting} entity.
 */
@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<Meeting> findOneById(Long id);

    Optional<Meeting> findOneByStartDate(Instant date);
}
