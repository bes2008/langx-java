package com.jn.langx.asn1.spec;

import com.jn.langx.annotation.NonNull;

import static com.jn.langx.asn1.spec.ASN1Messages.*;

/**
 * This class provides an ASN.1 Boolean element, whose value is a single byte
 * and represents either "TRUE" or "FALSE".  A value whose only byte is 0x00 is
 * considered "false", while any other single-byte value is considered "true".
 */
public final class ASN1Boolean extends ASN1Element {
    /**
     * A pre-allocated ASN.1 Boolean element with the universal Boolean BER type
     * and a value of "FALSE".
     */
    @NonNull
    public static final ASN1Boolean UNIVERSAL_BOOLEAN_FALSE_ELEMENT =
            new ASN1Boolean(false);


    /**
     * A pre-allocated ASN.1 Boolean element with the universal Boolean BER type
     * and a value of "TRUE".
     */
    @NonNull
    public static final ASN1Boolean UNIVERSAL_BOOLEAN_TRUE_ELEMENT =
            new ASN1Boolean(true);


    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = 7131700816847855524L;


    // The boolean value for this element.
    private final boolean booleanValue;


    /**
     * Creates a new ASN.1 Boolean element with the default BER type and the
     * provided boolean value.
     *
     * @param booleanValue The boolean value to use for this element.
     */
    public ASN1Boolean(final boolean booleanValue) {
        super(ASN1Constants.UNIVERSAL_BOOLEAN_TYPE,
                (booleanValue
                        ? ASN1Constants.BOOLEAN_VALUE_TRUE
                        : ASN1Constants.BOOLEAN_VALUE_FALSE));

        this.booleanValue = booleanValue;
    }


    /**
     * Creates a new ASN.1 Boolean element with the specified BER type and the
     * provided boolean value.
     *
     * @param type         The BER type to use for this element.
     * @param booleanValue The boolean value to use for this element.
     */
    public ASN1Boolean(final byte type, final boolean booleanValue) {
        super(type, (booleanValue
                ? ASN1Constants.BOOLEAN_VALUE_TRUE
                : ASN1Constants.BOOLEAN_VALUE_FALSE));

        this.booleanValue = booleanValue;
    }


    /**
     * Creates a new ASN.1 Boolean element with the provided information.
     *
     * @param type         The BER type to use for this element.
     * @param booleanValue The boolean value to use for this element.
     * @param value        The pre-encoded value to use for this element.
     */
    private ASN1Boolean(final byte type, final boolean booleanValue,
                        @NonNull final byte[] value) {
        super(type, value);

        this.booleanValue = booleanValue;
    }


    /**
     * Retrieves the boolean value for this element.
     *
     * @return {@code true} if this element has a value of "TRUE", or
     * {@code false} if it has a value of "FALSE".
     */
    public boolean booleanValue() {
        return booleanValue;
    }


    /**
     * Decodes the contents of the provided byte array as a Boolean element.
     *
     * @param elementBytes The byte array to decode as an ASN.1 Boolean element.
     * @return The decoded ASN.1 Boolean element.
     * @throws ASN1Exception If the provided array cannot be decoded as a
     *                       Boolean element.
     */
    @NonNull()
    public static ASN1Boolean decodeAsBoolean(@NonNull final byte[] elementBytes)
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

            if (length != 1) {
                throw new ASN1Exception(ERR_BOOLEAN_INVALID_LENGTH.get());
            }

            final byte[] value = {elementBytes[valueStartPos]};
            final boolean booleanValue = (value[0] != 0x00);
            return new ASN1Boolean(elementBytes[0], booleanValue, value);
        } catch (final ASN1Exception ae) {
            throw ae;
        } catch (final Exception e) {
            throw new ASN1Exception(ERR_ELEMENT_DECODE_EXCEPTION.get(e), e);
        }
    }


    /**
     * Decodes the provided ASN.1 element as a Boolean element.
     *
     * @param element The ASN.1 element to be decoded.
     * @return The decoded ASN.1 Boolean element.
     * @throws ASN1Exception If the provided element cannot be decoded as a
     *                       Boolean element.
     */
    @NonNull()
    public static ASN1Boolean decodeAsBoolean(@NonNull final ASN1Element element)
            throws ASN1Exception {
        final byte[] value = element.getValue();
        if (value.length != 1) {
            throw new ASN1Exception(ERR_BOOLEAN_INVALID_LENGTH.get());
        }

        if (value[0] == 0x00) {
            return new ASN1Boolean(element.getType(), false, value);
        } else {
            return new ASN1Boolean(element.getType(), true, value);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    public void toString(@NonNull final StringBuilder buffer) {
        buffer.append(booleanValue);
    }
}
