package tn.esprit.springfever.services.interprocess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.UserService;
import tn.esprit.springfever.entities.Role;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.security.jwt.JwtUtils;
import tn.esprit.springfever.services.interfaces.IUserService;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class RabbitMQMessageReceiver {

    private String jwtSecret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    @Autowired
    private JwtUtils jwtUtils;

    @RabbitListener(queues = "${spring.rabbitmq.template.routing-key}")
    public Message receiveMessage(@Payload Message message) throws IOException {
        String token = new String(message.getBody(), StandardCharsets.UTF_8);
        token = token.substring(1, token.length() - 1);
        User user = jwtUtils.getUserFromUserName(jwtUtils.getUserNameFromJwtToken(token));
        JSONObject obj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        obj.put("id", user.getId());
        obj.put("username", user.getUsername());
        for (Role role : user.getRoles()) {
            jsonArray.add(role.getName());
        }
        obj.put("roles", jsonArray);
        StringWriter out = new StringWriter();
        obj.writeJSONString(out);
        String jsonText = out.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(jsonText);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message response = MessageBuilder
                .withBody(json.getBytes())
                .andProperties(messageProperties)
                .build();
        return response;
    }
}
