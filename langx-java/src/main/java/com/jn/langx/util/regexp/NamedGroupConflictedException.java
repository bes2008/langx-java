package com.jn.langx.util.regexp;

import com.jn.langx.exception.SyntaxException;

/**
 * @since 4.5.0
 */
public class NamedGroupConflictedException extends SyntaxException {

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
