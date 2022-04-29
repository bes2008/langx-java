package com.jn.langx.asn1.spec;

import com.jn.langx.annotation.NonNull;

import java.io.Serializable;

/**
 * This class provides a data structure which is used in the course of writing
 * an ASN.1 sequence to an ASN.1 buffer.  It keeps track of the position at
 * which the sequence value begins so that the appropriate length may be
 * inserted after all embedded elements have been added.  The {@link #end}
 * method must be called after all elements have been added to ensure that the
 * length is properly computed and inserted into the associated buffer.
 */
public final class ASN1BufferSequence implements Serializable {
    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = 7219098399193345629L;


    // The ASN.1 buffer with which the sequence is associated.
    @NonNull
    private final ASN1Buffer buffer;

    // The position in the ASN.1 buffer at which the first sequence value begins.
    private final int valueStartPos;


    /**
     * Creates a new instance of this class for the provided ASN.1 buffer.
     *
     * @param buffer The ASN.1 buffer with which this object will be associated.
     */
    ASN1BufferSequence(@NonNull final ASN1Buffer buffer) {
        this.buffer = buffer;

        valueStartPos = buffer.length();
    }


    /**
     * Updates the associated ASN.1 buffer to indicate that all sequence elements
     * have been added and that the appropriate length should be inserted.
     */
    public void end() {
        buffer.endSequenceOrSet(valueStartPos);
    }
}
