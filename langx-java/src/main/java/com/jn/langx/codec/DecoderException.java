package com.jn.langx.codec;


/**
 * Thrown when there is a failure condition during the decoding process. This exception is thrown when a {@link Decoder}
 * encounters a decoding specific exception such as invalid data, or characters outside of the expected range.
 */
public class DecoderException extends RuntimeException {

    /**
     * Declares the Serial Version Uid.
     *
     * @see <a href="http://c2.com/cgi/wiki?AlwaysDeclareSerialVersionUid">Always Declare Serial Version Uid</a>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with <code>null</code> as its detail message. The cause is not initialized, and may
     * subsequently be initialized by a call to {@link #initCause}.
     */
    public DecoderException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     *
     * @param message The detail message which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public DecoderException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is not automatically incorporated into this
     * exception's detail message.
     *
     * @param message The detail message which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause   The cause which is saved for later retrieval by the {@link #getCause()} method. A <code>null</code>
     *                value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public DecoderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of <code>(cause==null ?
     * null : cause.toString())</code> (which typically contains the class and detail message of <code>cause</code>).
     * This constructor is useful for exceptions that are little more than wrappers for other throwables.
     *
     * @param cause The cause which is saved for later retrieval by the {@link #getCause()} method. A <code>null</code>
     *              value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public DecoderException(final Throwable cause) {
        super(cause);
    }
}
