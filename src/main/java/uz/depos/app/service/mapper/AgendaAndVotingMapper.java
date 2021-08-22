package uz.depos.app.service.mapper;

import org.springframework.stereotype.Service;
import uz.depos.app.domain.Agenda;
import uz.depos.app.domain.VotingOption;
import uz.depos.app.service.dto.AgendaDTO;
import uz.depos.app.service.dto.VotingOptionDTO;

/**
 * Mapper for the entity {@link Agenda}, {@link VotingOption} and its DTO called {@link AgendaDTO}, {@link VotingOptionDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class AgendaAndVotingMapper {

    public AgendaDTO agendaToAgendaDTO(Agenda agenda) {
        return new AgendaDTO(agenda);
    }

    public VotingOptionDTO votingOptionToVotingOptionDTO(VotingOption votingOption) {
        return new VotingOptionDTO(votingOption);
    }
}
