package com.jn.langx.asn1.spec;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotMutable;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.io.unicode.Utf8s;

import static com.jn.langx.asn1.spec.ASN1Messages.*;


/**
 * This class provides an ASN.1 UTF-8 string element that can hold any string
 * value that can be represented in the UTF-8 encoding.
 */
@NotMutable()
public final class ASN1UTF8String extends ASN1Element {
    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = -2147537370903003997L;


    // The string value for this element.
    @NonNull
    private final String stringValue;


    /**
     * Creates a new ASN.1 UTF-8 string element with the default BER type and the
     * provided value.
     *
     * @param stringValue The string value to use for this element.  It may be
     *                    {@code null} or empty if the value should be empty.
     */
    public ASN1UTF8String(@Nullable final String stringValue) {
        this(ASN1Constants.UNIVERSAL_UTF_8_STRING_TYPE, stringValue);
    }


    /**
     * Creates a new ASN.1 UTF-8 string element with the specified BER type and
     * the provided value.
     *
     * @param type        The BER type for this element.
     * @param stringValue The string value to use for this element.  It may be
     *                    {@code null} or empty if the value should be empty.
     */
    public ASN1UTF8String(final byte type, @Nullable final String stringValue) {
        this(type, stringValue, Utf8s.getBytes(stringValue));
    }


    /**
     * Creates a new ASN.1 UTF-8 string element with the specified BER type and
     * the provided value.
     *
     * @param type         The BER type for this element.
     * @param stringValue  The string value to use for this element.  It may be
     *                     {@code null} or empty if the value should be empty.
     * @param encodedValue The encoded representation of the value.
     */
    private ASN1UTF8String(final byte type, @Nullable final String stringValue,
                           @NonNull final byte[] encodedValue) {
        super(type, encodedValue);

        if (stringValue == null) {
            this.stringValue = "";
        } else {
            this.stringValue = stringValue;
        }
    }


    /**
     * Retrieves the string value for this element.
     *
     * @return The string value for this element.
     */
    @NonNull()
    public String stringValue() {
        return stringValue;
    }


    /**
     * Decodes the contents of the provided byte array as a UTF-8 string element.
     *
     * @param elementBytes The byte array to decode as an ASN.1 UTF-8 string
     *                     element.
     * @return The decoded ASN.1 UTF-8 string element.
     * @throws ASN1Exception If the provided array cannot be decoded as a UTF-8
     *                       string element.
     */
    @NonNull()
    public static ASN1UTF8String decodeAsUTF8String(
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

            final byte[] elementValue = new byte[length];
            System.arraycopy(elementBytes, valueStartPos, elementValue, 0, length);

            if (!Utf8s.isValidUTF8(elementValue)) {
                throw new ASN1Exception(ERR_UTF_8_STRING_DECODE_VALUE_NOT_UTF_8.get());
            }

            return new ASN1UTF8String(elementBytes[0],
                    Utf8s.toString(elementValue), elementValue);
        } catch (final ASN1Exception ae) {
            //Debug.debugException(ae);
            throw ae;
        } catch (final Exception e) {
            //Debug.debugException(e);
            throw new ASN1Exception(ERR_ELEMENT_DECODE_EXCEPTION.get(e), e);
        }
    }


    /**
     * Decodes the provided ASN.1 element as a UTF-8 string element.
     *
     * @param element The ASN.1 element to be decoded.
     * @return The decoded ASN.1 UTF-8 string element.
     * @throws ASN1Exception If the provided element cannot be decoded as a
     *                       UTF-8 string element.
     */
    @NonNull()
    public static ASN1UTF8String decodeAsUTF8String(
            @NonNull final ASN1Element element)
            throws ASN1Exception {
        final byte[] elementValue = element.getValue();
        if (!Utf8s.isValidUTF8(elementValue)) {
            throw new ASN1Exception(ERR_UTF_8_STRING_DECODE_VALUE_NOT_UTF_8.get());
        }

        return new ASN1UTF8String(element.getType(),
                Utf8s.toString(elementValue), elementValue);
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    public void toString(@NonNull final StringBuilder buffer) {
        buffer.append(stringValue);
    }
}
