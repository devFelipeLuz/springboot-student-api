package br.com.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Sistema escolar API",
                version = "1.0",
                description = "API para gerenciamento escolar"
        )
)
public class OpenApiConfig {
}
