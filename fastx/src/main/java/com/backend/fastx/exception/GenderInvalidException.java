package com.backend.fastx.exception;

public class GenderInvalidException extends RuntimeException {
    public GenderInvalidException(String message) {
        super(message);
    }

    public GenderInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenderInvalidException(Throwable cause) {
        super(cause);
    }

    public GenderInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GenderInvalidException() {
    }
}
