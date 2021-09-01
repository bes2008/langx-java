package com.jn.langx.session.exception;


/**
 * Exception thrown when attempting to interact with the system under a session that has been
 * stopped.  A session may be stopped in any number of ways, most commonly due to explicit
 * stopping (e.g. from logging out), or due to expiration.
 *
 * @since 3.7.0
 */
public class StoppedSessionException extends InvalidSessionException {

    /**
     * Creates a new StoppedSessionException.
     */
    public StoppedSessionException() {
        super();
    }

    /**
     * Constructs a new StoppedSessionException.
     *
     * @param message the reason for the exception
     */
    public StoppedSessionException(String message) {
        super(message);
    }

    /**
     * Constructs a new StoppedSessionException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public StoppedSessionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new StoppedSessionException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public StoppedSessionException(String message, Throwable cause) {
        super(message, cause);
    }

}
