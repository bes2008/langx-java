package com.jn.langx.text.grok;


/**
 * Signals that an {@code Grok} exception of some sort has occurred.
 * This class is the general class of
 * exceptions produced by failed or interrupted Grok operations.
 *
 * @since 4.7.2
 */
public class GrokException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new GrokException.
     */
    public GrokException() {
        super();
    }

    /**
     * Constructs a new GrokException.
     *
     * @param message the reason for the exception
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public GrokException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new GrokException.
     *
     * @param message the reason for the exception
     */
    public GrokException(String message) {
        super(message);
    }

    /**
     * Constructs a new GrokException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public GrokException(Throwable cause) {
        super(cause);
    }

}
