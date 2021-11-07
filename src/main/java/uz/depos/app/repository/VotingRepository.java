package uz.depos.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.VotingOption;

/**
 * Spring Data JPA repository for the {@link VotingOption} entity.
 */
@Repository
public interface VotingRepository extends JpaRepository<VotingOption, Long> {
    Optional<VotingOption> findOneByVotingTextAndAgendaId(String votingText, Long agenda_id);

    Page<VotingOption> findAllByMeetingId(Long meetingId, Pageable pageable);

    Page<VotingOption> findAllByAgendaId(Long agendaId, Pageable pageable);

    List<VotingOption> findAllByAgendaId(Long agendaId);
}
