package tn.esprit.springfever;

  import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;





@SpringBootApplication
@EnableJpaRepositories("tn.esprit.springfever.repositories")

@ComponentScan(basePackages = {"tn.esprit.springfever.Controllers","tn.esprit.springfever.Services","tn.esprit.springfever.Configurations","tn.esprit.springfever.repositories","tn.esprit.springfever.Security","tn.esprit.springfever.payload"})

@EnableSpringConfigured
 @EnableScheduling
 @Configuration
 @EnableAspectJAutoProxy

  public class RecrutementService {

    public static void main(String[] args)  {
        SpringApplication.run(RecrutementService.class, args);





    }

}


