package uz.depos.app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Agenda;

/**
 * Spring Data JPA repository for the {@link Agenda} entity.
 */
@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    Optional<Agenda> findOneBySubject(String subject);

    Optional<Agenda> findOneBySubjectContainsAndMeetingId(String subject, Long meeting_id);
}
