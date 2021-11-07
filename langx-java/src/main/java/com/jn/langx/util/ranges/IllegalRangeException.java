package com.jn.langx.util.ranges;

public class IllegalRangeException extends RuntimeException {
    public IllegalRangeException() {
        super();
    }

    public IllegalRangeException(String message) {
        super(message);
    }
    public IllegalRangeException(String message, Throwable cause) {
        super(message, cause);
    }
    public IllegalRangeException(Throwable cause) {
        super(cause);
    }
}
