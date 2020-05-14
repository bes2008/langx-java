package com.jn.langx.configuration.file;

public class InvalidConfigurationFileException extends RuntimeException {
    public InvalidConfigurationFileException() {
        super();
    }

    public InvalidConfigurationFileException(String message) {
        super(message);
    }

    public InvalidConfigurationFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigurationFileException(Throwable cause) {
        super(cause);
    }

}
