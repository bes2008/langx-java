package com.jn.langx.exception;

/**
 * @since 4.5.0
 */
public class ParseException extends RuntimeException {

    public ParseException() {
        super();
    }

    public ParseException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }
}