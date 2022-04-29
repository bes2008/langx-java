package com.jn.langx.asn1.spec;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.codec.hex.Hexs;

import static com.jn.langx.asn1.spec.ASN1Messages.*;

/**
 * This class provides an ASN.1 null element, which does not hold a value.  Null
 * elements are generally used as placeholders that can be substituted for other
 * types of elements.
 */
public final class ASN1Null
        extends ASN1Element {
    /**
     * A pre-allocated ASN.1 null element with the universal null BER type.
     */
    @NonNull
    public static final ASN1Null UNIVERSAL_NULL_ELEMENT = new ASN1Null();


    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = -3264450066845549348L;


    /**
     * Creates a new ASN.1 null element with the default BER type.
     */
    public ASN1Null() {
        super(ASN1Constants.UNIVERSAL_NULL_TYPE);
    }


    /**
     * Creates a new ASN.1 null element with the specified BER type.
     *
     * @param type The BER type to use for this ASN.1 null element.
     */
    public ASN1Null(final byte type) {
        super(type);
    }


    /**
     * Decodes the contents of the provided byte array as a null element.
     *
     * @param elementBytes The byte array to decode as an ASN.1 null element.
     * @return The decoded ASN.1 null element.
     * @throws ASN1Exception If the provided array cannot be decoded as a null
     *                       element.
     */
    @NonNull()
    public static ASN1Null decodeAsNull(@NonNull final byte[] elementBytes)
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

            if (length != 0) {
                throw new ASN1Exception(ERR_NULL_HAS_VALUE.get());
            }

            return new ASN1Null(elementBytes[0]);
        } catch (final ASN1Exception ae) {
            //Debug.debugException(ae);
            throw ae;
        } catch (final Exception e) {
            //Debug.debugException(e);
            throw new ASN1Exception(ERR_ELEMENT_DECODE_EXCEPTION.get(e), e);
        }
    }


    /**
     * Decodes the provided ASN.1 element as a null element.
     *
     * @param element The ASN.1 element to be decoded.
     * @return The decoded ASN.1 null element.
     * @throws ASN1Exception If the provided element cannot be decoded as a null
     *                       element.
     */
    @NonNull()
    public static ASN1Null decodeAsNull(@NonNull final ASN1Element element)
            throws ASN1Exception {
        if (element.getValue().length != 0) {
            throw new ASN1Exception(ERR_NULL_HAS_VALUE.get());
        }

        return new ASN1Null(element.getType());
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    public void toString(@NonNull final StringBuilder buffer) {
        buffer.append("ASN1Null(type=");
        Hexs.toHex(getType(), buffer);
        buffer.append(')');
    }
}
