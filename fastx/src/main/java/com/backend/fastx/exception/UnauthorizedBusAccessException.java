package com.backend.fastx.exception;

public class UnauthorizedBusAccessException extends RuntimeException {
    public UnauthorizedBusAccessException(String message) {
        super(message);
    }
}
