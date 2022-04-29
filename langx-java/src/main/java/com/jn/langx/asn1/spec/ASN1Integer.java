package com.jn.langx.asn1.spec;


import com.jn.langx.annotation.NonNull;

import static com.jn.langx.asn1.spec.ASN1Messages.*;


/**
 * This class provides an ASN.1 integer element that is backed by a Java
 * {@code int}, which is a signed 32-bit value and can represent any integer
 * between -2147483648 and 2147483647.  If you need support for integer values
 * in the signed 64-bit range, see the {@link ASN1Long} class as an alternative.
 * If you need support for integer values of arbitrary size, see
 * {@link ASN1BigInteger}.
 */
public final class ASN1Integer extends ASN1Element {
    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = -733929804601994372L;


    // The int value for this element.
    private final int intValue;


    /**
     * Creates a new ASN.1 integer element with the default BER type and the
     * provided int value.
     *
     * @param intValue The int value to use for this element.
     */
    public ASN1Integer(final int intValue) {
        super(ASN1Constants.UNIVERSAL_INTEGER_TYPE, encodeIntValue(intValue));

        this.intValue = intValue;
    }


    /**
     * Creates a new ASN.1 integer element with the specified BER type and the
     * provided int value.
     *
     * @param type     The BER type to use for this element.
     * @param intValue The int value to use for this element.
     */
    public ASN1Integer(final byte type, final int intValue) {
        super(type, encodeIntValue(intValue));

        this.intValue = intValue;
    }


    /**
     * Creates a new ASN.1 integer element with the specified BER type and the
     * provided int and pre-encoded values.
     *
     * @param type     The BER type to use for this element.
     * @param intValue The int value to use for this element.
     * @param value    The pre-encoded value to use for this element.
     */
    private ASN1Integer(final byte type, final int intValue,
                        @NonNull final byte[] value) {
        super(type, value);

        this.intValue = intValue;
    }


    /**
     * Encodes the provided int value to a byte array suitable for use as the
     * value of an integer element.
     *
     * @param intValue The int value to be encoded.
     * @return A byte array containing the encoded value.
     */
    @NonNull()
    static byte[] encodeIntValue(final int intValue) {
        if (intValue < 0) {
            if ((intValue & 0xFFFFFF80) == 0xFFFFFF80) {
                return new byte[]
                        {
                                (byte) (intValue & 0xFF)
                        };
            } else if ((intValue & 0xFFFF8000) == 0xFFFF8000) {
                return new byte[]
                        {
                                (byte) ((intValue >> 8) & 0xFF),
                                (byte) (intValue & 0xFF)
                        };
            } else if ((intValue & 0xFF800000) == 0xFF800000) {
                return new byte[]
                        {
                                (byte) ((intValue >> 16) & 0xFF),
                                (byte) ((intValue >> 8) & 0xFF),
                                (byte) (intValue & 0xFF)
                        };
            } else {
                return new byte[]
                        {
                                (byte) ((intValue >> 24) & 0xFF),
                                (byte) ((intValue >> 16) & 0xFF),
                                (byte) ((intValue >> 8) & 0xFF),
                                (byte) (intValue & 0xFF)
                        };
            }
        } else {
            if ((intValue & 0x0000007F) == intValue) {
                return new byte[]
                        {
                                (byte) (intValue & 0x7F)
                        };
            } else if ((intValue & 0x00007FFF) == intValue) {
                return new byte[]
                        {
                                (byte) ((intValue >> 8) & 0x7F),
                                (byte) (intValue & 0xFF)
                        };
            } else if ((intValue & 0x007FFFFF) == intValue) {
                return new byte[]
                        {
                                (byte) ((intValue >> 16) & 0x7F),
                                (byte) ((intValue >> 8) & 0xFF),
                                (byte) (intValue & 0xFF)
                        };
            } else {
                return new byte[]
                        {
                                (byte) ((intValue >> 24) & 0x7F),
                                (byte) ((intValue >> 16) & 0xFF),
                                (byte) ((intValue >> 8) & 0xFF),
                                (byte) (intValue & 0xFF)
                        };
            }
        }
    }


