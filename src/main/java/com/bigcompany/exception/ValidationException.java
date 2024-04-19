package com.bigcompany.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super("Validation Exception: " + message);
    }
}
