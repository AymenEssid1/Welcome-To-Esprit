package tn.esprit.springfever.services.interprocess;

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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.repositories.PostRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class RabbitMQMessageReceiver {

    /*@RabbitListener(queues = "myQueue")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }*/
    @Value("${spring.rabbitmq.template.exchange.forum}")
    private String rabbitmqExchange;



    @Value("${spring.rabbitmq.template.routing-key.forum.top}")
    private String rabbitmqRoutingtop;
    @Autowired
    private RabbitTemplate amqpTemplate;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private PostRepository postRepository;


    @RabbitListener(bindings = {
            @QueueBinding(value =
            @Queue(value = "${spring.rabbitmq.template.queue.forum}"), exchange = @Exchange("${spring.rabbitmq.template.exchange.forum}"), key = "${spring.rabbitmq.template.routing-key.forum.token}")})
    public Message receiveMessage(@Payload Message message, @Header("amqp_receivedRoutingKey") String routingKey) throws IOException {

        if (routingKey.equals(rabbitmqRoutingtop)) {
            return getUsersByIds(message);
        }
        return null;
    }


    public Message getUsersByIds(Message message) throws JsonProcessingException {
        String token = new String(message.getBody(), StandardCharsets.UTF_8);
        token = token.substring(1, token.length() - 1);
        List<Long> list=postRepository.countPostsByLikesAndComments();
        log.info(String.valueOf(list.size()));
        JSONArray jsonUsers = new JSONArray();
        for (Long i : list){
            jsonUsers.add(i);

            log.info(String.valueOf(i));

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
}
