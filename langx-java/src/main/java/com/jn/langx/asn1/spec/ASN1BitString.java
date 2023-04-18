package com.jn.langx.asn1.spec;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import static com.jn.langx.asn1.spec.ASN1Messages.*;

/**
 * This class provides an ASN.1 bit string element, whose value represents a
 * series of zero or more bits, where each bit is either one or zero.
 */
public final class ASN1BitString extends ASN1Element {
    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = -5962171503831966571L;


    // An array of the bits in this bit string, where true is 1 and false is 0.
    @NonNull
    private final boolean[] bits;

    // The bytes represented by the bits that comprise this bit string.
    @Nullable
    private final byte[] bytes;


    /**
     * Creates a new ASN.1 bit string element with the default BER type and the
     * provided set of bits.
     *
     * @param bits The bits to include in the bit string.  Each {@code boolean}
     *             value of {@code true} represents a bit of one, and each
     *             {@code boolean} value of {@code false} represents a bit of
     *             zero.  It must not be {@code null} but may be empty.
     */
    public ASN1BitString(@NonNull final boolean... bits) {
        this(ASN1Constants.UNIVERSAL_BIT_STRING_TYPE, bits);
    }


    /**
     * Creates a new ASN.1 bit string element with the specified BER type and the
     * provided set of bits.
     *
     * @param type The BER type to use for this element.
     * @param bits The bits to include in the bit string.  Each {@code boolean}
     *             value of {@code true} represents a bit of one, and each
     *             {@code boolean} value of {@code false} represents a bit of
     *             zero.  It must not be {@code null} but may be empty.
     */
    public ASN1BitString(final byte type, @NonNull final boolean... bits) {
        this(type, bits, null, encodeValue(bits));
    }


    /**
     * Creates a new ASN.1 bit string element with the provided information.
     *
     * @param type         The BER type to use for this element.
     * @param bits         The bits to include in the bit string.  Each
     *                     {@code boolean} value of {@code true} represents a
     *                     bit of one, and each {@code boolean} value of
     *                     {@code false} represents a bit of zero.  It must not
     *                     be {@code null} but may be empty.
     * @param bytes        The bytes represented by the bits that comprise this
     *                     bit string.  This may be {@code null} if it has not
     *                     yet been determined, or if the number of bits is not
     *                     an even multiple of eight.
     * @param encodedValue The encoded value for this element.
     */
    private ASN1BitString(final byte type, @NonNull final boolean[] bits,
                          @Nullable final byte[] bytes,
                          @NonNull final byte[] encodedValue) {
        super(type, encodedValue);

        this.bits = bits;

        if (bytes == null) {
            if ((bits.length % 8) == 0) {
                this.bytes = new byte[bits.length / 8];

                byte currentByte = 0x00;
                int byteIndex = 0;
                for (int i = 0; i < bits.length; i++) {
                    currentByte <<= 1;
                    if (bits[i]) {
                        currentByte |= 0x01;
                    }

                    if (((i + 1) % 8) == 0) {
                        this.bytes[byteIndex++] = currentByte;
                        currentByte = 0x00;
                    }
                }
            } else {
                this.bytes = null;
            }
        } else {
            this.bytes = bytes;
        }
    }


    /**
     * Creates a new ASN.1 bit string with the default BER type and a value
     * created from the provided string representation.
     *
     * @param stringRepresentation A string representation of the bit string to
     *                             create.  It must not be {@code null}, but may
     *                             be empty.  It must be comprised only of the
     *                             characters '1' and '0'.
     * @throws ASN1Exception If the provided string does not represent a valid
     *                       bit string value.
     */
    public ASN1BitString(@NonNull final String stringRepresentation)
            throws ASN1Exception {
        this(ASN1Constants.UNIVERSAL_BIT_STRING_TYPE, stringRepresentation);
    }


    /**
     * Creates a new ASN.1 bit string with the default BER type and a value
     * created from the provided string representation.
     *
     * @param type                 The BER type to use for this element.
     * @param stringRepresentation A string representation of the bit string to
     *                             create.  It must not be {@code null}, but may
     *                             be empty.  It must be comprised only of the
     *                             characters '1' and '0'.
     * @throws ASN1Exception If the provided string does not represent a valid
     *                       bit string value.
     */
    public ASN1BitString(final byte type,
                         @NonNull final String stringRepresentation)
            throws ASN1Exception {
        this(type, getBits(stringRepresentation));
    }


