package com.jn.langx.util.datetime;

/**
 * @since 4.5.2
 */
public class NotFoundDateTimeFormatterException extends RuntimeException{
    public NotFoundDateTimeFormatterException() {
        super();
    }

    public NotFoundDateTimeFormatterException(String message) {
        super(message);
    }

    public NotFoundDateTimeFormatterException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundDateTimeFormatterException(Throwable cause) {
        super(cause);
    }
}
