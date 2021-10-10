package uz.depos.app.web.websocket;

import static uz.depos.app.config.WebsocketConfiguration.IP_ADDRESS;

import java.security.Principal;
import java.time.Instant;
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
import uz.depos.app.service.QuestionService;
import uz.depos.app.service.dto.QuestionDTO;
import uz.depos.app.web.websocket.dto.ActivityDTO;

@Controller
public class TestWebSocketController implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(TestWebSocketController.class);

    private final SimpMessageSendingOperations messagingTemplate;

    //    private UserChatService userChatService;
    private QuestionService questionService;

    public TestWebSocketController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/topic/meeting-status") //keladi
    @SendTo("/topic/tracker/userId") //jo'natadi
    public QuestionDTO sendActivity(@Payload QuestionDTO questionDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        QuestionDTO savedQuestion = questionService.addQuestionByMember(questionDTO);
        //        activityDTO.setUserLogin(principal.getName());
        //        activityDTO.setSessionId(stompHeaderAccessor.getSessionId());
        //        activityDTO.setIpAddress(stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString());
        //        activityDTO.setTime(Instant.now());
        //
        //        log.debug("Sending user tracking data {}", activityDTO);
        return savedQuestion;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setSessionId(event.getSessionId());
        activityDTO.setPage("logout");
        messagingTemplate.convertAndSend("/topic/tracker", activityDTO);
    }
}
