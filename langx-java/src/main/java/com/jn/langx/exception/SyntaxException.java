package com.jn.langx.exception;

public class SyntaxException extends ParseException {

    public SyntaxException() {
        super();
    }

    public SyntaxException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyntaxException(Throwable cause) {
        super(cause);
    }
}