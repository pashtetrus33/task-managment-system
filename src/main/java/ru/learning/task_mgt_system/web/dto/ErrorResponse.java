package ru.learning.task_mgt_system.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for error responses.
 * Used to transmit error messages to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * The error message to be sent to the client.
     */
    private String errorMessage;
}