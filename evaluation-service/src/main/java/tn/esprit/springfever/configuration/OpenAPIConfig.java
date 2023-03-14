package tn.esprit.springfever.configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
@OpenAPIDefinition(servers = {@Server(url = "/evaluation")})
public class OpenAPIConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        SecurityConfiguration securityConfiguration = new SecurityConfiguration();
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-jwt", securityConfiguration.securityScheme()))
                .info(infoAPI())
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
    }
    public Info infoAPI() {
        return new Info().title("Evaluation Microservice")
                .description("This is a welcome to esprit project")
                .contact(contactAPI());
    }
    public Contact contactAPI() {
        Contact contact = new Contact().name("ahmed-gouiaa-4SE4")
                .email("ahmed.gouiaa@esprit.tn")
                .url("https://github.com/ahmedg99");
        return contact;
    }
}
