package tn.esprit.springfever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NotificationService {

    public static void main(String[] args) {
        SpringApplication.run(NotificationService.class, args);
    }

}