package com.jn.langx.util.jar;

import com.jn.langx.util.Strings;

public class JarVersionMismatchedException extends RuntimeException {
    public JarVersionMismatchedException(String... jarNames) {
        this(Strings.join(",", jarNames));
    }

    public JarVersionMismatchedException() {
        super();
    }

    public JarVersionMismatchedException(String message) {
        super(message);
    }

    public JarVersionMismatchedException(String message, Throwable cause) {
        super(message, cause);
    }

    public JarVersionMismatchedException(Throwable cause) {
        super(cause);
    }

}
