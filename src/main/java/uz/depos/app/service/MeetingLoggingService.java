package uz.depos.app.service;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.MeetingLogging;
import uz.depos.app.repository.MeetingLoggingRepository;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.dto.MeetingLoggingDTO;
import uz.depos.app.service.mapper.MeetingMapper;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

/**
 * Service class for managing meeting-logging.
 */
@Service
@Transactional
public class MeetingLoggingService {

    private final Logger log = LoggerFactory.getLogger(MeetingLoggingService.class);

    final MeetingRepository meetingRepository;
    final UserRepository userRepository;
    final MemberRepository memberRepository;
    final MeetingMapper meetingMapper;
    final MeetingLoggingRepository meetingLoggingRepository;

    public MeetingLoggingService(
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        MeetingMapper meetingMapper,
        MeetingLoggingRepository meetingLoggingRepository
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.meetingMapper = meetingMapper;
        this.meetingLoggingRepository = meetingLoggingRepository;
    }

    public MeetingLoggingDTO addMeetingLogging(MeetingLoggingDTO loggingDTO) {
        if (!meetingRepository.findById(loggingDTO.getMeetingId()).isPresent()) {
            throw new ResourceNotFoundException("Meeting not found by ID: " + loggingDTO.getMeetingId());
        } else if (!userRepository.findById(loggingDTO.getUserId()).isPresent()) {
            throw new ResourceNotFoundException("User not found by ID: " + loggingDTO.getUserId());
        } else if (!memberRepository.findById(loggingDTO.getMemberId()).isPresent()) {
            throw new ResourceNotFoundException("Member not found by ID: " + loggingDTO.getMemberId());
        } else if (StringUtils.isBlank(loggingDTO.getLoggingText())) {
            throw new ResourceNotFoundException("Logging text must not be Empty!");
        } else if (
            meetingLoggingRepository.findByMeetingIdAndLoggingText(loggingDTO.getMeetingId(), loggingDTO.getLoggingText()).isPresent()
        ) throw new BadRequestAlertException("Logging text already in use by this meeting", "loggingManagement", "textExist");

        MeetingLogging logging = new MeetingLogging();
        meetingRepository.findById(loggingDTO.getMeetingId()).ifPresent(meeting -> logging.setMeetingId(meeting.getId()));
        userRepository.findById(loggingDTO.getUserId()).ifPresent(user -> logging.setUserId(user.getId()));
        memberRepository.findById(loggingDTO.getMemberId()).ifPresent(member -> logging.setMemberId(member.getId()));
        logging.setLoggingText(loggingDTO.getLoggingText());
        logging.setActive(true);
        MeetingLogging savedLogging = meetingLoggingRepository.saveAndFlush(logging);
        log.debug("Created Information for Meeting-logging: {}", savedLogging);
        return meetingMapper.meetingLoggingToMeetingLoggingDTO(savedLogging);
    }

    @Transactional(readOnly = true)
    public Optional<MeetingLogging> getMeetingLogging(Long id) {
        return meetingLoggingRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<MeetingLoggingDTO> getAllMeetingLoggings(Pageable pageable) {
        return meetingLoggingRepository.findAll(pageable).map(MeetingLoggingDTO::new);
    }

    public void deleteMeetingLogging(Long id) {
        meetingLoggingRepository
            .findOneById(id)
            .ifPresent(
                logging -> {
                    meetingLoggingRepository.delete(logging);
                    log.debug("Deleted Meeting-logging: {}", logging);
                }
            );
    }

    public Optional<MeetingLoggingDTO> updateLoggingStatus(Long id) {
        return Optional
            .of(meetingLoggingRepository.findById(id))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                logging -> {
                    logging.setActive(!logging.getActive());
                    log.debug("Changed Information for Logging: {}", logging);
                    return meetingLoggingRepository.saveAndFlush(logging);
                }
            )
            .map(MeetingLoggingDTO::new);
    }
}
