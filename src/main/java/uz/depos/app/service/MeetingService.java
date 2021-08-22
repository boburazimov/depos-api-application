package uz.depos.app.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.City;
import uz.depos.app.domain.Company;
import uz.depos.app.domain.Meeting;
import uz.depos.app.repository.*;
import uz.depos.app.service.dto.MeetingDTO;
import uz.depos.app.service.mapper.MeetingMapper;

/**
 * Service class for managing meeting.
 */
@Service
@Transactional
public class MeetingService {

    private final Logger log = LoggerFactory.getLogger(MeetingService.class);

    final MeetingRepository meetingRepository;
    final UserRepository userRepository;
    final CompanyRepository companyRepository;
    final CityRepository cityRepository;
    final MemberRepository memberRepository;
    final AgendaRepository agendaRepository;
    final MeetingMapper meetingMapper;

    public MeetingService(
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        CompanyRepository companyRepository,
        CityRepository cityRepository,
        MemberRepository memberRepository,
        AgendaRepository agendaRepository,
        MeetingMapper meetingMapper
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.cityRepository = cityRepository;
        this.memberRepository = memberRepository;
        this.agendaRepository = agendaRepository;
        this.meetingMapper = meetingMapper;
    }

    public MeetingDTO createMeeting(MeetingDTO request) {
        try {
            Meeting meeting = request.getId() == null || request.getId() == 0
                ? new Meeting()
                : meetingRepository.findById(request.getId()).orElseThrow(() -> new ResourceNotFoundException("getMeeting"));
            meeting.setStatus(request.getStatus());
            meeting.setTypeEnum(request.getTypeEnum());
            meeting.setStartDate(request.getStartDate());
            meeting.setStartRegistration(request.getStartRegistration());
            meeting.setEndRegistration(request.getEndRegistration());
            if (request.getCompanyId() != null && request.getCompanyId() > 0) {
                Optional<Company> optionalCompany = companyRepository.findById(request.getCompanyId());
                optionalCompany.ifPresent(meeting::setCompany);
            }
            if (request.getCityId() != null && request.getCityId() > 0) {
                Optional<City> optionalCity = cityRepository.findById(request.getCityId());
                optionalCity.ifPresent(meeting::setCity);
            }
            meeting.setAddress(request.getAddress());
            meeting.setDescription(request.getDescription());
            Meeting savedMeeting = meetingRepository.save(meeting);
            MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(savedMeeting);
            log.debug("Created Information for Meeting: {}", meetingDTO);
            return meetingDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update all information for a specific meeting, and return the modified meeting.
     *
     * @param meetingDTO meeting to update.
     * @return updated meeting.
     */
    public Optional<MeetingDTO> updateMeeting(MeetingDTO meetingDTO) {
        return Optional
            .of(meetingRepository.findById(meetingDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                meeting -> {
                    meeting.setStatus(meetingDTO.getStatus());
                    meeting.setTypeEnum(meetingDTO.getTypeEnum());
                    meeting.setStartDate(meetingDTO.getStartDate());
                    meeting.setStartRegistration(meetingDTO.getStartRegistration());
                    meeting.setEndRegistration(meetingDTO.getEndRegistration());
                    companyRepository.findById(meetingDTO.getCompanyId()).ifPresent(meeting::setCompany);
                    if (meetingDTO.getCityId() != null && meetingDTO.getCityId() > 0) cityRepository
                        .findById(meetingDTO.getCityId())
                        .ifPresent(meeting::setCity);
                    meeting.setAddress(meetingDTO.getAddress());
                    meeting.setDescription(meetingDTO.getDescription());
                    log.debug("Changed Information for User: {}", meeting);
                    return meetingRepository.saveAndFlush(meeting);
                }
            )
            .map(MeetingDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<Meeting> getMeeting(Long id) {
        return meetingRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<MeetingDTO> getAllMeetings(Pageable pageable) {
        return meetingRepository.findAll(pageable).map(MeetingDTO::new);
    }

    public void deleteMeeting(Long id) {
        meetingRepository
            .findOneById(id)
            .ifPresent(
                meeting -> {
                    meetingRepository.delete(meeting);
                    log.debug("Deleted Meeting: {}", meeting);
                }
            );
    }
}
