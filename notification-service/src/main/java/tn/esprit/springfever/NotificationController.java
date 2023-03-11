package tn.esprit.springfever;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tn.esprit.springfever.entity.Message;

@Controller
@Slf4j
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message") // "/app/message"
    @SendTo("/chatroom/public")
    public Message receivePublicMessage(@Payload Message message){
        log.info(message.toString());
        return message;
    }
    @MessageMapping("/private-message")
    public Message receivePrivateMessage(@Payload Message message){
        log.info(message.toString());
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private", message); // "/user/username/private"

        return message;
    }
}
