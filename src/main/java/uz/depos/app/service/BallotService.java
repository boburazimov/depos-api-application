package uz.depos.app.service;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.Ballot;
import uz.depos.app.repository.BallotRepository;
import uz.depos.app.service.dto.BallotDTO;
import uz.depos.app.service.mapper.AgendaAndVotingMapper;

/**
 * Service class for managing ballot.
 */
@Service
@Transactional
public class BallotService {

    private final Logger log = LoggerFactory.getLogger(BallotService.class);

    private final BallotRepository ballotRepository;
    private final AgendaAndVotingMapper agendaAndVotingMapper;
    private final MemberService memberService;
    private final MeetingService meetingService;
    private final AgendaService agendaService;
    private final VotingOptionService votingOptionService;

    public BallotService(
        BallotRepository ballotRepository,
        AgendaAndVotingMapper agendaAndVotingMapper,
        MemberService memberService,
        MeetingService meetingService,
        AgendaService agendaService,
        VotingOptionService votingOptionService
    ) {
        this.ballotRepository = ballotRepository;
        this.agendaAndVotingMapper = agendaAndVotingMapper;
        this.memberService = memberService;
        this.meetingService = meetingService;
        this.agendaService = agendaService;
        this.votingOptionService = votingOptionService;
    }

    /**
     * Create new ballot, and return it.
     *
     * @param ballotDTO ballot to create.
     * @return created ballot.
     */
    public BallotDTO createBallot(BallotDTO ballotDTO) {
        Ballot ballot = new Ballot();

        memberService.getMember(ballotDTO.getMemberId()).ifPresent(ballot::setMember);
        meetingService.getMeeting(ballotDTO.getMeetingId()).ifPresent(ballot::setMeeting);
        agendaService.getAgenda(ballotDTO.getAgendaId()).ifPresent(ballot::setAgenda);
        votingOptionService.getVotingOption(ballotDTO.getVotingOptionId()).ifPresent(ballot::setVotingOption);
        if (StringUtils.isNoneBlank(ballotDTO.getOptions().toString())) ballot.setOptions(ballotDTO.getOptions());

        Ballot savedBallot = ballotRepository.saveAndFlush(ballot);
        log.debug("Created Information for Ballot: {}", savedBallot);
        return agendaAndVotingMapper.ballotToBallotDTO(savedBallot);
    }

    /**
     * Get one ballot from data base, and return it.
     *
     * @param id ballot to get.
     * @return get ballot.
     */
    @Transactional(readOnly = true)
    public Optional<Ballot> getBallot(Long id) {
        return ballotRepository.findById(id);
    }

    /**
     * Get all ballots from data base, and return it in pageable form.
     *
     * @param pageable ballots params to get all of them.
     * @return get ballots.
     */
    @Transactional(readOnly = true)
    public Page<BallotDTO> getAllBallots(Pageable pageable) {
        return ballotRepository.findAll(pageable).map(BallotDTO::new);
    }

    /**
     * Edit all information for a specific ballot, and return the modified ballot.
     *
     * @param ballotDTO ballot to edit.
     * @return edited ballot.
     */
    public Optional<BallotDTO> updateBallot(BallotDTO ballotDTO) {
        return Optional
            .of(ballotRepository.findById(ballotDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                ballot -> {
                    memberService.getMember(ballotDTO.getMemberId()).ifPresent(ballot::setMember);
                    meetingService.getMeeting(ballotDTO.getMeetingId()).ifPresent(ballot::setMeeting);
                    agendaService.getAgenda(ballotDTO.getAgendaId()).ifPresent(ballot::setAgenda);
                    votingOptionService.getVotingOption(ballotDTO.getVotingOptionId()).ifPresent(ballot::setVotingOption);
                    if (StringUtils.isNoneBlank(ballotDTO.getOptions().toString())) ballot.setOptions(ballotDTO.getOptions());
                    Ballot savedBallot = ballotRepository.saveAndFlush(ballot);
                    log.debug("Changed Information for Ballot: {}", savedBallot);
                    return savedBallot;
                }
            )
            .map(BallotDTO::new);
    }

    /**
     * Delete Ballot by id.
     *
     * @param id the id Ballot
     */
    public void deleteBallot(Long id) {
        ballotRepository.findById(id).ifPresent(this::accept);
    }

    private void accept(Ballot ballot) {
        ballotRepository.delete(ballot);
        log.debug("Deleted Agenda: {}", ballot);
    }
}
