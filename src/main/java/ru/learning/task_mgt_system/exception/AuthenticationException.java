package ru.learning.task_mgt_system.exception;

/**
 * Exception thrown when an authentication error occurs.
 */
public class AuthenticationException extends RuntimeException {

    /**
     * Constructs a new {@link AuthenticationException} with the specified detail message.
     *
     * @param message the detail message to be displayed when the exception is thrown.
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@link AuthenticationException} with the specified detail message and cause.
     *
     * @param message the detail message to be displayed when the exception is thrown.
     * @param cause   the cause of the exception, which can be retrieved later using {@link Throwable#getCause()}.
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}