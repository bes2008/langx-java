package com.jn.langx.exception;

public class ExpressionParseException extends RuntimeException {
    public ExpressionParseException() {
        super();
    }

    public ExpressionParseException(String message) {
        super(message);
    }

    public ExpressionParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionParseException(Throwable cause) {
        super(cause);
    }
}
