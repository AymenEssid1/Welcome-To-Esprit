package tn.esprit.springfever.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(servers = {@Server (url = "http://localhost:8181/admission")})
        public class OpenAPIConfig {

        @Bean
        public OpenAPI OpenAPI() {
        return new OpenAPI()
        .info(infoAPI());

        }

        public Info infoAPI() {
        return new Info().title("SPRING FEVER")
        .description("This is a springboot project")
        .contact(contactAPI());
        }

        public Contact contactAPI() {
        Contact contact = new Contact().name("Mondher-Souissi-4SE4")
        .email("mondher.souissi@esprit.tn")
        .url("https://gitlab.com/ahmedg99/welcome-to-esprit");
        return contact;
        }

        }