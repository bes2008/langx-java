package com.jn.langx.exception;

public class IllegalPropertyException extends RuntimeException{
    public IllegalPropertyException() {
        super();
    }

    public IllegalPropertyException(String message) {
        super(message);
    }

    public IllegalPropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPropertyException(Throwable cause) {
        super(cause);
    }
}
