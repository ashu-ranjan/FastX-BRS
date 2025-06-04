package com.backend.fastx.exception;

public class RoleInvalidException extends RuntimeException {
    public RoleInvalidException(String message) {
        super(message);
    }
}
