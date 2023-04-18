package com.jn.langx.asn1.spec;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.asn1.bytestring.ByteString;
import com.jn.langx.asn1.bytestring.ByteStringBuffer;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.io.unicode.Utf8s;

import static com.jn.langx.asn1.spec.ASN1Messages.*;


/**
 * This class provides an ASN.1 octet string element, whose value is simply
 * comprised of zero or more bytes.  Octet string elements are frequently used
 * to represent string values as well.
 */
public final class ASN1OctetString extends ASN1Element implements ByteString {
    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = -7857753188341295516L;



    /*
     * NOTE:  This class uses lazy initialization for the value.  The value may
     * be initially specified as either a string or a byte array, and if the value
     * is provided as a string, then the byte array version of that value will be
     * computed on-demand later.  Even though this class is externally immutable,
     * that does not by itself make it completely threadsafe, because weirdness in
     * the Java memory model could allow the assignment to be performed out of
     * order.  By passing the value through a volatile variable any time the value
     * is set other than in the constructor (which will always be safe) we ensure
     * that this reordering cannot happen.  This is only needed for the valueBytes
     * array because it is not required for primitives (like length and offset) or
     * for objects with only final fields (like stringValue).
     *
     * In the majority of cases, passing the value through a volatile variable is
     * much faster than declaring valueBytes itself to be volatile because a
     * volatile variable cannot be held in CPU caches or registers and must only
     * be accessed from memory visible to all threads.  Since the value may be
     * read much more often than it is written, passing it through a volatile
     * variable rather than making it volatile directly can help avoid that
     * penalty when possible.
     */


    // The binary representation of the value for this element.
    @Nullable
    private byte[] valueBytes;

    // A volatile variable used to guard publishing the valueBytes array.  See the
    // note above to explain why this is needed.
    @Nullable
    private volatile byte[] valueBytesGuard;

    // The length of the value in the byte array, if applicable.
    private int length;

    // The offset in the byte array at which the value begins, if applicable.
    private int offset;

    // The string representation of the value for this element.
    @Nullable
    private String stringValue;


    /**
     * Creates a new ASN.1 octet string element with the default BER type and
     * no value.
     */
    public ASN1OctetString() {
        super(ASN1Constants.UNIVERSAL_OCTET_STRING_TYPE);

        valueBytes = Emptys.EMPTY_BYTES;
        stringValue = "";
        offset = 0;
        length = 0;
    }


    /**
     * Creates a new ASN.1 octet string element with the specified type and no
     * value.
     *
     * @param type The BER type to use for this element.
     */
    public ASN1OctetString(final byte type) {
        super(type);

        valueBytes = Emptys.EMPTY_BYTES;
        stringValue = "";
        offset = 0;
        length = 0;
    }


    /**
     * Creates a new ASN.1 octet string element with the default BER type and the
     * provided value.
     *
     * @param value The value to use for this element.
     */
    public ASN1OctetString(@Nullable final byte[] value) {
        super(ASN1Constants.UNIVERSAL_OCTET_STRING_TYPE);

        if (value == null) {
            valueBytes = Emptys.EMPTY_BYTES;
            stringValue = "";
            offset = 0;
            length = 0;
        } else {
            valueBytes = value;
            stringValue = null;
            offset = 0;
            length = value.length;
        }
    }


    /**
     * Creates a new ASN.1 octet string element with the default BER type and the
     * provided value.
     *
     * @param value  The byte array containing the value to use for this
     *               element  It must not be {@code null}.
     * @param offset The offset within the array at which the value begins.  It
     *               must be greater than or equal to zero and less than or
     *               equal to the length of the array.
     * @param length The length in bytes of the value.   It must be greater than
     *               or equal to zero, and it must not extend beyond the end of
     *               the array.
     */
    public ASN1OctetString(@NonNull final byte[] value, final int offset,
                           final int length) {
        super(ASN1Constants.UNIVERSAL_OCTET_STRING_TYPE);

        Preconditions.checkNotNull(value);
        Preconditions.checkTrue((offset >= 0) && (length >= 0) &&
                (offset + length <= value.length));

        valueBytes = value;
        stringValue = null;
        this.offset = offset;
        this.length = length;
    }


    /**
     * Creates a new ASN.1 octet string element with the specified type and the
     * provided value.
     *
     * @param type  The BER type to use for this element.
     * @param value The value to use for this element.
     */
    public ASN1OctetString(final byte type, @Nullable final byte[] value) {
        super(type);

        if (value == null) {
            valueBytes = Emptys.EMPTY_BYTES;
            stringValue = "";
            offset = 0;
            length = 0;
        } else {
            valueBytes = value;
            stringValue = null;
            offset = 0;
            length = value.length;
        }
    }


    /**
     * Creates a new ASN.1 octet string element with the specified type and the
     * provided value.
     *
     * @param type   The BER type to use for this element.
     * @param value  The byte array containing the value to use for this
     *               element.  It must not be {@code null}.
     * @param offset The offset within the array at which the value begins.  It
     *               must be greater than or equal to zero and less than or
     *               equal to the length of the array.
     * @param length The length in bytes of the value.   It must be greater than
     *               or equal to zero, and it must not extend beyond the end of
     *               the array.
     */
    public ASN1OctetString(final byte type, @NonNull final byte[] value,
                           final int offset, final int length) {
        super(type);
        Preconditions.checkTrue((offset >= 0) && (length >= 0) &&
                (offset + length <= value.length));

        valueBytes = value;
        stringValue = null;
        this.offset = offset;
        this.length = length;
    }


