package com.jn.langx.text.i18n;

public class LanguageException extends Exception {
    public LanguageException() {
    }

    public LanguageException(String message) {
        super(message);
    }

    public LanguageException(Throwable cause) {
        super(cause);
    }

    public LanguageException(String message, Throwable cause) {
        super(message, cause);
    }
}