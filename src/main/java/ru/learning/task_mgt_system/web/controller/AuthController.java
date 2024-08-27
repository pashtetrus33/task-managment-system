package ru.learning.task_mgt_system.web.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.learning.task_mgt_system.exception.AuthenticationException;
import ru.learning.task_mgt_system.web.dto.UserRequest;

@Setter
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final RestTemplate restTemplate;

    @Value("${app.client-id}")
    private String clientId;

    @Value("${app.resource-url}")
    private String resourceServerUrl;

    @Value("${app.grant-type}")
    private String grantType;

    @Value("${app.secret}")
    private String secret;

    @PostMapping("/auth")
    public ResponseEntity<String> auth(
            @RequestBody @Valid @Parameter(description = "User credentials for authentication") UserRequest authDTO) {

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Build the request body using MultiValueMap
        var body = new LinkedMultiValueMap<String, String>();
        body.add("client_id", clientId);
        body.add("client_secret", secret);
        body.add("username", authDTO.getEmail());
        body.add("password", authDTO.getPassword());
        body.add("grant_type", grantType);

        var requestEntity = new HttpEntity<>(body, headers);

        try {
            // Send the request to the authentication server
            var response = restTemplate.exchange(
                    resourceServerUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Check response status and return token or throw exception
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String token = response.getBody();
                log.info("Successfully received token");
                return ResponseEntity.ok(token);
            } else {
                throw new AuthenticationException("Authentication failed with status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error during authentication with email: " + authDTO.getEmail(), e);
            throw new AuthenticationException("Error during authentication with email: " + authDTO.getEmail());
        }
    }
}