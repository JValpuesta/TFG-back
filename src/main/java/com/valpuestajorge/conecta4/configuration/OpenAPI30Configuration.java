package com.valpuestajorge.conecta4.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class OpenAPI30Configuration {

    public OpenAPI openApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Autostocknuvex API")
                                .description("Autostock Nuvex API")
                                .version("v1.0")
                                .contact(null)
                                .termsOfService("TOC")
                                .license(new License().name("License").url("#")));
    }

    @Bean
    public OpenApiCustomiser customGlobalHeaders() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
            log.info("\nLanguage header for Swagger done\n");
            Parameter headerParameter = new HeaderParameter()
                    .name("lang")
                    .description("Language")
                    .required(false)
                    .example("es")
                    .schema(new io.swagger.v3.oas.models.media.StringSchema());
            operation.addParametersItem(headerParameter);
        }));
    }

    @Bean
    public GroupedOpenApi partida() {
        return GroupedOpenApi.builder()
                .group("Partida")
                .packagesToScan("com.vapuestajorge.conecta4.tablero")
                .build();
    }

    @Bean
    public GroupedOpenApi historial() {
        return GroupedOpenApi.builder()
                .group("Historial")
                .packagesToScan("com.vapuestajorge.conecta4.historial")
                .build();
    }

    @Bean
    public GroupedOpenApi movimiento() {
        return GroupedOpenApi.builder()
                .group("Movimiento")
                .packagesToScan("com.vapuestajorge.conecta4.movimiento")
                .build();
    }

}