package ru.learning.task_mgt_system.exception;

/**
 * Exception thrown when a requested entity is not found.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructor to create a new instance of {@link EntityNotFoundException}.
     *
     * @param message the message that will be displayed when the exception is thrown.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}