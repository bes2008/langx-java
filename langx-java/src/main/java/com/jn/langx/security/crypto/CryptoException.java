package com.jn.langx.security.crypto;

import com.jn.langx.security.SecurityException;

public class CryptoException extends SecurityException {
    public CryptoException() {
        super();
    }
    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoException(Throwable cause) {
        super(cause);
    }
}
