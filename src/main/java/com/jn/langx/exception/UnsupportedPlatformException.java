package com.jn.langx.exception;

public class UnsupportedPlatformException extends RuntimeException {
    public UnsupportedPlatformException() {
        super();
    }

    public UnsupportedPlatformException(String message) {
        super(message);
    }

    public UnsupportedPlatformException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedPlatformException(Throwable cause) {
        super(cause);
    }
}
