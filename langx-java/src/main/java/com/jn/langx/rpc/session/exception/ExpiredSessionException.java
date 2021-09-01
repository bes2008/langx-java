package com.jn.langx.rpc.session.exception;


/**
 * A special case of a StoppedSessionException.  An expired session is a session that has
 * stopped explicitly due to inactivity (i.e. time-out), as opposed to stopping due to log-out or
 * other reason.
 *
 * @since 3.7.0
 */
public class ExpiredSessionException extends StoppedSessionException {

    /**
     * Creates a new ExpiredSessionException.
     */
    public ExpiredSessionException() {
        super();
    }

    /**
     * Constructs a new ExpiredSessionException.
     *
     * @param message the reason for the exception
     */
    public ExpiredSessionException(String message) {
        super(message);
    }

    /**
     * Constructs a new ExpiredSessionException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public ExpiredSessionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new ExpiredSessionException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public ExpiredSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
