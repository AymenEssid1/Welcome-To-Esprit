package tn.esprit.springfever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSpringConfigured
@EntityScan (basePackages = {"tn.esprit.springfever.entities"})
@EnableEurekaClient
@EnableScheduling
@EnableAspectJAutoProxy
public class ForumService {
    public static void main(String[] args) {
        SpringApplication.run(ForumService.class, args);
    }
}