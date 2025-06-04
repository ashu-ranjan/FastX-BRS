package com.backend.fastx.exception;

public class InvalidSeatTypeException extends RuntimeException {
    public InvalidSeatTypeException(String message) {
        super(message);
    }
}
