package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.dto.UserDTO;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Value("${spring.rabbitmq.template.exchange.notification}")
    private String rabbitmqExchange;
    @Value("${spring.rabbitmq.template.routing-key.notification}")
    private String rabbitmqRoutingKey;
    @Autowired
    private RabbitTemplate amqpTemplate;


    @Cacheable("user")
    public void sendNotification(String body, String sender, String receiver) throws JsonProcessingException {
        JSONObject obj = new JSONObject();
        obj.put("message", body);
        obj.put("sender", sender);
        obj.put("receiver", receiver);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = MessageBuilder
                .withBody(obj.toJSONString().getBytes())
                .andProperties(messageProperties)
                .build();
        amqpTemplate.convertAndSend(rabbitmqExchange, rabbitmqRoutingKey, message);
    }

}