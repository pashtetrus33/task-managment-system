package ru.learning.task_mgt_system.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.learning.task_mgt_system.exception.AccessDeniedException;
import ru.learning.task_mgt_system.exception.EntityNotFoundException;
import ru.learning.task_mgt_system.web.dto.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the ExceptionHandlerController class.
 *
 * This test class verifies the behavior of the ExceptionHandlerController by
 * checking its responses to various exceptions. It ensures that the controller
 * handles exceptions and returns the appropriate HTTP status codes and error messages.
 */
class ExceptionHandlerControllerTest {

    private final ExceptionHandlerController exceptionHandlerController = new ExceptionHandlerController();

    /**
     * Tests the notFound method of ExceptionHandlerController.
     *
     * This test verifies that the notFound method returns a ResponseEntity with
     * status code 404 Not Found and the correct error message when an EntityNotFoundException
     * is provided.
     */
    @Test
    void notFound_ShouldReturnNotFoundResponse() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        ResponseEntity<ErrorResponse> response = exceptionHandlerController.notFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());  // Asserting the status code
        assertEquals("Entity not found", response.getBody().getErrorMessage());  // Asserting the error message
    }

    /**
     * Tests the accessDenied method of ExceptionHandlerController.
     *
     * This test verifies that the accessDenied method returns a ResponseEntity with
     * status code 403 Forbidden and the correct error message when an AccessDeniedException
     * is provided.
     */
    @Test
    void accessDenied_ShouldReturnForbiddenResponse() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");
        ResponseEntity<ErrorResponse> response = exceptionHandlerController.accessDenied(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());  // Asserting the status code
        assertEquals("Access denied", response.getBody().getErrorMessage());  // Asserting the error message
    }

    /**
     * Tests the handleSecurityException method of ExceptionHandlerController.
     *
     * This test verifies that the handleSecurityException method returns a ResponseEntity with
     * status code 403 Forbidden and the correct error message when a SecurityException
     * is provided.
     */
    @Test
    void handleSecurityException_ShouldReturnForbiddenResponse() {
        SecurityException ex = new SecurityException("Security exception");
        ResponseEntity<String> response = exceptionHandlerController.handleSecurityException(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());  // Asserting the status code
        assertEquals("Security exception", response.getBody());  // Asserting the error message
    }
}