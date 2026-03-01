package com.pratham.banking.exception;

/**
 * Exception thrown when an idempotency key is reused with a different request payload.
 */
public class IdempotencyConflictException extends RuntimeException {

    /**
     * Creates a new idempotency-conflict exception with the provided message.
     *
     * @param message the exception message
     */
    public IdempotencyConflictException(String message) {
        super(message);
    }
}