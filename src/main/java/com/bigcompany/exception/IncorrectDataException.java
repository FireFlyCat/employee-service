package com.bigcompany.exception;

public class IncorrectDataException extends RuntimeException {
    public IncorrectDataException(String message) {
        super("Incorrect Data Exception: " + message);
    }
}
