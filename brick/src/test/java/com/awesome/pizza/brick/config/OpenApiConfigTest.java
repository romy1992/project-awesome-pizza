package com.awesome.pizza.brick.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {
    @Test
    void customOpenAPI_shouldReturnOpenAPIBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OpenApiConfig.class);
        OpenAPI openAPI = context.getBean(OpenAPI.class);
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("Awesome Pizza API");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");
        assertThat(openAPI.getInfo().getDescription()).contains("gestione degli ordini");
        context.close();
    }
}

