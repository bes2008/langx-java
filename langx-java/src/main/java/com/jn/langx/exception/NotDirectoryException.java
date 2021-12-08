package com.jn.langx.exception;

import java.io.IOException;

public class NotDirectoryException extends IOException {

    public NotDirectoryException() {
        super();
    }

    public NotDirectoryException(String message) {
        super(message);
    }

    public NotDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotDirectoryException(Throwable cause) {
        super(cause);
    }
}
