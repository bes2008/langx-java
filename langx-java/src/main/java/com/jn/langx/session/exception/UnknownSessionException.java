package com.jn.langx.session.exception;


/**
 * Exception thrown when attempting to interact with the system under the pretense of a
 * particular session (e.g. under a specific session id), and that session does not exist in
 * the system.
 *
 * @since 3.7.0
 */
public class UnknownSessionException extends InvalidSessionException {

    /**
     * Creates a new UnknownSessionException.
     */
    public UnknownSessionException() {
        super();
    }

    /**
     * Constructs a new UnknownSessionException.
     *
     * @param message the reason for the exception
     */
    public UnknownSessionException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnknownSessionException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public UnknownSessionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new UnknownSessionException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UnknownSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
