package com.jn.langx.util.net.mime;



/**
 * Exception thrown from {@link MediaType#parseMediaType(String)} in case of
 * encountering an invalid media type specification String.
 *
 */
@SuppressWarnings("serial")
public class InvalidMediaTypeException extends IllegalArgumentException {

    private final String mediaType;


    /**
     * Create a new InvalidMediaTypeException for the given media type.
     * @param mediaType the offending media type
     * @param message a detail message indicating the invalid part
     */
    public InvalidMediaTypeException(String mediaType, String message) {
        super("Invalid media type \"" + mediaType + "\": " + message);
        this.mediaType = mediaType;
    }

    /**
     * Constructor that allows wrapping {@link InvalidMimeTypeException}.
     */
    InvalidMediaTypeException(InvalidMimeTypeException ex) {
        super(ex.getMessage(), ex);
        this.mediaType = ex.getMimeType();
    }


    /**
     * Return the offending media type.
     */
    public String getMediaType() {
        return this.mediaType;
    }

}
