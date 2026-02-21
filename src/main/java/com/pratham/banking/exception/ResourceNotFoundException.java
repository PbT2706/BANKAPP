package com.pratham.banking.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new resource-not-found exception with the provided message.
     *
     * @param message the exception message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
