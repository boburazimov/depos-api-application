package uz.depos.app.service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.matching;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.*;
import uz.depos.app.domain.enums.MemberSearchFieldEnum;
import uz.depos.app.domain.enums.MemberTypeEnum;
import uz.depos.app.repository.*;
import uz.depos.app.service.dto.MemberDTO;
import uz.depos.app.service.dto.MemberManagersDTO;
import uz.depos.app.service.dto.MemberTypeDTO;
import uz.depos.app.service.mapper.MemberMapper;
import uz.depos.app.service.utils.DistinctByKey;
import uz.depos.app.web.rest.errors.BadRequestAlertException;

/**
 * Service class for managing member.
 */
@Service
@Transactional
public class MemberService {

    private final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final CompanyRepository companyRepository;
    private final MemberSessionRepository memberSessionRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final AgendaRepository agendaRepository;

    public MemberService(
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        MemberMapper memberMapper,
        CompanyRepository companyRepository,
        MemberSessionRepository memberSessionRepository,
        SimpMessageSendingOperations messagingTemplate,
        AgendaRepository agendaRepository
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.companyRepository = companyRepository;
        this.memberSessionRepository = memberSessionRepository;
        this.messagingTemplate = messagingTemplate;
        this.agendaRepository = agendaRepository;
    }

    public MemberDTO createMember(MemberDTO memberDTO) {
        Member member = new Member();
        meetingRepository
            .findOneById(memberDTO.getMeetingId())
            .ifPresent(
                meeting -> {
                    member.setMeeting(meeting);
                    if (meeting.getCompany() != null) {
                        companyRepository.findById(meeting.getCompany().getId()).ifPresent(member::setCompany);
                    } else {
                        throw new NullPointerException("Company must not be null for meeting!");
                    }
                }
            );
        userRepository.findById(memberDTO.getUserId()).ifPresent(member::setUser);
        member.setRemotely(memberDTO.getRemotely());
        member.setConfirmed(false);
        member.setInvolved(false);
        member.setMemberTypeEnum(memberDTO.getMemberTypeEnum());
        member.setHldIt(memberDTO.getHldIt());
        member.setPosition(memberDTO.getPosition());
        member.setFromReestr(false);

        Member savedMember = memberRepository.saveAndFlush(member);
        log.debug("Created Information for Member: {}", savedMember);
        return memberMapper.memberToMemberDTO(savedMember);
    }

