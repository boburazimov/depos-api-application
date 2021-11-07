package uz.depos.app.service.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import uz.depos.app.domain.Agenda;
import uz.depos.app.domain.Ballot;
import uz.depos.app.domain.Company;
import uz.depos.app.domain.VotingOption;
import uz.depos.app.service.dto.*;

/**
 * Mapper for the entity {@link Agenda}, {@link VotingOption}, {@link Ballot} and its DTO called {@link AgendaDTO}, {@link VotingDTO}, {@link BallotDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class AgendaAndVotingMapper {

    public AgendaDTO agendaToAgendaDTO(Agenda agenda) {
        return new AgendaDTO(agenda);
    }

    public VotingDTO votingOptionToVotingOptionDTO(VotingOption votingOption) {
        return new VotingDTO(votingOption);
    }

    public BallotDTO ballotToBallotDTO(Ballot ballot) {
        return new BallotDTO(ballot);
    }

    public AgendaAndOptionsDTO agendaToAgendaAndVotingDTO(Agenda agenda, List<VotingDTO> votingDTOs) {
        return new AgendaAndOptionsDTO(agenda, votingDTOs);
    }

    public List<VotingDTO> votingsToVotingDTOs(List<VotingOption> votingOptions) {
        return votingOptions.stream().filter(Objects::nonNull).map(this::votingOptionToVotingOptionDTO).collect(Collectors.toList());
    }
}
