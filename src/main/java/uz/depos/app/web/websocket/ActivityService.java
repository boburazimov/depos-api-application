package uz.depos.app.web.websocket;

import static uz.depos.app.config.WebsocketConfiguration.IP_ADDRESS;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import uz.depos.app.service.MeetingLoggingService;
import uz.depos.app.service.dto.MeetingLoggingDTO;
import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.websocket.dto.ActivityDTO;

@Controller
public class ActivityService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private final MeetingLoggingService meetingLoggingService;

    public ActivityService(SimpMessageSendingOperations messagingTemplate, MeetingLoggingService meetingLoggingService) {
        this.messagingTemplate = messagingTemplate;
        this.meetingLoggingService = meetingLoggingService;
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
    //
    //    @MessageMapping("topic/user1")
    //    @SendTo("/topic/user1")
    //    public List<MeetingLoggingDTO> sendToAll1(@Payload MeetingLoggingDTO loggingDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
    //        log.debug("Sending user logging data {}", loggingDTO);
    //        log.debug("Sending user stompHeaderAccessor data {}", stompHeaderAccessor);
    //        log.debug("Sending user principal data {}", principal);
    //
    //        MeetingLoggingDTO meetingLoggingDTO = meetingLoggingService.addMeetingLogging(loggingDTO);
    //        if (meetingLoggingDTO != null) {
    //            return meetingLoggingService.getAllLoggingsByMeeting(meetingLoggingDTO.getMeetingId());
    //        } else {
    //            throw new BadRequestAlertException("Error in save logging", "LoggingDTO", "LoggingUnsave");
    //        }
    //    }
}