    public Optional<MemberDTO> updateMember(MemberDTO memberDTO) {
        return Optional
            .of(memberRepository.findById(memberDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                member -> {
                    if (memberDTO.getMeetingId() != null) {
                        meetingRepository
                            .findById(memberDTO.getMeetingId())
                            .ifPresent(
                                meeting -> {
                                    member.setMeeting(meeting);
                                    if (meeting.getCompany() != null) {
                                        companyRepository.findById(meeting.getCompany().getId()).ifPresent(member::setCompany);
                                    } else {
                                        throw new NullPointerException("Company must not be null for meeting!");
                                    }
                                }
                            );
                    }
                    if (memberDTO.getUserId() != null) {
                        userRepository.findById(memberDTO.getUserId()).ifPresent(member::setUser);
                    }
                    member.setRemotely(memberDTO.getRemotely());
                    member.setConfirmed(false);
                    member.setInvolved(memberDTO.getInvolved());
                    member.setMemberTypeEnum(memberDTO.getMemberTypeEnum());
                    member.setHldIt(memberDTO.getHldIt());
                    member.setPosition(memberDTO.getPosition());
                    member.setFromReestr(false);
                    log.debug("Changed Information for Member: {}", member);
                    return memberRepository.saveAndFlush(member);
                }
            )
            .map(MemberDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMember(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<MemberDTO> getAllMembers(Pageable pageable) {
        return memberRepository.findAllByIdNotNullAndMeetingIdNotNull(pageable).map(MemberDTO::new);
    }

    public void deleteMember(Long id) {
        memberRepository
            .findOneById(id)
            .ifPresent(
                member -> {
                    if (
                        member.getMemberTypeEnum() != null &&
                        member.getMemberTypeEnum().equals(MemberTypeEnum.SPEAKER) &&
                        agendaRepository.findFirstBySpeaker(member).isPresent()
                    ) {
                        throw new BadRequestAlertException(
                            "This Member already participates an another Agenda",
                            "memberManagement",
                            "existInAgenda"
                        );
                    }
                    memberRepository.delete(member);
                    log.debug("Deleted Member: {}", member);
                }
            );
    }

    /**
     * @param meetingId to delete all Members by Meeting ID
     */
    public void deleteAllByMeetingId(Long meetingId) {
        memberRepository
            .findAllByMeetingIdAndFromReestrTrue(meetingId)
            .ifPresent(
                members -> {
                    memberRepository.deleteAllByMeetingIdAndFromReestrTrue(meetingId);
                    memberRepository.deleteAll(members);
                    log.debug("Deleted Members by Meeting ID: {}", meetingId);
                }
            );
    }

    public Page<MemberDTO> filterMembers(MemberSearchFieldEnum field, String value, Pageable pageable) {
        Member member = new Member();
        User user = new User();
        switch (field) {
            case NAME:
                user.setFullName(value);
                member.setUser(user);
                break;
            case PINFL:
                user.setPinfl(value);
                member.setUser(user);
                break;
            case EMAIL:
                user.setEmail(value);
                member.setUser(user);
                break;
            case PHONE_NUMBER:
                user.setPhoneNumber(value);
                member.setUser(user);
                break;
            default:
                break;
        }

        ExampleMatcher matcher = matching()
            .withMatcher("user.fullName", contains().ignoreCase())
            .withMatcher("user.pinfl", contains().ignoreCase())
            .withMatcher("user.email", contains().ignoreCase())
            .withMatcher("user.phoneNumber", contains().ignoreCase())
            .withIgnorePaths("createdDate", "lastModifiedDate");

        log.debug("Filtered Members by value: {}", value);
        if (value == null) {
            return memberRepository.findAll(pageable).map(MemberDTO::new);
        } else {
            return memberRepository.findAll(Example.of(member, matcher), pageable).map(MemberDTO::new);
        }
    }

    public MemberManagersDTO addManagers(MemberManagersDTO managersDTO) {
        if (
            memberRepository
                .findOneByMeetingIdAndUserIdAndMemberTypeEnum(
                    managersDTO.getMeetingId(),
                    managersDTO.getUserId(),
                    managersDTO.getMemberTypeEnum()
                )
                .isPresent()
        ) {
            throw new BadRequestAlertException("Member already has by this MemberTypeEnum", "memberManagement", "memberExistByType");
        }
        if (
            managersDTO.getMemberTypeEnum().equals(MemberTypeEnum.SECRETARY) &&
            memberRepository.findOneByMeetingIdAndMemberTypeEnum(managersDTO.getMeetingId(), MemberTypeEnum.SECRETARY).isPresent()
        ) {
            throw new BadRequestAlertException(
                "Member already has by this MemberTypeEnum : SECRETARY",
                "memberManagement",
                "memberExistByTypeSecretary"
            );
        }
        if (userRepository.findById(managersDTO.getUserId()).isPresent()) {
            Member member = new Member();
            meetingRepository
                .findById(managersDTO.getMeetingId())
                .ifPresent(
                    meeting -> {
                        member.setMeeting(meeting);
                        companyRepository.findById(meeting.getCompany().getId()).ifPresent(member::setCompany);
                    }
                );
            userRepository.findById(managersDTO.getUserId()).ifPresent(member::setUser);
            member.setRemotely(true);
            member.setConfirmed(false);
            member.setInvolved(false);
            member.setMemberTypeEnum(managersDTO.getMemberTypeEnum());
            member.setFromReestr(false);
            Member savedMember = memberRepository.saveAndFlush(member);
            log.debug("Created Manager-member: {}", savedMember);
            return memberMapper.memberToMemberManagersDTO(savedMember);
        } else {
            throw new ResourceNotFoundException("User not found by ID: " + managersDTO.getUserId());
        }
    }

    @Transactional(readOnly = true)
    public Page<MemberDTO> getMembersByMeeting(Long meetingId, Pageable pageable, Boolean fromReestr) {
        return memberRepository.findAllByMeetingIdAndFromReestr(meetingId, fromReestr, pageable).map(MemberDTO::new);
    }

    public MemberDTO turnOnConfirmed(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            Instant startRegistration = member.getMeeting().getStartRegistration();
            Instant now = Instant.now().plus(5, ChronoUnit.HOURS);
            if (now.isAfter(startRegistration)) {
                member.setConfirmed(true);
                memberRepository.save(member);
            }
            MemberDTO memberDTO = memberMapper.memberToMemberDTO(member);
            memberRepository
                .findAllByMeetingIdAndFromReestrTrue(memberDTO.getMeetingId())
                .ifPresent(
                    members -> {
                        List<MemberDTO> memberDTOS = memberMapper.membersToMemberDTOs(members);
                        messagingTemplate.convertAndSend("/topic/quorum/" + memberDTO.getMeetingId(), memberDTOS);
                    }
                );
            return memberDTO;
        } else {
            throw new ResourceNotFoundException("Member not found my ID: " + memberId);
        }
    }

    public MemberDTO changeRemotely(Long memberId, Boolean remotely) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setRemotely(remotely);
            memberRepository.save(member);
            MemberDTO memberDTO = memberMapper.memberToMemberDTO(member);
            memberRepository
                .findAllByMeetingIdAndFromReestrTrue(memberDTO.getMeetingId())
                .ifPresent(
                    members -> {
                        List<MemberDTO> memberDTOS = memberMapper.membersToMemberDTOs(members);
                        messagingTemplate.convertAndSend("/topic/quorum/" + memberDTO.getMeetingId(), memberDTOS);
                    }
                );
            return memberDTO;
        } else {
            throw new ResourceNotFoundException("Member not found my ID: " + memberId);
        }
    }

    public void setStatusForMember(Long memberId, Boolean isOnline, String sessionId) {
        if (memberId != null && isOnline && sessionId != null) {
            Optional<Member> byId = memberRepository.findById(memberId);
            if (byId.isPresent()) {
                Member member = byId.get();
                member.setInvolved(true);
                Member savedMember = memberRepository.saveAndFlush(member);
                Optional<Meeting> meetingOptional = meetingRepository.findById(savedMember.getMeeting().getId());
                if (meetingOptional.isPresent()) {
                    Meeting meeting = meetingOptional.get();
                    memberSessionRepository
                        .findAllByMeetingIdAndMemberId(meeting.getId(), member.getId())
                        .ifPresent(memberSessionRepository::deleteAll);
                    memberSessionRepository.save(new MemberSession(meeting.getId(), sessionId, savedMember.getId(), false, null));
                    getMembersForOnlineList(meeting);
                } else {
                    throw new ResourceNotFoundException("Meeting not found by this ID: " + savedMember.getMeeting().getId());
                }
            } else {
                throw new ResourceNotFoundException("Member not found by this ID: " + memberId);
            }
        } else if (memberId == null && !isOnline && sessionId != null) {
            Optional<MemberSession> oneBySessionId = memberSessionRepository.findOneBySessionId(sessionId);
            if (oneBySessionId.isPresent()) {
                MemberSession memberSession = oneBySessionId.get();
                Optional<Member> byId = memberRepository.findById(memberSession.getMemberId());
                if (byId.isPresent()) {
                    Member member = byId.get();
                    member.setInvolved(false);
                    Member savedMember = memberRepository.save(member);
                    MemberTypeEnum memberTypeEnum = member.getMemberTypeEnum();
                    boolean isManager = memberTypeEnum.equals(MemberTypeEnum.SECRETARY) || memberTypeEnum.equals(MemberTypeEnum.CHAIRMAN);
                    if (memberSession.isZoom() && memberSession.getZoomPassword() != null && isManager) messagingTemplate.convertAndSend(
                        "/topic/get-zoom/" + member.getMeeting().getId(),
                        new MemberSession(
                            memberSession.getId(),
                            member.getMeeting().getId(),
                            memberSession.getSessionId(),
                            member.getId(),
                            false,
                            null
                        )
                    );
                    memberSessionRepository.delete(memberSession);
                    Optional<Meeting> meetingOptional = meetingRepository.findById(savedMember.getMeeting().getId());
                    if (meetingOptional.isPresent()) {
                        Meeting meeting = meetingOptional.get();
                        getMembersForOnlineList(meeting);
                    } else {
                        throw new ResourceNotFoundException("Meeting not found by this ID: " + savedMember.getMeeting().getId());
                    }
                } else {
                    throw new ResourceNotFoundException("Member not found for set Offline by ID: " + memberSession.getMemberId());
                }
            } else {
                throw new ResourceNotFoundException("Session not found by ID: " + sessionId);
            }
        } else {
            throw new ResourceNotFoundException("SessionId must not be null");
        }
    }

    private void getMembersForOnlineList(Meeting meeting) {
        Optional<List<Member>> allByMeetingId = memberRepository.findAllByMeetingId(meeting.getId());
        if (allByMeetingId.isPresent()) {
            List<Member> members = allByMeetingId.get();
            List<MemberDTO> memberDTOList = members.stream().map(MemberDTO::new).collect(Collectors.toList());
            messagingTemplate.convertAndSend("/topic/getMember/" + meeting.getId(), memberDTOList);
        } else {
            throw new ResourceNotFoundException("Member not found by Meeting ID: " + meeting.getId());
        }
    }

    public MemberDTO electChairmen(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent() && optionalMember.get().getFromReestr()) {
            boolean present = memberRepository
                .findFirstByMeetingIdAndMemberTypeEnumAndFromReestrTrue(optionalMember.get().getMeeting().getId(), MemberTypeEnum.CHAIRMAN)
                .isPresent();
            if (!present) {
                Member member = optionalMember.get();
                member.setMemberTypeEnum(MemberTypeEnum.CHAIRMAN);
                Member savedMember = memberRepository.saveAndFlush(member);
                Optional<User> optionalUser = companyRepository.findById(savedMember.getCompany().getId()).map(Company::getChairman);
                if (optionalUser.isPresent()) {
                    memberRepository.save(new Member(savedMember, optionalUser.get(), false));
                } else {
                    if (
                        !memberRepository
                            .findByMemberTypeEnumAndMeetingIdAndFromReestrFalse(MemberTypeEnum.CHAIRMAN, savedMember.getMeeting().getId())
                            .isPresent()
                    ) memberRepository.save(new Member(savedMember, savedMember.getUser(), false));
                }
                return memberMapper.memberToMemberDTO(savedMember);
            } else {
                throw new BadRequestAlertException("By this Meeting Already has Chairmen!", "memberManagement", "existChairmen");
            }
        } else {
            throw new BadRequestAlertException("Member must be fromReestr is TRUE", "memberManagement", "notFromReestr");
        }
    }

    public List<MemberTypeDTO> getMemberTypes(Long userId, Long meetingId) {
        return memberRepository
            .findAllByUserIdAndMeetingId(userId, meetingId)
            .stream()
            //            .filter(DistinctByKey.runT(Member::getMemberTypeEnum))
            .map(MemberTypeDTO::new)
            .collect(Collectors.toList());
    }
}
