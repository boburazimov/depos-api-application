package uz.depos.app.repository;

import java.util.List;
import java.util.Optional;
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

    Optional<List<Ballot>> findAllByMeetingId(Long meetingId);

    void deleteAllByMeetingId(Long meetingId);

    Page<Ballot> findAllByMemberId(Long memberId, Pageable pageable);

    Page<Ballot> findAllByAgendaId(Long agendaId, Pageable pageable);

    Page<Ballot> findAllByVotingOptionId(Long votingId, Pageable pageable);

    Page<Ballot> findAllByOptions(BallotOptionEnum options, Pageable pageable);

    List<Ballot> findAllByAgendaIdAndMemberId(Long agendaId, Long memberId);

    Optional<Ballot> findByMeetingIdAndMemberIdAndAgendaIdAndVotingOptionId(
        Long meetingId,
        Long memberId,
        Long agendaId,
        Long votingOptionId
    );
}
