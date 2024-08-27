package ru.learning.task_mgt_system.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;

/**
 * Abstract base class for integration tests using Keycloak Testcontainers.
 * This class sets up a Keycloak container, configures the resource server,
 * and provides helper methods to obtain access tokens.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class KeycloakTestContainers {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakTestContainers.class.getName());

    @LocalServerPort
    private int port;

    static final KeycloakContainer keycloak;

    static {
        // Initialize the Keycloak container with a default configuration
        keycloak = new KeycloakContainer();
        keycloak.start();
    }

    /**
     * Initializes RestAssured's base URI with the random port assigned during test startup.
     */
    @PostConstruct
    public void init() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    /**
     * Dynamically registers the resource server's issuer URI property for the JWT configuration.
     * This allows Spring Security to validate tokens against the Keycloak issuer.
     *
     * @param registry The registry to which the issuer URI property is added.
     */
    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/taskmgt");
    }

    /**
     * Retrieves a Bearer token for the user 'pashtet_rus@mail.ru' from Keycloak.
     * The token is obtained by sending a POST request to the Keycloak token endpoint.
     *
     * @return The Bearer token as a String, or null if the token could not be obtained.
     */
    protected String getPavelBearer() {
        try {
            // Build the URI for the token request
            URI authorizationURI = new URIBuilder(keycloak.getAuthServerUrl()
                    + "/realms/taskmgt/protocol/openid-connect/token").build();

            // Configure the WebClient for making HTTP requests
            WebClient webClient = WebClient.builder()
                    .baseUrl(keycloak.getAuthServerUrl())
                    .build();

            // Prepare the form data for the token request
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "password");
            formData.add("client_id", "task-mgt-system");
            formData.add("username", "pashtet_rus@mail.ru");
            formData.add("password", "s3cr3t");

            // Send the request and parse the response
            String result = webClient.post()
                    .uri("/realms/taskmgt/protocol/openid-connect/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Extract the access token from the response
            JacksonJsonParser jsonParser = new JacksonJsonParser();
            return "Bearer " + jsonParser.parseMap(result).get("access_token").toString();
        } catch (URISyntaxException e) {
            LOGGER.error("Invalid URI for obtaining access token from Keycloak!", e);
        } catch (Exception e) {
            LOGGER.error("Failed to obtain an access token from Keycloak!", e);
        }

        return null;
    }
}