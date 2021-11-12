package uz.depos.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.Ballot;
import uz.depos.app.domain.enums.BallotOptionEnum;
import uz.depos.app.repository.*;
import uz.depos.app.service.dto.BallotDTO;
import uz.depos.app.service.dto.MeetingLoggingDTO;
import uz.depos.app.service.mapper.AgendaAndVotingMapper;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

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
    private final VotingService votingService;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final AgendaRepository agendaRepository;
    private final VotingRepository votingRepository;

    public BallotService(
        BallotRepository ballotRepository,
        AgendaAndVotingMapper agendaAndVotingMapper,
        MemberService memberService,
        MeetingService meetingService,
        AgendaService agendaService,
        VotingService votingService,
        MeetingRepository meetingRepository,
        MemberRepository memberRepository,
        AgendaRepository agendaRepository,
        VotingRepository votingRepository
    ) {
        this.ballotRepository = ballotRepository;
        this.agendaAndVotingMapper = agendaAndVotingMapper;
        this.memberService = memberService;
        this.meetingService = meetingService;
        this.agendaService = agendaService;
        this.votingService = votingService;
        this.meetingRepository = meetingRepository;
        this.memberRepository = memberRepository;
        this.agendaRepository = agendaRepository;
        this.votingRepository = votingRepository;
    }

    /**
     * Create new ballot, and return it.
     *
     * @param ballotDTO ballot to create.
     * @return created ballot.
     */
    public BallotDTO createBallot(BallotDTO ballotDTO) {
        if (ballotDTO.getId() != null && ballotDTO.getId() > 0) {
            throw new BadRequestAlertException("A new ballot cannot already have an ID", "ballotManagement", "idExists");
        } else if (ballotDTO.getMeetingId() == null || ballotDTO.getMeetingId() == 0) {
            throw new BadRequestAlertException("Meeting ID must NOT be null and zero", "ballotManagement", "meetingIDNull");
        } else if (ballotDTO.getMemberId() == null || ballotDTO.getMemberId() == 0) {
            throw new BadRequestAlertException("Member ID must NOT be null and zero", "ballotManagement", "memberIDNull");
        } else if (ballotDTO.getAgendaId() == null || ballotDTO.getAgendaId() == 0) {
            throw new BadRequestAlertException("Agenda ID must NOT be null and zero", "ballotManagement", "agendaIDNull");
        } else if (ballotDTO.getVotingOptionId() == null || ballotDTO.getVotingOptionId() == 0) {
            throw new BadRequestAlertException("Voting-option ID must NOT be null and zero", "ballotManagement", "votingOptionIDNull");
        } else if (!meetingRepository.existsById(ballotDTO.getMeetingId())) {
            throw new ResourceNotFoundException("Meeting not found by ID: " + ballotDTO.getMeetingId());
        } else if (!memberRepository.existsById(ballotDTO.getMemberId())) {
            throw new ResourceNotFoundException("Member not found by ID: " + ballotDTO.getMemberId());
        } else if (!agendaRepository.existsById(ballotDTO.getAgendaId())) {
            throw new ResourceNotFoundException("Agenda not found by ID: " + ballotDTO.getAgendaId());
        } else if (!votingRepository.existsById(ballotDTO.getVotingOptionId())) {
            throw new ResourceNotFoundException("Voting-option not found by ID: " + ballotDTO.getVotingOptionId());
        }

        Ballot ballot = new Ballot();
        memberService.getMember(ballotDTO.getMemberId()).ifPresent(ballot::setMember);
        meetingService.getMeeting(ballotDTO.getMeetingId()).ifPresent(ballot::setMeeting);
        agendaService.getAgenda(ballotDTO.getAgendaId()).ifPresent(ballot::setAgenda);
        votingService.getVotingOption(ballotDTO.getVotingOptionId()).ifPresent(ballot::setVotingOption);
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
                    votingService.getVotingOption(ballotDTO.getVotingOptionId()).ifPresent(ballot::setVotingOption);
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

    @Transactional(readOnly = true)
    public Page<BallotDTO> getBallotsByMeeting(Long meetingId, Pageable pageable) {
        return ballotRepository.findAllByMeetingId(meetingId, pageable).map(BallotDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<BallotDTO> getBallotsByMember(Long memberId, Pageable pageable) {
        return ballotRepository.findAllByMemberId(memberId, pageable).map(BallotDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<BallotDTO> getBallotsByAgenda(Long agendaId, Pageable pageable) {
        return ballotRepository.findAllByAgendaId(agendaId, pageable).map(BallotDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<BallotDTO> getBallotsByVoting(Long votingId, Pageable pageable) {
        return ballotRepository.findAllByVotingOptionId(votingId, pageable).map(BallotDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<BallotDTO> getBallotsByOption(BallotOptionEnum option, Pageable pageable) {
        return ballotRepository.findAllByOptions(option, pageable).map(BallotDTO::new);
    }

    @Transactional(readOnly = true)
    public List<BallotDTO> getBallotsByAgendaAndMember(Long agendaId, Long memberId) {
        return ballotRepository.findAllByAgendaIdAndMemberId(agendaId, memberId).stream().map(BallotDTO::new).collect(Collectors.toList());
    }
}
