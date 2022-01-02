package com.jn.langx.security.crypto;

public class InvalidKeyException extends SecurityException {
    public InvalidKeyException() {
        super();
    }

    public InvalidKeyException(String s) {
        super(s);
    }

    public InvalidKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidKeyException(Throwable cause) {
        super(cause);
    }
}
