package uz.depos.app.service;

import java.util.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.Agenda;
import uz.depos.app.domain.Meeting;
import uz.depos.app.domain.VotingOption;
import uz.depos.app.domain.enums.MeetingStatusEnum;
import uz.depos.app.repository.AgendaRepository;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.VotingRepository;
import uz.depos.app.service.dto.AgendaAndOptionsDTO;
import uz.depos.app.service.dto.AgendaDTO;
import uz.depos.app.service.dto.VotingDTO;
import uz.depos.app.service.mapper.AgendaAndVotingMapper;
import uz.depos.app.web.rest.errors.AgendaSubjectAlreadyUsedException;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

/**
 * Service class for managing agenda.
 */
@Service
@Transactional
public class AgendaService {

    private final Logger log = LoggerFactory.getLogger(AgendaService.class);

    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;
    private final AgendaRepository agendaRepository;
    private final AgendaAndVotingMapper agendaAndVotingMapper;
    private final VotingRepository votingRepository;

    public AgendaService(
        MemberRepository memberRepository,
        MeetingRepository meetingRepository,
        AgendaRepository agendaRepository,
        AgendaAndVotingMapper agendaAndVotingMapper,
        VotingRepository votingRepository
    ) {
        this.memberRepository = memberRepository;
        this.meetingRepository = meetingRepository;
        this.agendaRepository = agendaRepository;
        this.agendaAndVotingMapper = agendaAndVotingMapper;
        this.votingRepository = votingRepository;
    }

