package com.awesome.pizza.brick.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Awesome Pizza API")
                        .version("1.0.0")
                        .description("Documentazione delle API per la gestione degli ordini e pizze di Awesome Pizza."));
    }
}

