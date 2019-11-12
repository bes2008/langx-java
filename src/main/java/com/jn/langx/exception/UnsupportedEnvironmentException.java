package com.jn.langx.exception;

public class UnsupportedEnvironmentException extends RuntimeException {
    public UnsupportedEnvironmentException() {
        super();
    }

    public UnsupportedEnvironmentException(String message) {
        super(message);
    }

    public UnsupportedEnvironmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedEnvironmentException(Throwable cause) {
        super(cause);
    }
}
