package ru.learning.task_mgt_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for setting up Swagger/OpenAPI documentation
 * for the Task Management System project.
 * <p>
 * This class defines the settings for generating API documentation,
 * including server and main API metadata.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates and configures an OpenAPI component for generating API documentation.
     *
     * @return a configured OpenAPI instance containing server information
     * and API metadata.
     */
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url("http://localhost:8081")
                        )
                )
                .info(
                        new Info()
                                .title("Task Management System API")
                                .version("v1.0")
                                .description("API documentation for the Task Management System project.")
                                .contact(new Contact()
                                        .name("Support Team")
                                        .email("dev@example.com")
                                        .url("https://dev.com/support")
                                )
                                .license(new License()
                                        .name("Apache 2.0")
                                        .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                                )
                );
    }
}