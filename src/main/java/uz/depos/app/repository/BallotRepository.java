package uz.depos.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Ballot;
import uz.depos.app.domain.enums.BallotOptionEnum;

/**
 * Spring Data JPA repository for the {@link Ballot} entity.
 */
@Repository
public interface BallotRepository extends JpaRepository<Ballot, Long> {
    Page<Ballot> findAllByMeetingId(Long meetingId, Pageable pageable);
    Page<Ballot> findAllByMemberId(Long memberId, Pageable pageable);
    Page<Ballot> findAllByAgendaId(Long agendaId, Pageable pageable);
    Page<Ballot> findAllByVotingOptionId(Long votingId, Pageable pageable);
    Page<Ballot> findAllByOptions(BallotOptionEnum options, Pageable pageable);
}
