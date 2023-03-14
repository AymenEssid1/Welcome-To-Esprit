package tn.esprit.springfever.batch;


import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.entities.Role;
import tn.esprit.springfever.entities.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ProjectWriter implements ItemWriter<User> {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange.forum}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key.forum.daily}")
    private String routingkey;
    public void write(List<? extends User> Users) {
        JSONArray jsonUsers = new JSONArray();
        for (User user : Users){
            JSONObject obj = new JSONObject();
            JSONArray jsonRoles = new JSONArray();
            if (user != null) {
                obj.put("id", user.getUserid());
                obj.put("username", user.getUsername());
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
        rabbitTemplate.convertAndSend(exchange, routingkey, jsonText);
    }
}