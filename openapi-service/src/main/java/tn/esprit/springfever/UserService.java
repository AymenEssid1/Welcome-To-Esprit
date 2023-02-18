package tn.esprit.springfever;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@SpringBootApplication
@EnableSpringConfigured
@EntityScan (basePackages = {"tn.esprit.springfever.entities"})
@EnableEurekaClient
public class UserService {
    public static void main(String[] args) {
        SpringApplication.run(UserService.class, args);
    }
}