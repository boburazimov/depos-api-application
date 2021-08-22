package uz.depos.app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.VotingOption;

@Repository
public interface VotingOptionRepository extends JpaRepository<VotingOption, Long> {
    Optional<VotingOption> findOneByVotingTextAndAgendaId(String votingText, Long agenda_id);
}
