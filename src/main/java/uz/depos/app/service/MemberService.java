package uz.depos.app.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.Member;
import uz.depos.app.repository.MeetingRepository;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.dto.MemberDTO;
import uz.depos.app.service.mapper.MemberMapper;

@Service
@Transactional
public class MemberService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberService(
        MeetingRepository meetingRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        MemberMapper memberMapper
    ) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    public MemberDTO createMember(MemberDTO memberDTO) {
        Member member = new Member();
        meetingRepository.findOneById(memberDTO.getMeetingId()).ifPresent(member::setMeeting);
        userRepository.findById(memberDTO.getUserId()).ifPresent(member::setUser);
        member.setRemotely(memberDTO.getRemotely());
        member.setConfirmed(false);
        member.setInvolved(false);
        member.setSpeaker(memberDTO.getSpeaker());

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
                        meetingRepository.findById(memberDTO.getMeetingId()).ifPresent(member::setMeeting);
                    }
                    if (memberDTO.getUserId() != null) {
                        userRepository.findById(memberDTO.getUserId()).ifPresent(member::setUser);
                    }
                    member.setRemotely(memberDTO.getRemotely());
                    member.setConfirmed(memberDTO.getConfirmed());
                    member.setInvolved(memberDTO.getInvolved());
                    member.setSpeaker(memberDTO.getSpeaker());
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
}
