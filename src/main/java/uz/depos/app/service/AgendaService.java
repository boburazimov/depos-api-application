package uz.depos.app.service;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.Agenda;
import uz.depos.app.repository.AgendaRepository;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.service.dto.AgendaDTO;
import uz.depos.app.service.mapper.AgendaAndVotingMapper;

@Service
@Transactional
public class AgendaService {

    private final Logger log = LoggerFactory.getLogger(AgendaService.class);

    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;
    private final AgendaRepository agendaRepository;
    private final AgendaAndVotingMapper agendaAndVotingMapper;

    public AgendaService(
        MemberRepository memberRepository,
        MeetingRepository meetingRepository,
        AgendaRepository agendaRepository,
        AgendaAndVotingMapper agendaAndVotingMapper
    ) {
        this.memberRepository = memberRepository;
        this.meetingRepository = meetingRepository;
        this.agendaRepository = agendaRepository;
        this.agendaAndVotingMapper = agendaAndVotingMapper;
    }

    /**
     * Create new agenda, and return it.
     *
     * @param agendaDTO agenda to create.
     * @return created agenda.
     */
    public AgendaDTO createAgenda(AgendaDTO agendaDTO) {
        Agenda agenda = new Agenda();
        if (agendaDTO.getMeetingId() != null) meetingRepository.findById(agendaDTO.getMeetingId()).ifPresent(agenda::setMeeting);
        if (StringUtils.isNoneBlank(agendaDTO.getSubject())) agenda.setSubject(agendaDTO.getSubject());
        memberRepository.findOneById(agendaDTO.getSpeakerId()).ifPresent(agenda::setSpeaker);
        agenda.setSpeakTimeEnum(agendaDTO.getSpeakTimeEnum());
        agenda.setDebateEnum(agendaDTO.getDebateEnum());
        agenda.setActive(agendaDTO.getActive());
        Agenda savedAgenda = agendaRepository.saveAndFlush(agenda);
        log.debug("Created Information for Agenda: {}", savedAgenda);
        return agendaAndVotingMapper.agendaToAgendaDTO(savedAgenda);
    }

    /**
     * Get one agenda from data base, and return it.
     *
     * @param id agenda to get.
     * @return get agenda.
     */
    @Transactional(readOnly = true)
    public Optional<Agenda> getAgenda(Long id) {
        return agendaRepository.findById(id);
    }

    /**
     * Get all agendas from data base, and return it in pageable form.
     *
     * @param pageable agenda params to get all of them.
     * @return get agendas.
     */
    @Transactional(readOnly = true)
    public Page<AgendaDTO> getAllAgendas(Pageable pageable) {
        return agendaRepository.findAll(pageable).map(AgendaDTO::new);
    }

    /**
     * Edit all information for a specific agenda, and return the modified agenda.
     *
     * @param agendaDTO agenda to edit.
     * @return edited agenda.
     */
    public Optional<AgendaDTO> updateAgenda(AgendaDTO agendaDTO) {
        return Optional
            .of(agendaRepository.findById(agendaDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                agenda -> {
                    meetingRepository.findById(agendaDTO.getMeetingId()).ifPresent(agenda::setMeeting);
                    agenda.setSubject(agendaDTO.getSubject());
                    memberRepository.findById(agendaDTO.getSpeakerId()).ifPresent(agenda::setSpeaker);
                    agenda.setSpeakTimeEnum(agendaDTO.getSpeakTimeEnum());
                    agenda.setDebateEnum(agendaDTO.getDebateEnum());
                    agenda.setActive(agendaDTO.getActive());
                    Agenda savedAgenda = agendaRepository.saveAndFlush(agenda);
                    log.debug("Changed Information for Agenda: {}", savedAgenda);
                    return savedAgenda;
                }
            )
            .map(AgendaDTO::new);
    }

    /**
     * Delete agenda by id.
     *
     * @param id the id agenda
     */
    public void deleteAgenda(Long id) {
        agendaRepository.findById(id).ifPresent(this::accept);
    }

    private void accept(Agenda agenda) {
        agendaRepository.delete(agenda);
        log.debug("Deleted Agenda: {}", agenda);
    }
}
