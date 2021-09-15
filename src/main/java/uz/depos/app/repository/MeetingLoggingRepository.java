package uz.depos.app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.MeetingLogging;

/**
 * Spring Data JPA repository for the {@link MeetingLogging} entity.
 */
@Repository
public interface MeetingLoggingRepository extends JpaRepository<MeetingLogging, Long> {
    Optional<MeetingLogging> findOneById(Long id);

    Optional<MeetingLogging> findByMeetingIdAndLoggingText(Long meetingId, String loggingText);
}