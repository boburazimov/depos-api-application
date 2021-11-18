package uz.depos.app.web.websocket;

import static uz.depos.app.config.WebsocketConfiguration.IP_ADDRESS;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tech.jhipster.web.util.PaginationUtil;
import uz.depos.app.service.MeetingLoggingService;
import uz.depos.app.service.QuestionService;
import uz.depos.app.service.dto.MeetingLoggingDTO;
import uz.depos.app.service.dto.QuestionDTO;
import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.websocket.dto.ActivityDTO;

@Controller
public class ActivityService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private final MeetingLoggingService meetingLoggingService;
    private final QuestionService questionService;

    public ActivityService(
        SimpMessageSendingOperations messagingTemplate,
        MeetingLoggingService meetingLoggingService,
        QuestionService questionService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.meetingLoggingService = meetingLoggingService;
        this.questionService = questionService;
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
        System.out.println(event);
        System.out.println("DISCONNECT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setSessionId(event.getSessionId());
        activityDTO.setPage("logout");
        messagingTemplate.convertAndSend("/topic/tracker", activityDTO);
    }

    @MessageMapping("topic/user-all")
    @SendTo("/topic/user")
    public List<MeetingLoggingDTO> sendToAll(
        @Payload MeetingLoggingDTO loggingDTO,
        StompHeaderAccessor stompHeaderAccessor,
        Principal principal
    ) {
        log.debug("Sending user logging data {}", loggingDTO);
        log.debug("Sending user stompHeaderAccessor data {}", stompHeaderAccessor);
        log.debug("Sending user principal data {}", principal);

        MeetingLoggingDTO meetingLoggingDTO = meetingLoggingService.addMeetingLogging(loggingDTO);
        if (meetingLoggingDTO != null) {
            return meetingLoggingService.getAllLoggingsByMeeting(meetingLoggingDTO.getMeetingId());
        } else {
            throw new BadRequestAlertException("Error in save logging", "LoggingDTO", "LoggingUnsave");
        }
    }

    @MessageMapping("topic/question")
    @SendTo("/topic/answer")
    public List<QuestionDTO> WsQuestion(@Payload QuestionDTO questionDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
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
}
