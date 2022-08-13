package com.jn.langx.security.auth.pam;

public class PamException extends RuntimeException{
    public PamException() {
    }

    public PamException(String message) {
        super(message);
    }

    public PamException(String message, Throwable cause) {
        super(message, cause);
    }

    public PamException(Throwable cause) {
        super(cause);
    }
}
