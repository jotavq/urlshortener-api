package com.joaodev.urlshortener.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Definir informações gerais da API
                .info(new Info()
                        .title("URL Shortener API")
                        .version("1.0.0")
                        .description("API REST para encurtar URLs com autenticação JWT. " +
                                "Permite criar, gerenciar e redirecionar URLs encurtadas com controle de acesso.")
                        // Contato
                        .contact(new Contact()
                                .name("João Victor")
                                .email("jottavz@icloud.com")
                                .url("https://github.com/jotavq"))
                        // Informações de licença
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))

                // Adicionar Security Requirement globalmente
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                // Definir o Security Scheme para autenticação Bearer Token
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Token obtido após login. " +
                                                "Formato: Authorization: Bearer {token}")));
    }
}