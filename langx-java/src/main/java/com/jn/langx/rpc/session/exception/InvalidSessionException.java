package com.jn.langx.rpc.session.exception;
import com.jn.langx.rpc.session.Session;
/**
 * Exception thrown when attempting to interact with the system under an established session
 * when that session is considered invalid.  The meaning of the term 'invalid' is based on
 * application behavior.  For example, a Session is considered invalid if it has been explicitly
 * stopped (e.g. when a user logs-out or when explicitly
 * {@link Session#invalid() stopped} programmatically.  A Session can also be
 * considered invalid if it has expired.
 *
 * @see StoppedSessionException
 * @see ExpiredSessionException
 * @see UnknownSessionException
 * @since 3.7.0
 */
public class InvalidSessionException extends SessionException {

    /**
     * Creates a new InvalidSessionException.
     */
    public InvalidSessionException() {
        super();
    }

    /**
     * Constructs a new InvalidSessionException.
     *
     * @param message the reason for the exception
     */
    public InvalidSessionException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidSessionException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidSessionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new InvalidSessionException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public InvalidSessionException(String message, Throwable cause) {
        super(message, cause);
    }

}

