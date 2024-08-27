package ru.learning.task_mgt_system.config;

// Import necessary classes from Spring framework
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// Marks this class as a configuration class for Spring
@Configuration
public class AppConfig {

    // Defines a bean named 'restTemplate' that will be managed by the Spring container
    @Bean
    public RestTemplate restTemplate() {
        // Returns a new instance of RestTemplate
        // RestTemplate is used for making HTTP requests in a synchronous manner
        return new RestTemplate();
    }
}