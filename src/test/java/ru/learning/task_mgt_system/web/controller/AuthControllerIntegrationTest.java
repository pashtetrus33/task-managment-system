package ru.learning.task_mgt_system.web.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import ru.learning.task_mgt_system.config.KeycloakTestContainers;
import ru.learning.task_mgt_system.web.dto.UserRequest;

/**
 * Integration tests for the AuthController class.
 *
 * This test class verifies the authentication process by sending HTTP requests
 * to the authentication endpoint and asserting the expected results.
 * It extends the KeycloakTestContainers class to ensure that Keycloak is properly
 * configured and running in a Docker container during the test execution.
 *
 * The @ActiveProfiles("test") annotation ensures that the test-specific configuration
 * is used when running these tests.
 */
@ActiveProfiles("test")
class AuthControllerIntegrationTest extends KeycloakTestContainers {

    /**
     * Tests the /auth endpoint for an authenticated user.
     *
     * This test sends a POST request to the /auth endpoint with a valid Bearer token
     * and checks if the response contains the correct user information, such as
     * username, lastname, firstname, and email.
     *
     * The getPavelBearer() method is expected to return a valid Bearer token for the user.
     */
    @Test
    void givenAuthenticatedUser_whenAuth_shouldReturnMyInfo() {

        given()
                .header("Authorization", getPavelBearer())  // Adds the Authorization header with a Bearer token
                .contentType(MediaType.APPLICATION_JSON_VALUE)  // Sets the content type to JSON
                .body(new UserRequest("pashtet_rus@mail.ru", "s3cr3t"))  // Adds the request body containing user credentials
                .when()
                .post("/auth")  // Sends a POST request to the /auth endpoint
                .then()
                .statusCode(HttpStatus.OK.value())  // Asserts that the response status is 200 OK
                .body("username", equalTo("pashtet_rus@mail.ru"))  // Asserts that the username in the response is correct
                .body("lastname", equalTo("Pavel"))  // Asserts that the lastname in the response is correct
                .body("firstname", equalTo("Bakanov"))  // Asserts that the firstname in the response is correct
                .body("email", equalTo("pashtet_rus@mail.ru"));  // Asserts that the email in the response is correct
    }
}