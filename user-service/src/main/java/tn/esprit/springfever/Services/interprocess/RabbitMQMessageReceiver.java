package tn.esprit.springfever.Services.interprocess;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.Repositories.UserRepo;
import tn.esprit.springfever.entities.Role;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.Security.jwt.JwtUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class RabbitMQMessageReceiver {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RabbitTemplate amqpTemplate;

    @Value("${spring.rabbitmq.template.routing-key.forum.id}")
    private String routingKeyForumId;
    @Value("${spring.rabbitmq.template.routing-key.forum.token}")
    private String routingKeyForumToken;

    @RabbitListener(bindings = {
            @QueueBinding(value =
            @Queue(value = "${spring.rabbitmq.template.queue.forum}"), exchange = @Exchange("${spring.rabbitmq.template.exchange.forum}"), key = "${spring.rabbitmq.template.routing-key.forum.token}")})
    public Message receiveMessage(@Payload Message message, @Header("amqp_receivedRoutingKey") String routingKey) throws IOException {
        if (routingKey.equals(routingKeyForumToken)) {
            return getUserByToken(message);
        }
        if (routingKey.equals(routingKeyForumId)) {
            return getUserById(message);
        }
        return null;
    }


    public Message getUserById(Message message) {
        String token = new String(message.getBody(), StandardCharsets.UTF_8);
        token = token.substring(1, token.length() - 1);
        Long id = Long.valueOf(1);
        User user = userRepo.findById(id).orElse(null);
        if (user != null) {
            JSONObject obj = new JSONObject();
            JSONArray jsonRoles = new JSONArray();
            obj.put("id", user.getUserid());
            obj.put("username", user.getUsername());
            for (Role role : user.getRoles()) {
                jsonRoles.add(role.getRolename());
            }
            obj.put("roles", jsonRoles);
            if (user.getImage() != null) {
                obj.put("image", user.getImage().getLocation());
            } else {
                obj.put("image", null);
            }
            String jsonText = obj.toJSONString();
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");
            Message response = MessageBuilder
                    .withBody(jsonText.getBytes())
                    .andProperties(messageProperties)
                    .build();
            return response;
        }
        return null;
    }

    public Message getUserByToken(Message message) {
        String token = new String(message.getBody(), StandardCharsets.UTF_8);
        token = token.substring(1, token.length() - 1);
        try {
            User user = jwtUtils.getUserFromUserName(jwtUtils.getUserNameFromJwtToken(token));
            JSONObject obj = new JSONObject();
            JSONArray jsonRoles = new JSONArray();
            obj.put("id", user.getUserid());
            obj.put("username", user.getUsername());
            for (Role role : user.getRoles()) {
                jsonRoles.add(role.getRolename());
            }
            obj.put("roles", jsonRoles);
            if (user.getImage() != null) {
                obj.put("image", user.getImage().getLocation());
            } else {
                obj.put("image", null);
            }
            String jsonText = obj.toJSONString();
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");
            Message response = MessageBuilder
                    .withBody(jsonText.getBytes())
                    .andProperties(messageProperties)
                    .build();
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
