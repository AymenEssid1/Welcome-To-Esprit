package tn.esprit.springfever.apigateway;

import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.*;

import java.time.Duration;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;


@Configuration
public class CorsConfiguration {
    @Bean
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (CorsUtils.isCorsRequest(request)) {
                HttpHeaders headers = new HttpHeaders();
                headers.setAccessControlAllowOrigin("*");
                headers.setAccessControlAllowHeaders(Arrays.asList("*"));
                headers.setAccessControlAllowMethods(Arrays.asList(HttpMethod.GET, HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PUT));
                headers.setAccessControlMaxAge(Duration.ofHours(1));

                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().addAll(headers);
            }

            return chain.filter(exchange);
        };
    }

}
