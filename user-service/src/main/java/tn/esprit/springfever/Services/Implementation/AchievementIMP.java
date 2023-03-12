package tn.esprit.springfever.Services.Implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Repositories.AchievmentRepo;
import tn.esprit.springfever.Services.Interface.AchievementService;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Achievement;
import tn.esprit.springfever.entities.User;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
@Slf4j
public class AchievementIMP implements AchievementService {

    @Value("${spring.rabbitmq.template.exchange.forum}")
    private String rabbitmqExchange;



    @Value("${spring.rabbitmq.template.routing-key.forum.top}")
    private String rabbitmqRoutingtop;
    @Autowired
    private RabbitTemplate amqpTemplate;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Long> getUserDetailsFromId() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString("top");
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = MessageBuilder
                .withBody(json.getBytes())
                .andProperties(messageProperties)
                .build();
        Message response = amqpTemplate.sendAndReceive(rabbitmqExchange, rabbitmqRoutingtop, message);
        List<Long> lista = null;
        if (response != null && response.getBody() != null && response.getBody().length > 0) {
            String jsonResponse = new String(response.getBody(), StandardCharsets.UTF_8);
            lista = objectMapper.readValue(jsonResponse, List.class);
        }
        log.info(lista.toString());
        return lista;
    }

    @Override
    public void createAchievementForTopThreeActiveUsers() {

    }
}
