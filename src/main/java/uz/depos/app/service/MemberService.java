package uz.depos.app.service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.matching;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.Member;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.MemberSearchFieldEnum;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.dto.MemberDTO;
import uz.depos.app.service.mapper.MemberMapper;

/**
 * Service class for managing member.
 */
@Service
@Transactional
public class MemberService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final CompanyRepository companyRepository;

    public MemberService(
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        MemberMapper memberMapper,
        CompanyRepository companyRepository
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.companyRepository = companyRepository;
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
        member.setSpeaker(memberDTO.getSpeaker());
        member.setChairmen(memberDTO.getChairmen());
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
                    member.setConfirmed(memberDTO.getConfirmed());
                    member.setInvolved(memberDTO.getInvolved());
                    member.setSpeaker(memberDTO.getSpeaker());
                    member.setChairmen(memberDTO.getChairmen());
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

        if (value == null) {
            return memberRepository.findAll(pageable).map(MemberDTO::new);
        } else {
            return memberRepository.findAll(Example.of(member, matcher), pageable).map(MemberDTO::new);
        }
    }
}
