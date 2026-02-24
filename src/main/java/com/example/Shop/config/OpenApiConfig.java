package com.example.Shop.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Shop API",
                version = "1.0",
                description = "API for managing products, users and orders",
                contact = @Contact(
                        name = "Rolland Gavrilita",
                        email = "gavrilita_rolland@yahoo.com"
                )
        )
)
public class OpenApiConfig {
}