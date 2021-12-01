package uz.depos.app.web.websocket;

import static uz.depos.app.config.WebsocketConfiguration.IP_ADDRESS;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
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
import uz.depos.app.repository.MemberSessionRepository;
import uz.depos.app.service.MeetingLoggingService;
import uz.depos.app.service.MemberService;
import uz.depos.app.service.QuestionService;
import uz.depos.app.service.dto.MeetingLoggingDTO;
import uz.depos.app.service.dto.MemberSessionDTO;
import uz.depos.app.service.dto.QuestionDTO;
import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.websocket.dto.ActivityDTO;

@Controller
public class ActivityService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private final MeetingLoggingService meetingLoggingService;
    private final QuestionService questionService;
    private final MemberService memberService;
    private final MemberSessionRepository memberSessionRepository;

    public ActivityService(
        SimpMessageSendingOperations messagingTemplate,
        MeetingLoggingService meetingLoggingService,
        QuestionService questionService,
        MemberService memberService,
        MemberSessionRepository memberSessionRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.meetingLoggingService = meetingLoggingService;
        this.questionService = questionService;
        this.memberService = memberService;
        this.memberSessionRepository = memberSessionRepository;
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
