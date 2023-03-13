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


    @Autowired
    private RabbitTemplate amqpTemplate;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Long> getUserDetailsFromId() throws JsonProcessingException {
        return  null;
    }

    @Override
    public void createAchievementForTopThreeActiveUsers() {

    }
}
