package tn.esprit.springfever.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.mappers.ModelMapper;

@Configuration
@OpenAPIDefinition(servers = {@Server (url = "http://localhost/admission")})
public class OpenAPIConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(infoAPI());

    }

    public Info infoAPI() {
        return new Info().title("EXAM - JAN - 2023")
                .description("This is a springboot exam")
                .contact(contactAPI());
    }

    public Contact contactAPI() {
        Contact contact = new Contact().name("Ahmed_Debbiche-4SE4")
                .email("ahmed.debbiche@esprit.tn")
                .url("https://github.com/Ahmed-Debbiche007");
        return contact;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}