package com.jn.langx.security.crypto;

public class IllegalKeyException extends SecurityException {
    public IllegalKeyException() {
        super();
    }

    public IllegalKeyException(String s) {
        super(s);
    }

    public IllegalKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalKeyException(Throwable cause) {
        super(cause);
    }
}
