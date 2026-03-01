package com.pratham.banking.exception;

/**
 * Exception thrown when an account balance is insufficient for a requested debit operation.
 */
public class InsufficientBalanceException extends RuntimeException {

    /**
     * Creates a new insufficient-balance exception with the provided message.
     *
     * @param message the exception message
     */
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
