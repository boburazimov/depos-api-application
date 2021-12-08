package uz.depos.app.service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.matching;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.City;
import uz.depos.app.domain.Company;
import uz.depos.app.domain.Meeting;
import uz.depos.app.domain.Member;
import uz.depos.app.domain.enums.MeetingSearchFieldEnum;
import uz.depos.app.domain.enums.MeetingStatusEnum;
import uz.depos.app.domain.enums.MeetingTypeEnum;
import uz.depos.app.domain.enums.MemberTypeEnum;
import uz.depos.app.repository.*;
import uz.depos.app.repository.specification.MeetingSpecification;
import uz.depos.app.service.dto.MeetingDTO;
import uz.depos.app.service.dto.MeetingFilterDTO;
import uz.depos.app.service.mapper.MeetingMapper;
import uz.depos.app.service.mapper.UserMapper;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

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
    final UserMapper userMapper;
    final MeetingSpecification meetingSpecification;

    public MeetingService(
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        CompanyRepository companyRepository,
        CityRepository cityRepository,
        MemberRepository memberRepository,
        AgendaRepository agendaRepository,
        MeetingMapper meetingMapper,
        UserMapper userMapper,
        MeetingSpecification meetingSpecification
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.cityRepository = cityRepository;
        this.memberRepository = memberRepository;
        this.agendaRepository = agendaRepository;
        this.meetingMapper = meetingMapper;
        this.userMapper = userMapper;
        this.meetingSpecification = meetingSpecification;
    }

    public MeetingDTO createMeeting(MeetingDTO request) {
        if (ObjectUtils.isEmpty(request.getCompanyId())) {
            throw new NullPointerException("Meeting have must a company");
        } else if (!companyRepository.existsById(request.getCompanyId())) {
            throw new ResourceNotFoundException("Company not found by ID: " + request.getCompanyId());
        }
        boolean present = meetingRepository.findOneByStartDateAndCompanyId(request.getStartDate(), request.getCompanyId()).isPresent();
        if (present) {
            throw new MeetingWithStartDateAlreadyCreatedException();
        }
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

            // create new Member for Chairmen and Secretary
            companyRepository
                .findById(meetingDTO.getCompanyId())
                .ifPresent(
                    company -> {
                        // CHAIRMAN
                        if (company.getChairman() != null && userRepository.findById(company.getChairman().getId()).isPresent()) {
                            Member member = new Member();
                            member.setMeeting(savedMeeting);
                            member.setCompany(company);
                            member.setUser(company.getChairman());
                            member.setRemotely(true);
                            member.setConfirmed(false);
                            member.setInvolved(false);
                            member.setMemberTypeEnum(MemberTypeEnum.CHAIRMAN);
                            member.setFromReestr(false);
                            memberRepository.save(member);
                        }
                        // SECRETARY
                        if (company.getSecretary() != null && userRepository.findById(company.getSecretary().getId()).isPresent()) {
                            Member member = new Member();
                            member.setMeeting(savedMeeting);
                            member.setCompany(company);
                            member.setUser(company.getSecretary());
                            member.setRemotely(true);
                            member.setConfirmed(false);
                            member.setInvolved(false);
                            member.setMemberTypeEnum(MemberTypeEnum.SECRETARY);
                            member.setFromReestr(false);
                            memberRepository.save(member);
                        }
                    }
                );
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
                    companyRepository
                        .findById(meetingDTO.getCompanyId())
                        .ifPresent(
                            company -> {
                                meeting.setCompany(company);
                                memberRepository
                                    .findAllByMeetingId(meetingDTO.getId())
                                    .ifPresent(
                                        members ->
                                            members.forEach(
                                                member -> {
                                                    member.setCompany(company);
                                                    memberRepository.save(member);
                                                }
                                            )
                                    );
                            }
                        );
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
                    Optional<List<Member>> allByMeetingIdAndFromReestrTrue = memberRepository.findAllByMeetingIdAndFromReestrTrue(
                        meeting.getId()
                    );
                    allByMeetingIdAndFromReestrTrue.ifPresent(
                        members -> {
                            if (!members.isEmpty()) {
                                throw new BadRequestAlertException(
                                    "By this Meeting already has Reestr!",
                                    "MeetingManagement",
                                    "ReestrExist"
                                );
                            } else {
                                meetingRepository.delete(meeting);
                                log.debug("Deleted Meeting: {}", meeting);
                            }
                        }
                    );
                }
            );
    }

    public Page<MeetingDTO> filterMeeting(MeetingSearchFieldEnum field, String value, Pageable pageable) {
        Meeting meeting = new Meeting();
        switch (field) {
            case COMPANY:
                Company company = new Company();
                company.setName(value);
                meeting.setCompany(company);
                break;
            case STATUS:
                meeting.setStatus(MeetingStatusEnum.valueOf(value));
                break;
            case TYPE_ENUM:
                meeting.setTypeEnum(MeetingTypeEnum.valueOf(value));
                break;
            case START_DATE:
                Instant instant = Instant.parse(value); // "2018-11-30T18:35:24.00Z"
                meeting.setStartDate(instant);
                break;
            case CITY:
                City city = new City();
                city.setNameUz(value);
                meeting.setCity(city);
                break;
            default:
                break;
        }

        ExampleMatcher matcher = matching()
            .withMatcher("company.name", contains().ignoreCase())
            .withMatcher("status", (ExampleMatcher.GenericPropertyMatcher) matching())
            .withMatcher("typeEnum", (ExampleMatcher.GenericPropertyMatcher) matching())
            .withMatcher("startDate", (ExampleMatcher.GenericPropertyMatcher) matching())
            .withMatcher("city", contains().ignoreCase())
            .withIgnorePaths("createdDate", "lastModifiedDate");

        if (value == null) {
            return meetingRepository.findAll(pageable).map(MeetingDTO::new);
        } else {
            return meetingRepository.findAll(Example.of(meeting, matcher), pageable).map(MeetingDTO::new);
        }
    }

    @Transactional(readOnly = true)
    public Page<MeetingDTO> getMeetingsByCompany(Long companyId, Pageable pageable) {
        return meetingRepository.findAllByCompanyId(companyId, pageable).map(MeetingDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<MeetingDTO> getMeetingsByCompanyAndUser(Long userId, Long companyId, Pageable pageable) {
        return meetingRepository.findMeetingsByUserAndCompany(userId, companyId, pageable).map(MeetingDTO::new);
    }

    public MeetingDTO changeMeetingStatus(Long meetingId, MeetingStatusEnum statusEnum) {
        Optional<Meeting> meetingOptional = meetingRepository.findById(meetingId);
        if (meetingOptional.isPresent()) {
            Meeting meeting = meetingOptional.get();
            Boolean active = meeting.getCompany().getActive();
            if (statusEnum.equals(MeetingStatusEnum.ACTIVE) && !active) {
                throw new BadRequestAlertException(
                    "Company must be in ACTIVE for change Meeting status!",
                    "MeetingManagement",
                    "CompanyNotActive"
                );
            } else {
                meeting.setStatus(statusEnum);
                Meeting savedMeeting = meetingRepository.saveAndFlush(meeting);
                return meetingMapper.meetingToMeetingDTO(savedMeeting);
            }
        } else {
            throw new BadRequestAlertException("Meeting not found by this ID " + meetingId, "MeetingManagement", "NotFoundMeeting");
        }
    }

    public Page<MeetingFilterDTO> getFilteredMeetings(MeetingFilterDTO meetingDTO, Pageable pageable) {
        return meetingRepository.findAll(meetingSpecification.getMeetings(meetingDTO), pageable).map(MeetingFilterDTO::new);
    }
}
