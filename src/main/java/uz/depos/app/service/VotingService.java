package uz.depos.app.service;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.VotingOption;
import uz.depos.app.repository.AgendaRepository;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.VotingRepository;
import uz.depos.app.service.dto.VotingDTO;
import uz.depos.app.service.dto.VotingEditDTO;
import uz.depos.app.service.mapper.AgendaAndVotingMapper;

/**
 * Service class for managing voting-option.
 */
@Service
@Transactional
public class VotingService {

    private final Logger log = LoggerFactory.getLogger(VotingService.class);

    private final MeetingRepository meetingRepository;
    private final AgendaRepository agendaRepository;
    private final AgendaAndVotingMapper agendaAndVotingMapper;
    private final VotingRepository votingRepository;

    public VotingService(
        MeetingRepository meetingRepository,
        AgendaRepository agendaRepository,
        AgendaAndVotingMapper agendaAndVotingMapper,
        VotingRepository votingRepository
    ) {
        this.meetingRepository = meetingRepository;
        this.agendaRepository = agendaRepository;
        this.agendaAndVotingMapper = agendaAndVotingMapper;
        this.votingRepository = votingRepository;
    }

    /**
     * Create new votingOption, and return it.
     *
     * @param votingDTO votingOption to create.
     * @return created votingOption.
     */
    public VotingDTO createVotingOption(VotingDTO votingDTO) {
        VotingOption votingOption = new VotingOption();
        if (StringUtils.isNoneBlank(votingDTO.getVotingText())) votingOption.setVotingText(votingDTO.getVotingText());
        if (votingDTO.getMeetingId() != null) meetingRepository.findById(votingDTO.getMeetingId()).ifPresent(votingOption::setMeeting);
        if (votingDTO.getAgendaId() != null) agendaRepository.findById(votingDTO.getAgendaId()).ifPresent(votingOption::setAgenda);
        VotingOption savedVotingOption = votingRepository.saveAndFlush(votingOption);
        log.debug("Created Information for VotingOption: {}", savedVotingOption);
        return agendaAndVotingMapper.votingOptionToVotingOptionDTO(savedVotingOption);
    }

    /**
     * Get one votingOption from data base, and return it.
     *
     * @param id votingOption to get.
     * @return get votingOption.
     */
    @Transactional(readOnly = true)
    public Optional<VotingOption> getVotingOption(Long id) {
        return votingRepository.findById(id);
    }

    /**
     * Get all votingOptions from data base, and return it in pageable form.
     *
     * @param pageable votingOptions params to get all of them.
     * @return get votingOptions.
     */
    @Transactional(readOnly = true)
    public Page<VotingDTO> getAllVotingOptions(Pageable pageable) {
        return votingRepository.findAll(pageable).map(VotingDTO::new);
    }

    /**
     * Edit all information for a specific votingOption, and return the modified votingOption.
     *
     * @param votingDTO votingOption to edit.
     * @return edited votingOption.
     */
    public Optional<VotingEditDTO> updateVotingOption(VotingEditDTO votingDTO) {
        return Optional
            .of(votingRepository.findById(votingDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                votingOption -> {
                    votingOption.setVotingText(votingDTO.getVotingText());
                    VotingOption savedVotingOption = votingRepository.saveAndFlush(votingOption);
                    log.debug("Changed Information for VotingOption: {}", savedVotingOption);
                    return savedVotingOption;
                }
            )
            .map(VotingEditDTO::new);
    }

    /**
     * Delete VotingOption by id.
     *
     * @param id the id VotingOption
     */
    public void deleteVotingOption(Long id) {
        votingRepository.findById(id).ifPresent(this::accept);
    }

    private void accept(VotingOption votingOption) {
        votingRepository.delete(votingOption);
        log.debug("Deleted Agenda: {}", votingOption);
    }

    @Transactional(readOnly = true)
    public Page<VotingDTO> getVotingsByMeeting(Long meetingId, Pageable pageable) {
        return votingRepository.findAllByMeetingId(meetingId, pageable).map(VotingDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<VotingDTO> getVotingsByAgenda(Long agendaId, Pageable pageable) {
        return votingRepository.findAllByAgendaId(agendaId, pageable).map(VotingDTO::new);
    }
}
