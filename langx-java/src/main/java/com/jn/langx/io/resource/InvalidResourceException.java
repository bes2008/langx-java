package com.jn.langx.io.resource;

public class InvalidResourceException extends RuntimeException {
    public InvalidResourceException() {
        super();
    }

    public InvalidResourceException(String message) {
        super(message);
    }

    public InvalidResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidResourceException(Throwable cause) {
        super(cause);
    }

}