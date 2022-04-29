package com.jn.langx.asn1.spec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.asn1.OID;
import com.jn.langx.asn1.bytestring.ByteStringBuffer;

import static com.jn.langx.asn1.spec.ASN1Messages.*;


/**
 * This class provides an ASN.1 object identifier element, whose value
 * represents a numeric OID.  Note that ASN.1 object identifier elements must
 * strictly conform to the numeric OID specification, which has the following
 * requirements:
 * <UL>
 * <LI>All valid OIDs must contain at least two components.</LI>
 * <LI>The value of the first component must be 0, 1, or 2.</LI>
 * <LI>If the value of the first component is 0 or 1, then the value of the
 * second component must not be greater than 39.</LI>
 * </UL>
 */
public final class ASN1ObjectIdentifier
        extends ASN1Element {
    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = -777778295086222273L;


    // The OID represented by this object identifier element.
    @NonNull
    private final OID oid;


    /**
     * Creates a new ASN.1 object identifier element with the default BER type and
     * the provided OID.
     *
     * @param oid The OID to represent with this element.  It must not be
     *            {@code null}, and it must represent a valid OID.
     * @throws ASN1Exception If the provided OID does not strictly adhere to the
     *                       numeric OID format.
     */
    public ASN1ObjectIdentifier(@NonNull final OID oid)
            throws ASN1Exception {
        this(ASN1Constants.UNIVERSAL_OBJECT_IDENTIFIER_TYPE, oid);
    }


    /**
     * Creates a new ASN.1 object identifier element with the specified BER type
     * and the provided OID.
     *
     * @param type The BER type for this element.
     * @param oid  The OID to represent with this element.  It must not be
     *             {@code null}, and it must represent a valid OID.
     * @throws ASN1Exception If the provided OID does not strictly adhere to the
     *                       numeric OID format.
     */
    public ASN1ObjectIdentifier(final byte type, @NonNull final OID oid)
            throws ASN1Exception {
        this(type, oid, encodeValue(oid));
    }


    /**
     * Creates a new ASN.1 object identifier element with the default BER type and
     * the provided OID.
     *
     * @param oidString The string representation of the OID to represent with
     *                  this element.  It must not be {@code null}, and it must
     *                  represent a valid OID.
     * @throws ASN1Exception If the provided OID does not strictly adhere to the
     *                       numeric OID format.
     */
    public ASN1ObjectIdentifier(@NonNull final String oidString)
            throws ASN1Exception {
        this(ASN1Constants.UNIVERSAL_OBJECT_IDENTIFIER_TYPE, oidString);
    }


    /**
     * Creates a new ASN.1 object identifier element with the specified BER type
     * and the provided OID.
     *
     * @param type      The BER type for this element.
     * @param oidString The string representation of the OID to represent with
     *                  this element.  It must not be {@code null}, and it must
     *                  represent a valid OID.
     * @throws ASN1Exception If the provided OID does not strictly adhere to the
     *                       numeric OID format.
     */
    public ASN1ObjectIdentifier(final byte type,
                                @NonNull final String oidString)
            throws ASN1Exception {
        this(type, new OID(oidString));
    }


    /**
     * Creates a new ASN.1 object identifier element with the provided
     * information.
     *
     * @param type         The BER type to use for this element.
     * @param oid          The OID to represent with this element.
     * @param encodedValue The encoded value for this element.
     */
    private ASN1ObjectIdentifier(final byte type, @NonNull final OID oid,
                                 @NonNull final byte[] encodedValue) {
        super(type, encodedValue);

        this.oid = oid;
    }


    /**
     * Generates an encoded value for an object identifier element with the
     * provided OID.
     *
     * @param oid The OID to represent with this element.  It must not be
     *            {@code null}, and it must represent a valid OID.
     * @return The encoded value.
     * @throws ASN1Exception If the provided OID does not strictly conform to
     *                       the requirements for ASN.1 OIDs.
     */
    @NonNull()
    private static byte[] encodeValue(@NonNull final OID oid)
            throws ASN1Exception {
        // Make sure that the provided UID conforms to the necessary constraints.
        if (!oid.isValidNumericOID()) {
            throw new ASN1Exception(ERR_OID_ENCODE_NOT_NUMERIC.get());
        }

        final List<Integer> components = oid.getComponents();
        if (components.size() < 2) {
            throw new ASN1Exception(ERR_OID_ENCODE_NOT_ENOUGH_COMPONENTS.get(
                    oid.toString()));
        }

        final Iterator<Integer> componentIterator = components.iterator();

        final int firstComponent = componentIterator.next();
        if ((firstComponent < 0) || (firstComponent > 2)) {
            throw new ASN1Exception(ERR_OID_ENCODE_INVALID_FIRST_COMPONENT.get(
                    oid.toString(), firstComponent));
        }

        final int secondComponent = componentIterator.next();
        if ((secondComponent < 0) ||
                ((firstComponent != 2) && (secondComponent > 39))) {
            throw new ASN1Exception(ERR_OID_ENCODE_INVALID_SECOND_COMPONENT.get(
                    oid.toString(), firstComponent, secondComponent));
        }


        // Construct the encoded representation of the OID.  Compute it as follows:
        // - The first and second components are merged together by multiplying the
        //   value of the first component by 40 and adding the value of the second
        //   component.  Every other component is handled individually.
        // - For components (including the merged first and second components) whose
        //   value is less than or equal to 127, the encoded representation of that
        //   component is simply the single-byte encoded representation of that
        //   number.
        // - For components (including the merged first and second components) whose
        //   value is greater than 127, that component must be encoded in multiple
        //   bytes.  In the encoded representation, only the lower seven bits of
        //   each byte will be used to convey the value.  The most significant bit
        //   of each byte will be used to indicate whether there are more bytes in
        //   the component.
        final ByteStringBuffer buffer = new ByteStringBuffer();
        final int mergedFirstComponents = (40 * firstComponent) + secondComponent;
        encodeComponent(mergedFirstComponents, buffer);
        while (componentIterator.hasNext()) {
            encodeComponent(componentIterator.next(), buffer);
        }

        return buffer.toByteArray();
    }


    /**
     * Appends an encoded representation of the provided component value to the
     * given buffer.
     *
     * @param c The value of the component to encode.
     * @param b The buffer to which the encoded representation should be
     *          appended.
     */
    private static void encodeComponent(final int c,
                                        @NonNull final ByteStringBuffer b) {
        final int finalByte = c & 0x7F;
        if (finalByte == c) {
            b.append((byte) finalByte);
        } else if ((c & 0x3FFF) == c) {
            b.append((byte) (0x80 | ((c >> 7) & 0x7F)));
            b.append((byte) finalByte);
        } else if ((c & 0xFFFFF) == c) {
            b.append((byte) (0x80 | ((c >> 14) & 0x7F)));
            b.append((byte) (0x80 | ((c >> 7) & 0x7F)));
            b.append((byte) finalByte);
        } else if ((c & 0xFFFFFF) == c) {
            b.append((byte) (0x80 | ((c >> 21) & 0x7F)));
            b.append((byte) (0x80 | ((c >> 14) & 0x7F)));
            b.append((byte) (0x80 | ((c >> 7) & 0x7F)));
            b.append((byte) finalByte);
        } else {
            b.append((byte) (0x80 | ((c >> 28) & 0x7F)));
            b.append((byte) (0x80 | ((c >> 21) & 0x7F)));
            b.append((byte) (0x80 | ((c >> 14) & 0x7F)));
            b.append((byte) (0x80 | ((c >> 7) & 0x7F)));
            b.append((byte) finalByte);
        }
    }


    /**
     * Retrieves the OID represented by this object identifier element.
     *
     * @return The OID represented by this object identifier element.
     */
    @NonNull()
    public OID getOID() {
        return oid;
    }


    /**
     * Decodes the contents of the provided byte array as an object identifier
     * element.
     *
     * @param elementBytes The byte array to decode as an ASN.1 object
     *                     identifier element.
     * @return The decoded ASN.1 object identifier element.
     * @throws ASN1Exception If the provided array cannot be decoded as an
     *                       object identifier element.
     */
    @NonNull()
    public static ASN1ObjectIdentifier decodeAsObjectIdentifier(
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
            final OID oid = decodeValue(elementValue);
            return new ASN1ObjectIdentifier(elementBytes[0], oid, elementValue);
        } catch (final ASN1Exception ae) {
            //Debug.debugException(ae);
            throw ae;
        } catch (final Exception e) {
            //Debug.debugException(e);
            throw new ASN1Exception(ERR_ELEMENT_DECODE_EXCEPTION.get(e), e);
        }
    }


    /**
     * Decodes the provided ASN.1 element as an object identifier element.
     *
     * @param element The ASN.1 element to be decoded.
     * @return The decoded ASN.1 object identifier element.
     * @throws ASN1Exception If the provided element cannot be decoded as an
     *                       object identifier element.
     */
    @NonNull()
    public static ASN1ObjectIdentifier decodeAsObjectIdentifier(
            @NonNull final ASN1Element element)
            throws ASN1Exception {
        final OID oid = decodeValue(element.getValue());
        return new ASN1ObjectIdentifier(element.getType(), oid, element.getValue());
    }


    /**
     * Decodes the provided value as an OID.
     *
     * @param elementValue The bytes that comprise the encoded value for an
     *                     object identifier element.
     * @return The decoded OID.
     * @throws ASN1Exception If the provided value cannot be decoded as a valid
     *                       OID.
     */
    @NonNull()
    private static OID decodeValue(@NonNull final byte[] elementValue)
            throws ASN1Exception {
        if (elementValue.length == 0) {
            throw new ASN1Exception(ERR_OID_DECODE_EMPTY_VALUE.get());
        }

        final byte lastByte = elementValue[elementValue.length - 1];
        if ((lastByte & 0x80) == 0x80) {
            throw new ASN1Exception(ERR_OID_DECODE_INCOMPLETE_VALUE.get());
        }

        int currentComponent = 0x00;
        final ArrayList<Integer> components = new ArrayList<Integer>(elementValue.length);
        for (final byte b : elementValue) {
            currentComponent <<= 7;
            currentComponent |= (b & 0x7F);
            if ((b & 0x80) == 0x00) {
                if (components.isEmpty()) {
                    if (currentComponent < 40) {
                        components.add(0);
                        components.add(currentComponent);
                    } else if (currentComponent < 80) {
                        components.add(1);
                        components.add(currentComponent - 40);
                    } else {
                        components.add(2);
                        components.add(currentComponent - 80);
                    }
                } else {
                    components.add(currentComponent);
                }

                currentComponent = 0x00;
            }
        }

        return new OID(components);
    }


    /**
     * {@inheritDoc}
     */
    @Override()
    public void toString(@NonNull final StringBuilder buffer) {
        buffer.append(oid.toString());
    }
}