    /**
     * Create new agenda, and return it.
     *
     * @param agendaDTO agenda to create.
     * @return created agenda.
     */
    public AgendaAndOptionsDTO createAgenda(AgendaDTO agendaDTO) {
        if (
            agendaDTO.getMeetingId() == null || agendaDTO.getMeetingId() == 0 || (!meetingRepository.existsById(agendaDTO.getMeetingId()))
        ) {
            throw new ResourceNotFoundException("Meeting not found by ID: " + agendaDTO.getMeetingId());
        } else if (agendaDTO.getId() != null && agendaDTO.getId() > 0) {
            throw new BadRequestAlertException("A new agenda cannot already have an ID", "agendaManagement", "idExists");
        } else if (
            agendaDTO.getSpeakerId() != null &&
            agendaDTO.getSpeakerId() > 0 &&
            (!memberRepository.findById(agendaDTO.getSpeakerId()).isPresent())
        ) {
            throw new ResourceNotFoundException("Speaker not found by ID: " + agendaDTO.getSpeakerId());
        }

        agendaRepository
            .findOneBySubjectIgnoreCaseContainsAndMeetingId(agendaDTO.getSubject(), agendaDTO.getMeetingId())
            .ifPresent(
                agenda -> {
                    if (Objects.equals(agendaDTO.getSubject(), agenda.getSubject())) {
                        throw new AgendaSubjectAlreadyUsedException();
                    }
                }
            );
        Agenda agenda = new Agenda();
        meetingRepository.findById(agendaDTO.getMeetingId()).ifPresent(agenda::setMeeting);
        if (StringUtils.isNoneBlank(agendaDTO.getSubject())) agenda.setSubject(agendaDTO.getSubject());
        memberRepository.findOneById(agendaDTO.getSpeakerId()).ifPresent(agenda::setSpeaker);
        agenda.setSpeakTimeEnum(agendaDTO.getSpeakTimeEnum());
        agenda.setTypeEnum(agendaDTO.getTypeEnum());
        agenda.setDebateEnum(agendaDTO.getDebateEnum());
        agenda.setActive(agendaDTO.getActive());
        Agenda savedAgenda = agendaRepository.saveAndFlush(agenda);
        if (agendaDTO.getVariants() != null) {
            Set<String> variants = agendaDTO.getVariants();
            variants.forEach(variant -> votingRepository.save(new VotingOption(variant, savedAgenda.getMeeting(), savedAgenda)));
        }
        log.debug("Created Information for Agenda: {}", savedAgenda);
        List<VotingOption> allByAgendaId = votingRepository.findAllByAgendaId(savedAgenda.getId());
        List<VotingDTO> votingDTOS = agendaAndVotingMapper.votingsToVotingDTOs(allByAgendaId);
        return agendaAndVotingMapper.agendaToAgendaAndVotingDTO(savedAgenda, votingDTOS);
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

    public Optional<AgendaDTO> updateAgenda(AgendaDTO agendaDTO) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(agendaDTO.getMeetingId());
        if (optionalMeeting.isPresent() && optionalMeeting.get().getStatus().equals(MeetingStatusEnum.ACTIVE)) {
            throw new BadRequestAlertException("Agenda can not EDITED for Meeting by ACTIVE status", "agentManagement", "meetingIsActive");
        }
        agendaRepository
            .findOneBySubjectIgnoreCaseContainsAndMeetingId(agendaDTO.getSubject(), agendaDTO.getMeetingId())
            .ifPresent(
                agenda -> {
                    if (!agenda.getId().equals(agendaDTO.getId())) {
                        if (Objects.equals(agendaDTO.getSubject(), agenda.getSubject())) throw new AgendaSubjectAlreadyUsedException();
                    }
                }
            );
        return Optional
            .of(agendaRepository.findById(agendaDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                agenda -> {
                    meetingRepository.findById(agendaDTO.getMeetingId()).ifPresent(agenda::setMeeting);
                    agenda.setSubject(agendaDTO.getSubject());
                    if (agendaDTO.getSpeakerId() != null) {
                        memberRepository.findById(agendaDTO.getSpeakerId()).ifPresent(agenda::setSpeaker);
                    } else {
                        agenda.setSpeaker(null);
                    }
                    agenda.setSpeakTimeEnum(agendaDTO.getSpeakTimeEnum());
                    agenda.setTypeEnum(agendaDTO.getTypeEnum());
                    agenda.setDebateEnum(agendaDTO.getDebateEnum());
                    agenda.setActive(agendaDTO.getActive());
                    Agenda savedAgenda = agendaRepository.saveAndFlush(agenda);
                    log.debug("Changed Information for Agenda: {}", savedAgenda);
                    return savedAgenda;
                }
            )
            .map(AgendaDTO::new);
    }

    public Optional<AgendaDTO> switchAgendaStatus(AgendaDTO agendaDTO) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(agendaDTO.getMeetingId());
        if (optionalMeeting.isPresent() && optionalMeeting.get().getStatus().equals(MeetingStatusEnum.ACTIVE)) {
            throw new BadRequestAlertException("Agenda can not EDITED for Meeting by ACTIVE status", "agentManagement", "meetingIsActive");
        }
        return Optional
            .of(agendaRepository.findById(agendaDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                agenda -> {
                    agenda.setActive(!agenda.getActive());
                    if (ObjectUtils.isNotEmpty(agendaDTO.getExtraInfo())) agenda.setExtraInfo(agendaDTO.getExtraInfo());
                    Agenda savedAgenda = agendaRepository.saveAndFlush(agenda);
                    log.debug("Changed Status Information for Agenda: {}", savedAgenda);
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

    @Transactional(readOnly = true)
    public Page<AgendaAndOptionsDTO> getAgendasByMeeting(Long meetingId, Pageable pageable) {
        Page<Agenda> allByMeetingId = agendaRepository.findAllByMeetingId(meetingId, pageable);
        return allByMeetingId.map(
            agenda -> {
                List<VotingOption> allByAgendaId = votingRepository.findAllByAgendaId(agenda.getId());
                List<VotingDTO> votingDTOS = agendaAndVotingMapper.votingsToVotingDTOs(allByAgendaId);
                return agendaAndVotingMapper.agendaToAgendaAndVotingDTO(agenda, votingDTOS);
            }
        );
    }
}
