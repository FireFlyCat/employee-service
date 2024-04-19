package com.bigcompany.exception;

public class UnexpectedException extends RuntimeException {

    public UnexpectedException(String message) {
        super("Unexpected Exception: " + message);
    }

    public UnexpectedException(String message, Throwable cause) {
        super("Unexpected Exception: " + message, cause);
    }
}
