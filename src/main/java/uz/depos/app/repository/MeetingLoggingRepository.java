package uz.depos.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    List<MeetingLogging> findAllByMeetingId(Long meetingId);

    void deleteAllByMeetingId(Long meetingId);
}
