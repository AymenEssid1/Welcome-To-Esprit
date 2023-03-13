package tn.esprit.springfever.Services.interprocess;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.Repositories.UserRepo;
import tn.esprit.springfever.entities.Role;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.Security.jwt.JwtUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RabbitMQMessageReceiver {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RabbitTemplate amqpTemplate;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.rabbitmq.template.routing-key.forum.id}")
    private String routingKeyForumId;
    @Value("${spring.rabbitmq.template.routing-key.forum.ids}")
    private String routingKeyForumIds;
    @Value("${spring.rabbitmq.template.routing-key.forum.token}")
    private String routingKeyForumToken;

    @Value("${spring.rabbitmq.template.routing-key.admission.disponible}")
    private String routingForumTop;
    @Value("${spring.rabbitmq.template.routing-key.admission.indisponible}")
    private String routingAdmission;

    @Cacheable("user")
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
        if (routingKey.equals(routingKeyForumIds)) {
            return getUsersByIds(message);
        }

        if (routingKey.equals(routingForumTop)) {
            return getAvailableUsers(message);
        }

        if (routingKey.equals(routingAdmission)) {
            setDisponibility(message);
        }
        return null;
    }

    @Cacheable("user")
    public Message getUserById(Message message) {
        String token = new String(message.getBody(), StandardCharsets.UTF_8);
        token = token.substring(1, token.length() - 1);
        String jsonText = null;
        if (redisTemplate.opsForValue().get("user" + token) != null) {
            jsonText = redisTemplate.opsForValue().get("user" + token).toString();
        } else {
            Long id = Long.valueOf(token);
            User user = userRepo.findById(id).orElse(null);
            if (user != null) {
                JSONObject obj = new JSONObject();
                JSONArray jsonRoles = new JSONArray();
                obj.put("id", user.getUserid());
                obj.put("username", user.getUsername());
                obj.put("etatUser", user.getEtatUser());
                obj.put("email", user.getEmail());
                for (Role role : user.getRoles()) {
                    jsonRoles.add(role.getRolename());
                }
                obj.put("roles", jsonRoles);
                if (user.getImage() != null) {
                    obj.put("image", user.getImage().getLocation());
                } else {
                    obj.put("image", null);
                }
                jsonText = obj.toJSONString();
            } else {
                return null;
            }
        }
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message response = MessageBuilder
                .withBody(jsonText.getBytes())
                .andProperties(messageProperties)
                .build();
        redisTemplate.opsForValue().set("user" + token, jsonText);
        return response;
    }


    @Cacheable("user")
    public Message getUserByToken(Message message) {
        String token = new String(message.getBody(), StandardCharsets.UTF_8);
        token = token.substring(1, token.length() - 1);
        try {
            User user = jwtUtils.getUserFromUserName(jwtUtils.getUserNameFromJwtToken(token));
            JSONObject obj = new JSONObject();
            JSONArray jsonRoles = new JSONArray();
            obj.put("id", user.getUserid());
            obj.put("username", user.getUsername());
            obj.put("etatUser", user.getEtatUser());
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

    @Cacheable("user")
    public Message getUsersByIds(Message message) throws JsonProcessingException {
        String token = new String(message.getBody(), StandardCharsets.UTF_8);
        token = token.substring(1, token.length() - 1);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> list = objectMapper.readValue(token, new TypeReference<List<Long>>() {
        });
        JSONArray jsonUsers = new JSONArray();
        for (Long i : list) {
            User user = userRepo.findById(i).orElse(null);
            JSONObject obj = new JSONObject();
            JSONArray jsonRoles = new JSONArray();
            if (user != null) {
                obj.put("id", user.getUserid());
                obj.put("username", user.getUsername());
                obj.put("etatUser", user.getEtatUser());
                obj.put("email", user.getEmail());
                for (Role role : user.getRoles()) {
                    jsonRoles.add(role.getRolename());
                }
                obj.put("roles", jsonRoles);
                if (user.getImage() != null) {
                    obj.put("image", user.getImage().getLocation());
                } else {
                    obj.put("image", null);
                }
                jsonUsers.add(obj);
            }
        }
        String jsonText = jsonUsers.toJSONString();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message response = MessageBuilder
                .withBody(jsonText.getBytes())
                .andProperties(messageProperties)
                .build();
        return response;
    }

    @Cacheable("user")
    public Message getAvailableUsers(Message message) throws JsonProcessingException {
        String token = new String(message.getBody(), StandardCharsets.UTF_8);
        token = token.substring(1, token.length() - 1);
        List<User> list = userRepo.findByEtatUser("disponible");
        JSONArray jsonUsers = new JSONArray();
        for (User user : list) {
            JSONObject obj = new JSONObject();
            JSONArray jsonRoles = new JSONArray();
            obj.put("id", user.getUserid());
            obj.put("username", user.getUsername());
            obj.put("etatUser", user.getEtatUser());
            obj.put("email", user.getEmail());
            for (Role role : user.getRoles()) {
                jsonRoles.add(role.getRolename());
            }
            obj.put("roles", jsonRoles);
            if (user.getImage() != null) {
                obj.put("image", user.getImage().getLocation());
            } else {
                obj.put("image", null);
            }
            jsonUsers.add(obj);
        }
        String jsonText = jsonUsers.toJSONString();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message response = MessageBuilder
                .withBody(jsonText.getBytes())
                .andProperties(messageProperties)
                .build();
        return response;
    }

    @Cacheable("user")
    public void setDisponibility(Message message){
        String token = new String(message.getBody(), StandardCharsets.UTF_8);
        token = token.substring(1, token.length() - 1);
        Long id = Long.valueOf(token);
        User u = userRepo.findById(id).orElse(null);
        if ( u != null){
            u.setEtatUser("non disponible");
            userRepo.save(u);
        }
    }
}

