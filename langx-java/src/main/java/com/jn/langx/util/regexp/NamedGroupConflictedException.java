package com.jn.langx.util.regexp;

public class NamedGroupConflictedException extends RuntimeException{

    public NamedGroupConflictedException() {
        super();
    }

    public NamedGroupConflictedException(String message) {
        super(message);
    }

    public NamedGroupConflictedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NamedGroupConflictedException(Throwable cause) {
        super(cause);
    }

}
