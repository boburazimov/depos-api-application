package uz.depos.app.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Agenda;
import uz.depos.app.domain.Member;

/**
 * Spring Data JPA repository for the {@link Agenda} entity.
 */
@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    Optional<Agenda> findOneBySubjectIgnoreCaseContainsAndMeetingId(String subject, Long meetingId);

    Page<Agenda> findAllByMeetingId(Long meetingId, Pageable pageable);

    Optional<Agenda> findFirstBySpeaker(Member speaker);
}
