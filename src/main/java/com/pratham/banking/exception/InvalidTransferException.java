package com.pratham.banking.exception;

/**
 * Exception thrown when a transfer request is invalid (for example, same source and destination account).
 */
public class InvalidTransferException extends RuntimeException {

    /**
     * Creates a new invalid-transfer exception with the provided message.
     *
     * @param message the exception message
     */
    public InvalidTransferException(String message) {
        super(message);
    }
}
