
package tn.esprit.springfever.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;


@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitmqPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitmqPassword;

    @Value("${spring.rabbitmq.template.exchange.forum}")
    private String rabbitmqExchange;

    @Value("${spring.rabbitmq.template.routing-key.forum.token}")
    private String rabbitmqRoutingForumKey;
    @Value("${spring.rabbitmq.template.routing-key.forum.id}")
    private String rabbitmqRoutingForumId;

    @Value("${spring.rabbitmq.template.routing-key.forum.ids}")
    private String rabbitmqRoutingForumIds;

    @Value("${spring.rabbitmq.template.queue.forum}")
    private String rabbitmqRoutingForumQueue;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmqHost, rabbitmqPort);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public Queue requestQueue() {
        return new Queue(rabbitmqRoutingForumQueue, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(rabbitmqExchange);
    }

    @Bean
    public AmqpAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue forumQueue() {
        return QueueBuilder.durable(rabbitmqRoutingForumQueue).build();
    }

    @Bean
    public DirectExchange forumExchange() {
        return new DirectExchange(rabbitmqExchange);
    }

    @Bean
    public Binding forumTokenBinding(Queue forumQueue, DirectExchange forumExchange) {
        return BindingBuilder.bind(forumQueue).to(forumExchange).with(rabbitmqRoutingForumKey);
    }

    @Bean
    public Binding forumIdBinding(Queue forumQueue, DirectExchange forumExchange) {
        return BindingBuilder.bind(forumQueue).to(forumExchange).with(rabbitmqRoutingForumId);
    }

    @Bean
    public Binding forumIdsBinding(Queue forumQueue, DirectExchange forumExchange) {
        return BindingBuilder.bind(forumQueue).to(forumExchange).with(rabbitmqRoutingForumIds);
    }
}
