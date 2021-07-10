package com.jn.langx.util.jar;

public class JarNotFoundException extends RuntimeException{
    public JarNotFoundException() {
        super();
    }

    public JarNotFoundException(String message) {
        super(message);
    }

    public JarNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public JarNotFoundException(Throwable cause) {
        super(cause);
    }
}
