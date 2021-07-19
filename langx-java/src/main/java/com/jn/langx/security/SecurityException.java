package com.jn.langx.security;

public class SecurityException  extends RuntimeException{
    public SecurityException() {
        super();
    }
    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }
}