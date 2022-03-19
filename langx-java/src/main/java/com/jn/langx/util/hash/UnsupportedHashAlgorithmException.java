package com.jn.langx.util.hash;

/**
 * @since 4.4.0
 */
public class UnsupportedHashAlgorithmException extends RuntimeException {

    public UnsupportedHashAlgorithmException() {
        super();
    }

    public UnsupportedHashAlgorithmException(String message) {
        super(message);
    }

    public UnsupportedHashAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedHashAlgorithmException(Throwable cause) {
        super(cause);
    }
}
