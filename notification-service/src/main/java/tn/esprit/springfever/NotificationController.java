package tn.esprit.springfever;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tn.esprit.springfever.entity.Notification;
import tn.esprit.springfever.repository.MessageRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Controller
@Slf4j
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRepository messageRepository;
    @Value("${spring.rabbitmq.template.routing-key.notification}")
    private String routingKey;

    @MessageMapping("/notification") // "/app/notification"
    @SendTo("/chatroom/public")
    public Notification receivePublicMessage(@Payload Notification notification) {
        notification.setTimestamps(LocalDateTime.now());
        notification.setReceiver("public");
        notification = messageRepository.save(notification);
        log.info(notification.toString());
        return notification;
    }

    @MessageMapping("/private-notification")
    public Notification receivePrivateMessage(@Payload Notification notification) {
        notification.setTimestamps(LocalDateTime.now());
        notification = messageRepository.save(notification);
        log.info(notification.toString());
        simpMessagingTemplate.convertAndSendToUser(notification.getReceiver(), "/private", notification); // "/user/username/private"

        return notification;
    }

    @RabbitListener(bindings = {
            @QueueBinding(value =
            @Queue(value = "${spring.rabbitmq.template.queue.notification}"), exchange = @Exchange("${spring.rabbitmq.template.exchange.notification}"), key = "${spring.rabbitmq.template.routing-key.notification}")})
    public void getNotification(@Payload Message message, @Header("amqp_receivedRoutingKey") String routingKey) throws JsonProcessingException {
        if (!routingKey.equals(routingKey)) {
            return;
        }
        log.info("getting the message");
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        Notification n = mapper.readValue(body, Notification.class);
        Notification notification = new Notification();
        notification.setMessage(n.getMessage());
        notification.setSender(n.getSender());
        notification.setReceiver(n.getReceiver());
        notification.setTimestamps(LocalDateTime.now());
        messageRepository.save(notification);
        simpMessagingTemplate.convertAndSendToUser(n.getReceiver(), "/private", messageRepository.save(notification));

    }
}
