package tn.esprit.springfever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class AdmissionApplication {

    public static void main( String[] args) {
        SpringApplication.run(AdmissionApplication.class, args);
    }

}