    /**
     * Creates a new ASN.1 octet string element with the default BER type and the
     * provided value.
     *
     * @param value The value to use for this element.
     */
    public ASN1OctetString(@Nullable final String value) {
        super(ASN1Constants.UNIVERSAL_OCTET_STRING_TYPE);

        if (value == null) {
            valueBytes = Emptys.EMPTY_BYTES;
            stringValue = "";
            offset = 0;
            length = 0;
        } else {
            valueBytes = null;
            stringValue = value;
            offset = -1;
            length = -1;
        }
    }


    /**
     * Creates a new ASN.1 octet string element with the specified type and the
     * provided value.
     *
     * @param type  The BER type to use for this element.
     * @param value The value to use for this element.
     */
    public ASN1OctetString(final byte type, @Nullable final String value) {
        super(type);

        if (value == null) {
            valueBytes = Emptys.EMPTY_BYTES;
            stringValue = "";
            offset = 0;
            length = 0;
        } else {
            valueBytes = null;
            stringValue = value;
            offset = -1;
            length = -1;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    @NonNull()
    byte[] getValueArray() {
        return getValue();
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    int getValueOffset() {
        return 0;
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    public int getValueLength() {
        return getValue().length;
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    @NonNull()
    public byte[] getValue() {
        if (valueBytes == null) {
            valueBytesGuard = Utf8s.getBytes(stringValue);
            offset = 0;
            length = valueBytesGuard.length;
            valueBytes = valueBytesGuard;
        } else if ((offset != 0) || (length != valueBytes.length)) {
            final byte[] newArray = new byte[length];
            System.arraycopy(valueBytes, offset, newArray, 0, length);
            offset = 0;
            valueBytesGuard = newArray;
            valueBytes = valueBytesGuard;
        }

        return valueBytes;
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    public void encodeTo(@NonNull final ByteStringBuffer buffer) {
        buffer.append(getType());

        if (valueBytes == null) {
            // Assume that the string contains only ASCII characters.  That will be
            // true most of the time and we can optimize for it.  If it's not true,
            // then we'll fix it later.
            final int stringLength = stringValue.length();
            final int lengthStartPos = buffer.length();
            encodeLengthTo(stringLength, buffer);
            final int valueStartPos = buffer.length();
            buffer.append(stringValue);
            final int stringBytesLength = buffer.length() - valueStartPos;
            if (stringBytesLength != stringLength) {
                // This must mean that the string had non-ASCII characters in it, so
                // fix the encoded representation.
                final byte[] newLengthBytes = encodeLength(stringBytesLength);
                if (newLengthBytes.length == (valueStartPos - lengthStartPos)) {
                    // It takes the same number of bytes to encode the new length as
                    // the length we previously expected, so we can just overwrite the
                    // length bytes in the backing array.
                    System.arraycopy(newLengthBytes, 0, buffer.getBackingArray(),
                            lengthStartPos, newLengthBytes.length);
                } else {
                    buffer.setLength(lengthStartPos);
                    buffer.append(newLengthBytes);
                    buffer.append(stringValue);
                }
            }
        } else {
            encodeLengthTo(length, buffer);
            buffer.append(valueBytes, offset, length);
        }
    }


    /**
     * Retrieves the string value for this element.
     *
     * @return The String value for this element.
     */
    @Override()
    @NonNull()
    public String stringValue() {
        if (stringValue == null) {
            if (length == 0) {
                stringValue = "";
            } else {
                stringValue = Utf8s.toString(valueBytes, offset, length);
            }
        }

        return stringValue;
    }


    /**
     * Decodes the contents of the provided byte array as an octet string element.
     *
     * @param elementBytes The byte array to decode as an ASN.1 octet string
     *                     element.
     * @return The decoded ASN.1 octet string element.
     * @throws ASN1Exception If the provided array cannot be decoded as an
     *                       octet string element.
     */
    @NonNull()
    public static ASN1OctetString decodeAsOctetString(
            @NonNull final byte[] elementBytes)
            throws ASN1Exception {
        try {
            int valueStartPos = 2;
            int length = (elementBytes[1] & 0x7F);
            if (length != elementBytes[1]) {
                final int numLengthBytes = length;

                length = 0;
                for (int i = 0; i < numLengthBytes; i++) {
                    length <<= 8;
                    length |= (elementBytes[valueStartPos++] & 0xFF);
                }
            }

            if ((elementBytes.length - valueStartPos) != length) {
                throw new ASN1Exception(ERR_ELEMENT_LENGTH_MISMATCH.get(length,
                        (elementBytes.length - valueStartPos)));
            }

            return new ASN1OctetString(elementBytes[0], elementBytes, valueStartPos,
                    length);
        } catch (final ASN1Exception ae) {
            throw ae;
        } catch (final Exception e) {
            throw new ASN1Exception(ERR_ELEMENT_DECODE_EXCEPTION.get(e), e);
        }
    }


    /**
     * Decodes the provided ASN.1 element as an octet string element.
     *
     * @param element The ASN.1 element to be decoded.
     * @return The decoded ASN.1 octet string element.
     */
    @NonNull()
    public static ASN1OctetString decodeAsOctetString(
            @NonNull final ASN1Element element) {
        return new ASN1OctetString(element.getType(), element.getValue());
    }


    /**
     * Appends the value of this ASN.1 octet string to the provided buffer.
     *
     * @param buffer The buffer to which the value is to be appended.
     */
    @Override()
    public void appendValueTo(@NonNull final ByteStringBuffer buffer) {
        if (valueBytes == null) {
            buffer.append(stringValue);
        } else {
            buffer.append(valueBytes, offset, length);
        }
    }


    /**
     * Converts this byte string to an ASN.1 octet string.
     *
     * @return An ASN.1 octet string with the value of this byte string.
     */
    @Override()
    @NonNull()
    public ASN1OctetString toASN1OctetString() {
        return this;
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    public void toString(@NonNull final StringBuilder buffer) {
        buffer.append(stringValue());
    }
}
