package ru.learning.task_mgt_system.exception;

/**
 * Exception thrown when access to a resource or operation is denied
 * due to insufficient permissions.
 */
public class AccessDeniedException extends RuntimeException {

    /**
     * Constructs a new {@link AccessDeniedException} with the specified detail message.
     *
     * @param message the detail message to be displayed when the exception is thrown.
     */
    public AccessDeniedException(String message) {
        super(message);
    }
}