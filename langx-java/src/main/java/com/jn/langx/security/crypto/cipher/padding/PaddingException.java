package com.jn.langx.security.crypto.cipher.padding;

import com.jn.langx.security.SecurityException;

public class PaddingException extends SecurityException {
    public PaddingException() {
        super();
    }

    public PaddingException(String message) {
        super(message);
    }

    public PaddingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaddingException(Throwable cause) {
        super(cause);
    }
}
