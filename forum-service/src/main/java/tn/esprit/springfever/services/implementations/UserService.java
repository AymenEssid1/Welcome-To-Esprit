package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.dto.RoleDTO;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.services.interfaces.IUserService;

@Service
@Slf4j
public class UserService implements IUserService {

    @Value("${spring.rabbitmq.template.exchange}")
    private String rabbitmqExchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String rabbitmqRoutingKey;
    @Autowired
    private RabbitTemplate amqpTemplate;

    // Send JWT token to User Service and wait for response
    public UserDTO getUserDetailsFromToken(String token) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(token.substring("Bearer ".length()));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = MessageBuilder
                .withBody(json.getBytes())
                .andProperties(messageProperties)
                .build();
        Message response = amqpTemplate.sendAndReceive(rabbitmqExchange, rabbitmqRoutingKey, message);
        // Wait for response with user details
        UserDTO userDetails = null;
        if (response != null && response.getBody() != null && response.getBody().length > 0) {
            String jsonResponse = (String)amqpTemplate.getMessageConverter().fromMessage(response);
            userDetails = objectMapper.readValue(jsonResponse, UserDTO.class);
        }
        return userDetails;
    }
}