    /**
     * Retrieves the int value for this element.
     *
     * @return The int value for this element.
     */
    public int intValue() {
        return intValue;
    }


    /**
     * Decodes the contents of the provided byte array as an integer element.
     *
     * @param elementBytes The byte array to decode as an ASN.1 integer element.
     * @return The decoded ASN.1 integer element.
     * @throws ASN1Exception If the provided array cannot be decoded as an
     *                       integer element.
     */
    @NonNull()
    public static ASN1Integer decodeAsInteger(@NonNull final byte[] elementBytes)
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

            final byte[] value = new byte[length];
            System.arraycopy(elementBytes, valueStartPos, value, 0, length);

            int intValue;
            switch (value.length) {
                case 1:
                    intValue = (value[0] & 0xFF);
                    if ((value[0] & 0x80) != 0x00) {
                        intValue |= 0xFFFFFF00;
                    }
                    break;

                case 2:
                    intValue = ((value[0] & 0xFF) << 8) | (value[1] & 0xFF);
                    if ((value[0] & 0x80) != 0x00) {
                        intValue |= 0xFFFF0000;
                    }
                    break;

                case 3:
                    intValue = ((value[0] & 0xFF) << 16) | ((value[1] & 0xFF) << 8) |
                            (value[2] & 0xFF);
                    if ((value[0] & 0x80) != 0x00) {
                        intValue |= 0xFF000000;
                    }
                    break;

                case 4:
                    intValue = ((value[0] & 0xFF) << 24) | ((value[1] & 0xFF) << 16) |
                            ((value[2] & 0xFF) << 8) | (value[3] & 0xFF);
                    break;

                default:
                    throw new ASN1Exception(ERR_ENUMERATED_INVALID_LENGTH.get(
                            value.length));
            }

            return new ASN1Integer(elementBytes[0], intValue, value);
        } catch (final ASN1Exception ae) {
            throw ae;
        } catch (final Exception e) {
            throw new ASN1Exception(ERR_ELEMENT_DECODE_EXCEPTION.get(e), e);
        }
    }


    /**
     * Decodes the provided ASN.1 element as an integer element.
     *
     * @param element The ASN.1 element to be decoded.
     * @return The decoded ASN.1 integer element.
     * @throws ASN1Exception If the provided element cannot be decoded as an
     *                       integer element.
     */
    @NonNull()
    public static ASN1Integer decodeAsInteger(@NonNull final ASN1Element element)
            throws ASN1Exception {
        int intValue;
        final byte[] value = element.getValue();
        switch (value.length) {
            case 1:
                intValue = (value[0] & 0xFF);
                if ((value[0] & 0x80) != 0x00) {
                    intValue |= 0xFFFFFF00;
                }
                break;

            case 2:
                intValue = ((value[0] & 0xFF) << 8) | (value[1] & 0xFF);
                if ((value[0] & 0x80) != 0x00) {
                    intValue |= 0xFFFF0000;
                }
                break;

            case 3:
                intValue = ((value[0] & 0xFF) << 16) | ((value[1] & 0xFF) << 8) |
                        (value[2] & 0xFF);
                if ((value[0] & 0x80) != 0x00) {
                    intValue |= 0xFF000000;
                }
                break;

            case 4:
                intValue = ((value[0] & 0xFF) << 24) | ((value[1] & 0xFF) << 16) |
                        ((value[2] & 0xFF) << 8) | (value[3] & 0xFF);
                break;

            default:
                throw new ASN1Exception(ERR_INTEGER_INVALID_LENGTH.get(value.length));
        }

        return new ASN1Integer(element.getType(), intValue, value);
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    public void toString(@NonNull final StringBuilder buffer) {
        buffer.append(intValue);
    }
}
