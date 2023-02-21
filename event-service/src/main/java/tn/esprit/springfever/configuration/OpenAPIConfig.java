package tn.esprit.springfever.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.web.bind.annotation.CrossOrigin;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import springfox.documentation.service.*;



@Configuration
@OpenAPIDefinition(servers = {@Server(url = "/event")})
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.APIKEY,
        bearerFormat = "JWT",
        scheme = "bearer"
)

public class OpenAPIConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private ApiInfo apiInfo() {
        return new ApiInfo("My REST API",
                "Some custom description of API.",
                "1.0",
                "Terms of service",
                new Contact("ahmed gouiaa", "https://www.baeldung.com", "ahmed.gouiaa@esprit.tn"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .consumes(Collections.singleton(MediaType.MULTIPART_FORM_DATA_VALUE))
                .produces(Collections.singleton(MediaType.APPLICATION_JSON_VALUE));
    }


    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

}