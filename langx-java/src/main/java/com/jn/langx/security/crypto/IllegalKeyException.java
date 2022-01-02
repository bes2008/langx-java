package com.jn.langx.security.crypto;

import com.jn.langx.text.StringTemplates;

public class IllegalKeyException extends SecurityException {
    public IllegalKeyException() {
        super();
    }

    public IllegalKeyException(String algorithm, String key) {
        this(StringTemplates.formatWithPlaceholder("illegal {} key {}", algorithm, key));
    }

    public IllegalKeyException(String s) {
        super(s);
    }

    public IllegalKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalKeyException(Throwable cause) {
        super(cause);
    }
}
