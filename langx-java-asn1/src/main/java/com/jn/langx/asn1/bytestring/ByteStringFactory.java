package com.jn.langx.asn1.bytestring;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.asn1.spec.ASN1OctetString;

/**
 * This class provides a mechanism for creating {@link ByteString} values.
 */
public final class ByteStringFactory {
    /**
     * A pre-allocated ASN.1 octet string with no value.
     */
    @NonNull
    private static final ASN1OctetString EMPTY_VALUE = new ASN1OctetString();


    /**
     * Prevent this class from being instantiated.
     */
    private ByteStringFactory() {
        // No implementation required.
    }


    /**
     * Creates a new byte string with no value.
     *
     * @return The created byte string.
     */
    @NonNull()
    public static ByteString create() {
        return EMPTY_VALUE;
    }


    /**
     * Creates a new byte string with the provided value.
     *
     * @param value The value to use for the byte string.
     * @return The created byte string.
     */
    @NonNull()
    public static ByteString create(@Nullable final byte[] value) {
        return new ASN1OctetString(value);
    }


    /**
     * Creates a new byte string with the provided value.
     *
     * @param value  The byte array containing the data to use for the value.
     *               It must not be {@code null}.
     * @param offset The position in the array at which the value begins.  It
     *               must be greater than or equal to zero and less or equal to
     *               the end of the array.
     * @param length The number of bytes contained in the value.  It must be
     *               greater than or equal to zero, and the sum of the offset
     *               and the length must be less than or equal to the end of the
     *               array.
     * @return The created byte string.
     */
    @NonNull()
    public static ByteString create(@NonNull final byte[] value, final int offset,
                                    final int length) {
        return new ASN1OctetString(value, offset, length);
    }


    /**
     * Creates a new byte string with the provided value.
     *
     * @param value The value to use for the byte string.
     * @return The created byte string.
     */
    @NonNull()
    public static ByteString create(@Nullable final String value) {
        return new ASN1OctetString(value);
    }
}