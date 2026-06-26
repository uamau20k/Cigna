package com.example.sucursales_service.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String s = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("API 2026 - Sucursales").version("1.0").description("Gestion de sucursales medicas"))
                .addSecurityItem(new SecurityRequirement().addList(s))
                .components(new Components().addSecuritySchemes(s, new SecurityScheme()
                        .name(s).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
