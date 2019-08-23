package com.jn.langx.exception;

import java.io.File;

public class FileExistsException extends RuntimeException {

    /**
     * Defines the serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default Constructor.
     */
    public FileExistsException() {
        super();
    }

    /**
     * Construct an instance with the specified message.
     *
     * @param message The error message
     */
    public FileExistsException(final String message) {
        super(message);
    }

    /**
     * Construct an instance with the specified file.
     *
     * @param file The file that exists
     */
    public FileExistsException(final File file) {
        super("File " + file + " exists");
    }

}
