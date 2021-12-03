package uz.depos.app.web.websocket;

import static uz.depos.app.config.WebsocketConfiguration.IP_ADDRESS;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import uz.depos.app.domain.MemberSession;
import uz.depos.app.domain.enums.MemberTypeEnum;
import uz.depos.app.repository.MemberRepository;
import uz.depos.app.repository.MemberSessionRepository;
import uz.depos.app.service.MeetingLoggingService;
import uz.depos.app.service.MemberService;
import uz.depos.app.service.QuestionService;
import uz.depos.app.service.dto.MeetingLoggingDTO;
import uz.depos.app.service.dto.MemberSessionDTO;
import uz.depos.app.service.dto.QuestionDTO;
import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.websocket.dto.ActivityDTO;
import uz.depos.app.web.websocket.dto.ZoomDTO;

@Controller
public class ActivityService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private final MeetingLoggingService meetingLoggingService;
    private final QuestionService questionService;
    private final MemberService memberService;
    private final MemberSessionRepository memberSessionRepository;
    private final MemberRepository memberRepository;

    public ActivityService(
        SimpMessageSendingOperations messagingTemplate,
        MeetingLoggingService meetingLoggingService,
        QuestionService questionService,
        MemberService memberService,
        MemberSessionRepository memberSessionRepository,
        MemberRepository memberRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.meetingLoggingService = meetingLoggingService;
        this.questionService = questionService;
        this.memberService = memberService;
        this.memberSessionRepository = memberSessionRepository;
        this.memberRepository = memberRepository;
    }

    @MessageMapping("/topic/activity")
    @SendTo("/topic/tracker")
    public ActivityDTO sendActivity(@Payload ActivityDTO activityDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        activityDTO.setUserLogin(principal.getName());
        activityDTO.setSessionId(stompHeaderAccessor.getSessionId());
        activityDTO.setIpAddress(stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString());
        activityDTO.setTime(Instant.now());
        log.debug("Sending user tracking data {}", activityDTO);
        return activityDTO;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        memberSessionRepository
            .findOneBySessionId(event.getSessionId())
            .ifPresent(
                memberSession -> {
                    memberService.setStatusForMember(null, false, memberSession.getSessionId());
                }
            );
        System.out.println(">>>>>>>>>>>>> DISCONNECT !!!!");
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setSessionId(event.getSessionId());
        activityDTO.setPage("logout");
        messagingTemplate.convertAndSend("/topic/tracker", activityDTO);
    }

    @MessageMapping("/topic/user-all")
    @SendTo("/topic/user")
    public List<MeetingLoggingDTO> sendToAll(@Payload MeetingLoggingDTO loggingDTO) {
        log.debug("Save Meeting logging data {}", loggingDTO);
        MeetingLoggingDTO meetingLoggingDTO = meetingLoggingService.addMeetingLogging(loggingDTO);
        if (meetingLoggingDTO != null) {
            return meetingLoggingService.getAllLoggingsByMeeting(meetingLoggingDTO.getMeetingId());
        } else {
            throw new BadRequestAlertException("Error in save logging", "LoggingDTO", "LoggingUnsaved");
        }
    }

    @MessageMapping("/topic/start-zoom")
    @SendTo("/topic/get-zoom")
    public MemberSession zoom(@Payload ZoomDTO zoomDTO) {
        log.debug("Start and Stop Zoom-Meeting logging data {}", zoomDTO);
        if (
            zoomDTO.getMeetingId() != null && zoomDTO.getMemberId() != null && memberRepository.findById(zoomDTO.getMemberId()).isPresent()
        ) { // General check
            MemberTypeEnum memberTypeEnum = memberRepository.findById(zoomDTO.getMemberId()).get().getMemberTypeEnum();
            boolean isManager = memberTypeEnum.equals(MemberTypeEnum.SECRETARY) || memberTypeEnum.equals(MemberTypeEnum.CHAIRMAN);
            Optional<MemberSession> optionalManagerMemberSession = memberSessionRepository.findOneByMeetingIdAndZoomIsTrueAndZoomPasswordNotNull(
                zoomDTO.getMeetingId()
            );
            Optional<MemberSession> optionalCurrentMemberSession = memberSessionRepository.findOneByMemberId(zoomDTO.getMemberId());

            if (
                zoomDTO.isZoom() &&
                StringUtils.isNotEmpty(zoomDTO.getPassword()) &&
                optionalCurrentMemberSession.isPresent() &&
                isManager &&
                !optionalManagerMemberSession.isPresent()
            ) { // <------Secretary or Chairmen created Zoom----->
                MemberSession memberSession = optionalCurrentMemberSession.get();
                memberSession.setZoom(true);
                memberSession.setZoomPassword(zoomDTO.getPassword());
                return memberSessionRepository.saveAndFlush(memberSession);
            } else if (
                !zoomDTO.isZoom() &&
                StringUtils.isEmpty(zoomDTO.getPassword()) &&
                isManager &&
                optionalCurrentMemberSession.isPresent() &&
                optionalManagerMemberSession.isPresent() &&
                optionalManagerMemberSession.get().getSessionId().equals(optionalCurrentMemberSession.get().getSessionId())
            ) { // <-----Secretary or Chairmen finished Zoom----->
                memberSessionRepository
                    .findAllByMeetingIdAndZoomIsTrueAndZoomPasswordNotNull(zoomDTO.getMeetingId())
                    .ifPresent(
                        memberSessions -> {
                            memberSessions.forEach(
                                memberSession -> {
                                    memberSession.setZoom(false);
                                    memberSession.setZoomPassword(null);
                                    memberSessionRepository.save(memberSession);
                                }
                            );
                        }
                    );
                MemberSession memberSession = optionalCurrentMemberSession.get();
                memberSession.setZoom(false);
                memberSession.setZoomPassword(null);
                return memberSessionRepository.saveAndFlush(memberSession);
            } else if (
                !zoomDTO.isZoom() &&
                StringUtils.isEmpty(zoomDTO.getPassword()) &&
                optionalCurrentMemberSession.isPresent() &&
                optionalManagerMemberSession.isPresent() &&
                !optionalManagerMemberSession.get().getSessionId().equals(optionalCurrentMemberSession.get().getSessionId())
            ) { // <-----Secretary or Chairmen or Simple joining to Zoom----->
                MemberSession memberSession = optionalCurrentMemberSession.get();
                memberSession.setZoom(true);
                memberSession.setZoomPassword(optionalManagerMemberSession.get().getZoomPassword());
                return memberSessionRepository.saveAndFlush(memberSession);
            } else if (
                !zoomDTO.isZoom() &&
                StringUtils.isEmpty(zoomDTO.getPassword()) &&
                optionalCurrentMemberSession.isPresent() &&
                !optionalManagerMemberSession.isPresent()
            ) {
                return optionalCurrentMemberSession.get();
            } else {
                throw new BadRequestAlertException("Please check all fields in ZoomDTO", "ZoomDTOManagement", "fieldsHasSomeError");
            }
        } else {
            throw new BadRequestAlertException("MemberId or MeetingId must not be null", "MemberSessionManagement", "IdNull");
        }
    }

    @MessageMapping("/topic/question")
    @SendTo("/topic/answer")
    public List<QuestionDTO> WsQuestion(@Payload QuestionDTO questionDTO) {
        log.debug("Save user question data {}", questionDTO);

        if (questionDTO != null && questionDTO.getUserId() == null && questionDTO.getMemberId() != null) {
            QuestionDTO questionDTO1 = questionService.addQuestionByMember(questionDTO);
            return questionService.getQuestionsByMeetingId(questionDTO1.getMeetingId());
        } else if (questionDTO != null && questionDTO.getUserId() != null) {
            QuestionDTO questionDTO1 = questionService.updateQuestionAnswer(questionDTO).orElse(null);
            assert questionDTO1 != null;
            return questionService.getQuestionsByMeetingId(questionDTO1.getMeetingId());
        } else {
            throw new BadRequestAlertException("Error in save question", "QuestionDTO", "QuestionSaveError");
        }
    }

    @MessageMapping("/topic/setStatus")
    //    @SendTo("/topic/getMember")
    public void setMemberStatus(@Payload MemberSessionDTO memberSessionDTO, StompHeaderAccessor headerAccessor) {
        log.debug("Set member status by ID: {}", memberSessionDTO.getMemberId());

        if (memberSessionDTO.getMemberId() != null && headerAccessor.getSessionId() != null && memberSessionDTO.getOnline() != null) {
            memberService.setStatusForMember(memberSessionDTO.getMemberId(), memberSessionDTO.getOnline(), headerAccessor.getSessionId());
        } else {
            throw new BadRequestAlertException("Error in set member status", "MemberDTO", "setStatusError");
        }
    }
}
