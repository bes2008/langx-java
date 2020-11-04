package com.jn.langx.exception;

import java.io.IOException;

public class RuntimeIOException extends RuntimeException {

    public RuntimeIOException() {
        super();
    }

    public RuntimeIOException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public RuntimeIOException(String message) {
        super(message);
    }

    public RuntimeIOException(String message, IOException cause) {
        super(message, cause);
    }

    public RuntimeIOException(IOException cause) {
        super(cause);
    }

}
