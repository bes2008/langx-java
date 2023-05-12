package com.jn.langx.asn1.spec;

import com.jn.langx.annotation.NonNull;

import static com.jn.langx.asn1.spec.ASN1Messages.ERR_STREAM_READER_SET_READ_PAST_END;


/**
 * This class provides a data structure which is used in the course of reading
 * an ASN.1 set from an ASN.1 stream reader.  It provides access to the BER type
 * and the total number of bytes in the encoded representations of all of the
 * embedded values, and provides a method to determine when the end of the set
 * has been reached.
 */
public final class ASN1StreamReaderSet {
    // The ASN.1 stream reader associated with this object.
    @NonNull
    private final ASN1StreamReader reader;

    // The BER type for this ASN.1 set.
    private final byte type;

    // The number of bytes contained in the encoded representations of all of the
    // embedded values.
    private final int length;

    // The value for the total number of bytes read from the associated reader at
    // which the end of the set should be encountered.
    private final long endBytesRead;


    /**
     * Creates a new instance of this class with the provided information.
     *
     * @param reader The ASN.1 stream reader with which this object will be
     *               associated.
     * @param type   The BER type for this ASN.1 set.
     * @param length The number of bytes contained in the encoded
     *               representations of all the embedded values.
     */
    ASN1StreamReaderSet(@NonNull final ASN1StreamReader reader, final byte type,
                        final int length) {
        this.reader = reader;
        this.type = type;
        this.length = length;

        endBytesRead = reader.getTotalBytesRead() + length;
    }


    /**
     * Retrieves the BER type for this ASN.1 set.
     *
     * @return The BER type for this ASN.1 set.
     */
    public byte getType() {
        return type;
    }


    /**
     * Retrieves the number of bytes contained in the encoded representations of
     * all the embedded values.
     *
     * @return The number of bytes contained in the encoded representations of
     * all the embedded values.
     */
    public int getLength() {
        return length;
    }


    /**
     * Indicates whether there are more elements in this set to be read from the
     * associated ASN.1 stream reader.
     *
     * @return {@code true} if there are more elements in this set to be read
     * from the associated ASN.1 stream reader or false if the end of the
     * set has been reached.
     * @throws ASN1Exception If the associated ASN.1 stream reader has already
     *                       read beyond the end of the set.
     */
    public boolean hasMoreElements()
            throws ASN1Exception {
        final long currentBytesRead = reader.getTotalBytesRead();
        if (currentBytesRead == endBytesRead) {
            return false;
        } else if (currentBytesRead < endBytesRead) {
            return true;
        }

        throw new ASN1Exception(ERR_STREAM_READER_SET_READ_PAST_END.get(
                length, endBytesRead, currentBytesRead));
    }
}
