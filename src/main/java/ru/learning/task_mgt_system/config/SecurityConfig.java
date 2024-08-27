package ru.learning.task_mgt_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for setting up security in the application.
 * <p>
 * This configuration sets up security filters and rules for handling
 * authentication and authorization, as well as configuring OAuth2
 * and JWT settings.
 */
@Configuration
public class SecurityConfig {

    @Value("${app.jwk-set-uri}")
    private String jwkSetUri;

    /**
     * Configures the {@link SecurityFilterChain} for the application.
     * <p>
     * This method sets up the security rules for HTTP requests, including
     * disabling CSRF protection (suitable for APIs), permitting access to
     * the /auth endpoint without authentication, and requiring authentication
     * for all other requests. It also configures OAuth2 login and JWT resource
     * server settings.
     *
     * @param http the {@link HttpSecurity} object used to configure security settings.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs while configuring security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF protection for APIs
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth").permitAll() // Allow access to /auth endpoint without authentication
                                .anyRequest().authenticated() // Require authentication for all other requests
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .defaultSuccessUrl("/swagger-ui/index.html", true) // Redirect to Swagger UI on successful login
                                .failureUrl("/login?error") // Redirect to login page on failure
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(jwt ->
                                        jwt.decoder(jwtDecoder()) // Use the specified JWT decoder
                                )
                );

        return http.build();
    }

    /**
     * Provides a {@link JwtDecoder} bean for decoding JWT tokens.
     * <p>
     * This method sets up the JWT decoder with a URL to the public key
     * used for decoding JWT tokens. The public key is obtained from the
     * Keycloak server or other authorization server.
     *
     * @return the configured {@link JwtDecoder} bean.
     */

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}