    /**
     * Decodes the provided string representation of a bit string into an array of
     * bits.
     *
     * @param s A string representation of the bit string to create.  It must
     *          not be {@code null}, but may be empty.  It must be comprised
     *          only of the characters '1' and '0'.
     * @return An array of {@code boolean} values that correspond to the bits in
     * this bit string.
     * @throws ASN1Exception If the provided string does not represent a valid
     *                       bit string value.
     */
    @NonNull()
    private static boolean[] getBits(@NonNull final String s)
            throws ASN1Exception {
        final char[] chars = s.toCharArray();
        final boolean[] bits = new boolean[chars.length];
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '0') {
                bits[i] = false;
            } else if (chars[i] == '1') {
                bits[i] = true;
            } else {
                throw new ASN1Exception(
                        ERR_BIT_STRING_DECODE_STRING_INVALID_CHAR.get());
            }
        }

        return bits;
    }


    /**
     * Generates an encoded value for a bit string with the specified set of
     * bits.
     *
     * @param bits The bits to include in the bit string.  Each {@code boolean}
     *             value of {@code true} represents a bit of one, and each
     *             {@code boolean} value of {@code false} represents a bit of
     *             zero.  It must not be {@code null} but may be empty.
     * @return The encoded value.
     */
    @NonNull()
    private static byte[] encodeValue(@NonNull final boolean... bits) {
        // A bit string value always has at least one byte, and that byte specifies
        // the number of padding bits needed in the last byte.  The remaining bytes
        // are used to hold the bits, with eight bits per byte.  If the number of
        // bits provided is not a multiple of eight, then it will be assumed that
        // there are enough extra bits of zero to make an even last byte.
        final byte[] encodedValue;
        final int paddingBitsNeeded;
        final int numBitsMod8 = (bits.length % 8);
        if (numBitsMod8 == 0) {
            paddingBitsNeeded = 0;
            encodedValue = new byte[(bits.length / 8) + 1];
        } else {
            paddingBitsNeeded = 8 - numBitsMod8;
            encodedValue = new byte[(bits.length / 8) + 2];
        }

        encodedValue[0] = (byte) paddingBitsNeeded;

        byte currentByte = 0x00;
        int bitIndex = 0;
        int encodedValueIndex = 1;
        for (final boolean bit : bits) {
            currentByte <<= 1;
            if (bit) {
                currentByte |= 0x01;
            }

            bitIndex++;
            if ((bitIndex % 8) == 0) {
                encodedValue[encodedValueIndex] = currentByte;
                currentByte = 0x00;
                encodedValueIndex++;
            }
        }

        if (paddingBitsNeeded > 0) {
            currentByte <<= paddingBitsNeeded;
            encodedValue[encodedValueIndex] = currentByte;
        }

        return encodedValue;
    }


    /**
     * Retrieves an array of {@code boolean} values that correspond to the bits in
     * this bit string.  Each {@code boolean} value of {@code true} represents a
     * bit of one, and each {@code boolean} value of {@code false} represents a
     * bit of zero.
     *
     * @return An array of {@code boolean} values that correspond to the bits in
     * this bit string.
     */
    @NonNull()
    public boolean[] getBits() {
        return bits;
    }


    /**
     * Retrieves the bytes represented by the bits that comprise this bit string,
     * if the number of bits is a multiple of eight.
     *
     * @return The bytes represented by the bits that comprise this bit string.
     * @throws ASN1Exception If the number of bits in this bit string is not a
     *                       multiple of eight.
     */
    @NonNull()
    public byte[] getBytes()
            throws ASN1Exception {
        if (bytes == null) {
            throw new ASN1Exception(
                    ERR_BIT_STRING_GET_BYTES_NOT_MULTIPLE_OF_EIGHT_BITS.get(
                            bits.length));
        } else {
            return bytes;
        }
    }


    /**
     * Retrieves an array of booleans that represent the bits in the provided
     * array of bytes.
     *
     * @param bytes The bytes for which to retrieve the corresponding bits.  It
     *              must not be {@code null}.
     * @return An array of the bits that make up the provided bytes.
     */
    @NonNull()
    public static boolean[] getBitsForBytes(@NonNull final byte... bytes) {
        final boolean[] bits = new boolean[bytes.length * 8];
        for (int i = 0; i < bytes.length; i++) {
            final byte b = bytes[i];
            bits[i * 8] = ((b & 0x80) == 0x80);
            bits[(i * 8) + 1] = ((b & 0x40) == 0x40);
            bits[(i * 8) + 2] = ((b & 0x20) == 0x20);
            bits[(i * 8) + 3] = ((b & 0x10) == 0x10);
            bits[(i * 8) + 4] = ((b & 0x08) == 0x08);
            bits[(i * 8) + 5] = ((b & 0x04) == 0x04);
            bits[(i * 8) + 6] = ((b & 0x02) == 0x02);
            bits[(i * 8) + 7] = ((b & 0x01) == 0x01);
        }

        return bits;
    }


    /**
     * Decodes the contents of the provided byte array as a bit string element.
     *
     * @param elementBytes The byte array to decode as an ASN.1 bit string
     *                     element.
     * @return The decoded ASN.1 bit string element.
     * @throws ASN1Exception If the provided array cannot be decoded as a bit
     *                       string element.
     */
    @NonNull()
    public static ASN1BitString decodeAsBitString(
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
            final boolean[] bits = decodeValue(elementValue);

            final byte[] bytes;
            if ((bits.length % 8) == 0) {
                bytes = new byte[elementValue.length - 1];
                System.arraycopy(elementValue, 1, bytes, 0, bytes.length);
            } else {
                bytes = null;
            }

            return new ASN1BitString(elementBytes[0], bits, bytes, elementValue);
        } catch (final ASN1Exception ae) {
            throw ae;
        } catch (final Exception e) {
            throw new ASN1Exception(ERR_ELEMENT_DECODE_EXCEPTION.get(e), e);
        }
    }


    /**
     * Decodes the provided ASN.1 element as a bit string element.
     *
     * @param element The ASN.1 element to be decoded.
     * @return The decoded ASN.1 bit string element.
     * @throws ASN1Exception If the provided element cannot be decoded as a bit
     *                       string element.
     */
    @NonNull()
    public static ASN1BitString decodeAsBitString(
            @NonNull final ASN1Element element)
            throws ASN1Exception {
        final byte[] elementValue = element.getValue();
        final boolean[] bits = decodeValue(elementValue);

        final byte[] bytes;
        if ((bits.length % 8) == 0) {
            bytes = new byte[elementValue.length - 1];
            System.arraycopy(elementValue, 1, bytes, 0, bytes.length);
        } else {
            bytes = null;
        }

        return new ASN1BitString(element.getType(), bits, bytes,
                element.getValue());
    }


    /**
     * Decodes the provided value into a set of bits.
     *
     * @param elementValue The bytes that comprise the encoded value for a
     *                     bit string element.
     * @return An array of {@code boolean} values that correspond to the bits in
     * this bit string.
     * @throws ASN1Exception If the provided value cannot be decoded as a valid
     *                       bit string.
     */
    @NonNull()
    private static boolean[] decodeValue(@NonNull final byte[] elementValue)
            throws ASN1Exception {
        if (elementValue.length == 0) {
            throw new ASN1Exception(ERR_BIT_STRING_DECODE_EMPTY_VALUE.get());
        }

        final int paddingBitsNeeded = (elementValue[0] & 0xFF);
        if (paddingBitsNeeded > 7) {
            throw new ASN1Exception(ERR_BIT_STRING_DECODE_INVALID_PADDING_BIT_COUNT.get(paddingBitsNeeded));
        }

        if ((paddingBitsNeeded > 0) && (elementValue.length == 1)) {
            throw new ASN1Exception(ERR_BIT_STRING_DECODE_NONZERO_PADDING_BIT_COUNT_WITH_NO_MORE_BYTES.get());
        }

        int bitsIndex = 0;
        final int numBits = ((elementValue.length - 1) * 8) - paddingBitsNeeded;
        final boolean[] bits = new boolean[numBits];
        for (int i = 1; i < elementValue.length; i++) {
            byte b = elementValue[i];
            if ((i == (elementValue.length - 1)) && (paddingBitsNeeded > 0)) {
                for (int j = 0; j < (8 - paddingBitsNeeded); j++) {
                    bits[bitsIndex++] = ((b & 0x80) == 0x80);
                    b <<= 1;
                }
            } else {
                bits[bitsIndex++] = ((b & 0x80) == 0x80);
                bits[bitsIndex++] = ((b & 0x40) == 0x40);
                bits[bitsIndex++] = ((b & 0x20) == 0x20);
                bits[bitsIndex++] = ((b & 0x10) == 0x10);
                bits[bitsIndex++] = ((b & 0x08) == 0x08);
                bits[bitsIndex++] = ((b & 0x04) == 0x04);
                bits[bitsIndex++] = ((b & 0x02) == 0x02);
                bits[bitsIndex++] = ((b & 0x01) == 0x01);
            }
        }

        return bits;
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    public void toString(@NonNull final StringBuilder buffer) {
        buffer.ensureCapacity(buffer.length() + bits.length);
        for (final boolean bit : bits) {
            if (bit) {
                buffer.append('1');
            } else {
                buffer.append('0');
            }
        }
    }
}
