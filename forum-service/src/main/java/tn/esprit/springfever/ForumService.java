package tn.esprit.springfever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableSpringConfigured
@EntityScan (basePackages = {"tn.esprit.springfever.entities"})
@EnableEurekaClient
@EnableScheduling
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"tn.esprit.springfever.*"})
@EnableWebSocket
public class ForumService {

    public static void main(String[] args) {
        SpringApplication.run(ForumService.class, args);
    }
}