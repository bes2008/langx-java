package com.jn.langx.exception;

public class IllegalParameterException extends IllegalArgumentException {
    public IllegalParameterException() {
        super();
    }

    public IllegalParameterException(ExceptionMessage exceptionMessage) {
        this(exceptionMessage.getMessage());
    }

    public IllegalParameterException(String s) {
        super(s);
    }

    public IllegalParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalParameterException(Throwable cause) {
        super(cause);
    }

}
