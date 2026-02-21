package com.pratham.banking.exception;

/**
 * Exception thrown when attempting to create a duplicate resource.
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Creates a new duplicate-resource exception with the provided message.
     *
     * @param message the exception message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
