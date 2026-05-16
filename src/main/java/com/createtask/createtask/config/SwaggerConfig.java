package com.createtask.createtask.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerConfig registers the OpenAPI metadata displayed in the Swagger UI.
 * Access Swagger at: http://localhost:8080/swagger-ui/index.html after starting the app.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI taskManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Management API")
                        .description("REST API for the Task Management Application — User, Role, and Role Mapping endpoints")
                        .version("1.0.0"));
    }
}