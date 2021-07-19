package com.jn.langx.security.crypto.key.spec;

public class KeyFileFormatException extends RuntimeException{
    public KeyFileFormatException() {
        super();
    }
    public KeyFileFormatException(String message) {
        super(message);
    }

    public KeyFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyFileFormatException(Throwable cause) {
        super(cause);
    }
}
