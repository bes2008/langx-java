package com.jn.langx.asn1.spec;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotExtensible;
import com.jn.langx.annotation.NotMutable;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.asn1.bytestring.ByteStringBuffer;
import com.jn.langx.codec.hex.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.jn.langx.asn1.spec.ASN1Messages.*;


/**
 * This class defines a generic ASN.1 BER element, which has a type and value.
 * It provides a framework for encoding and decoding BER elements, both as
 * generic elements and more specific subtypes.
 */
@NotExtensible()
@NotMutable()
public class ASN1Element
        implements Serializable {
    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = -1871166128693521335L;


    // The BER type for this element.
    private final byte type;

    // The encoded value for this element.
    @NonNull
    private final byte[] value;

    // The cached hashCode for this element.
    private int hashCode = -1;

    // The number of bytes contained in the value.
    private final int valueLength;

    // The offset within the value array at which the value begins.
    private final int valueOffset;


    /**
     * Creates a new ASN.1 BER element with the specified type and no value.
     *
     * @param type The BER type for this element.
     */
    public ASN1Element(final byte type) {
        this.type = type;
        value = ASN1Constants.NO_VALUE;
        valueOffset = 0;
        valueLength = 0;
    }


    /**
     * Creates a new ASN1 BER element with the specified type and value.
     *
     * @param type  The BER type for this element.
     * @param value The encoded value for this element.
     */
    public ASN1Element(final byte type, @Nullable final byte[] value) {
        this.type = type;

        if (value == null) {
            this.value = ASN1Constants.NO_VALUE;
        } else {
            this.value = value;
        }

        valueOffset = 0;
        valueLength = this.value.length;
    }


    /**
     * Creates a new ASN1 BER element with the specified type and value.
     *
     * @param type   The BER type for this element.
     * @param value  The array containing the encoded value for this element.
     *               It must not be {@code null}.
     * @param offset The offset within the array at which the value begins.
     * @param length The number of bytes contained in the value.
     */
    public ASN1Element(final byte type, @NonNull final byte[] value,
                       final int offset, final int length) {
        this.type = type;
        this.value = value;

        valueOffset = offset;
        valueLength = length;
    }


    /**
     * Retrieves the BER type for this element.
     *
     * @return The BER type for this element.
     */
    public final byte getType() {
        return type;
    }


    /**
     * Retrieves a value that corresponds to the type class for this element.  The
     * value returned will be one of
     * {@link ASN1Constants#TYPE_MASK_UNIVERSAL_CLASS},
     * {@link ASN1Constants#TYPE_MASK_APPLICATION_CLASS},
     * {@link ASN1Constants#TYPE_MASK_CONTEXT_SPECIFIC_CLASS}, or
     * {@link ASN1Constants#TYPE_MASK_PRIVATE_CLASS}.
     *
     * @return A value that corresponds to the type class for this element.
     */
    public byte getTypeClass() {
        return (byte) (type & 0xC0);
    }


    /**
     * Indicates whether the type indicates that this element is constructed.  A
     * constructed element is one whose value is comprised of the encoded
     * representation of zero or more ASN.1 elements.  If the type does not
     * indicate that the element is constructed, then the element is considered
     * primitive.
     *
     * @return {@code true} if the type indicates that the element is
     * constructed, or {@code false} if the type indicates that the
     * element is primitive.
     */
    public boolean isConstructed() {
        return ((type & ASN1Constants.TYPE_MASK_PC_CONSTRUCTED) != 0x00);
    }


    /**
     * Retrieves the array containing the value.  The returned array may be
     * larger than the actual value, so it must be used in conjunction with the
     * values returned by the {@link #getValueOffset} and {@link #getValueLength}
     * methods.
     *
     * @return The array containing the value.
     */
    @NonNull()
    byte[] getValueArray() {
        return value;
    }


    /**
     * Retrieves the position in the value array at which the value actually
     * begins.
     *
     * @return The position in the value array at which the value actually
     * begins.
     */
    int getValueOffset() {
        return valueOffset;
    }


    /**
     * Retrieves the number of bytes contained in the value.
     *
     * @return The number of bytes contained in the value.
     */
    public int getValueLength() {
        return valueLength;
    }


    /**
     * Retrieves the encoded value for this element.
     *
     * @return The encoded value for this element.
     */
    @NonNull()
    public byte[] getValue() {
        if ((valueOffset == 0) && (valueLength == value.length)) {
            return value;
        } else {
            final byte[] returnValue = new byte[valueLength];
            System.arraycopy(value, valueOffset, returnValue, 0, valueLength);
            return returnValue;
        }
    }


    /**
     * Encodes this ASN.1 element to a byte array.
     *
     * @return A byte array containing the encoded representation of this ASN.1
     * element.
     */
    @NonNull()
    public final byte[] encode() {
        final byte[] valueArray = getValueArray();
        final int length = getValueLength();
        final int offset = getValueOffset();

        if (length == 0) {
            return new byte[]{type, 0x00};
        }

        final byte[] lengthBytes = encodeLength(length);
        final byte[] elementBytes = new byte[1 + lengthBytes.length + length];

        elementBytes[0] = type;
        System.arraycopy(lengthBytes, 0, elementBytes, 1, lengthBytes.length);
        System.arraycopy(valueArray, offset, elementBytes, 1 + lengthBytes.length,
                length);

        return elementBytes;
    }


    /**
     * Encodes the provided length to the given buffer.
     *
     * @param length The length to be encoded.
     * @param buffer The buffer to which the length should be appended.
     */
    static void encodeLengthTo(final int length,
                               @NonNull final ByteStringBuffer buffer) {
        if ((length & 0x7F) == length) {
            buffer.append((byte) length);
        } else if ((length & 0xFF) == length) {
            buffer.append((byte) 0x81);
            buffer.append((byte) (length & 0xFF));
        } else if ((length & 0xFFFF) == length) {
            buffer.append((byte) 0x82);
            buffer.append((byte) ((length >> 8) & 0xFF));
            buffer.append((byte) (length & 0xFF));
        } else if ((length & 0x00FFFFFF) == length) {
            buffer.append((byte) 0x83);
            buffer.append((byte) ((length >> 16) & 0xFF));
            buffer.append((byte) ((length >> 8) & 0xFF));
            buffer.append((byte) (length & 0xFF));
        } else {
            buffer.append((byte) 0x84);
            buffer.append((byte) ((length >> 24) & 0xFF));
            buffer.append((byte) ((length >> 16) & 0xFF));
            buffer.append((byte) ((length >> 8) & 0xFF));
            buffer.append((byte) (length & 0xFF));
        }
    }


    /**
     * Appends an encoded representation of this ASN.1 element to the provided
     * buffer.
     *
     * @param buffer The buffer to which the encoded representation should be
     *               appended.
     */
    public void encodeTo(@NonNull final ByteStringBuffer buffer) {
        final byte[] valueArray = getValueArray();
        final int length = getValueLength();
        final int offset = getValueOffset();

        buffer.append(type);
        if (length == 0) {
            buffer.append((byte) 0x00);
        } else {
            encodeLengthTo(length, buffer);
            buffer.append(valueArray, offset, length);
        }
    }

    private static Map<Integer, byte[]> encodeLengthMap = new LinkedHashMap<Integer, byte[]>();

    static {
        for (int i = 0; i < 0x80; i++) {
            encodeLengthMap.put(i, new byte[]{(byte) i});
        }
    }

    /**
     * Encodes the provided length to a byte array.
     *
     * @param length The length to be encoded.
     * @return A byte array containing the encoded length.
     */
    @NonNull()
    public static byte[] encodeLength(final int length) {
        if (length >= 0x00 && length < 0x80) {
            return encodeLengthMap.get(length);
        }

        if ((length & 0x000000FF) == length) {
            return new byte[]
                    {
                            (byte) 0x81,
                            (byte) (length & 0xFF)
                    };
        } else if ((length & 0x0000FFFF) == length) {
            return new byte[]
                    {
                            (byte) 0x82,
                            (byte) ((length >> 8) & 0xFF),
                            (byte) (length & 0xFF)
                    };
        } else if ((length & 0x00FFFFFF) == length) {
            return new byte[]
                    {
                            (byte) 0x83,
                            (byte) ((length >> 16) & 0xFF),
                            (byte) ((length >> 8) & 0xFF),
                            (byte) (length & 0xFF)
                    };
        } else {
            return new byte[]
                    {
                            (byte) 0x84,
                            (byte) ((length >> 24) & 0xFF),
                            (byte) ((length >> 16) & 0xFF),
                            (byte) ((length >> 8) & 0xFF),
                            (byte) (length & 0xFF)
                    };
        }
    }


    /**
     * Decodes the content in the provided byte array as an ASN.1 element.
     *
     * @param elementBytes The byte array containing the data to decode.
     * @return The decoded ASN.1 BER element.
     * @throws ASN1Exception If the provided byte array does not represent a
     *                       valid ASN.1 element.
     */
    @NonNull()
    public static ASN1Element decode(@NonNull final byte[] elementBytes)
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
            return new ASN1Element(elementBytes[0], value);
        } catch (final ASN1Exception ae) {
            throw ae;
        } catch (final Exception e) {
            throw new ASN1Exception(ERR_ELEMENT_DECODE_EXCEPTION.get(e), e);
        }
    }


    /**
     * Decodes this ASN.1 element as a bit string element.
     *
     * @return The decoded bit string element.
     * @throws ASN1Exception If this element cannot be decoded as a bit string
     *                       element.
     */
    @NonNull()
    public final ASN1BitString decodeAsBitString()
            throws ASN1Exception {
        return ASN1BitString.decodeAsBitString(this);
    }


    /**
     * Decodes this ASN.1 element as a Boolean element.
     *
     * @return The decoded Boolean element.
     * @throws ASN1Exception If this element cannot be decoded as a Boolean
     *                       element.
     */
    @NonNull()
    public final ASN1Boolean decodeAsBoolean()
            throws ASN1Exception {
        return ASN1Boolean.decodeAsBoolean(this);
    }


    /**
     * Decodes this ASN.1 element as an enumerated element.
     *
     * @return The decoded enumerated element.
     * @throws ASN1Exception If this element cannot be decoded as an enumerated
     *                       element.
     */
    @NonNull()
    public final ASN1Enumerated decodeAsEnumerated()
            throws ASN1Exception {
        return ASN1Enumerated.decodeAsEnumerated(this);
    }


    /**
     * Decodes this ASN.1 element as a generalized time element.
     *
     * @return The decoded generalized time element.
     * @throws ASN1Exception If this element cannot be decoded as a generalized
     *                       time element.
     */
    @NonNull()
    public final ASN1GeneralizedTime decodeAsGeneralizedTime()
            throws ASN1Exception {
        return ASN1GeneralizedTime.decodeAsGeneralizedTime(this);
    }


    /**
     * Decodes this ASN.1 element as an IA5 string element.
     *
     * @return The decoded IA5 string element.
     * @throws ASN1Exception If this element cannot be decoded as a IA5 string
     *                       element.
     */
    @NonNull()
    public final ASN1IA5String decodeAsIA5String()
            throws ASN1Exception {
        return ASN1IA5String.decodeAsIA5String(this);
    }


    /**
     * Decodes this ASN.1 element as an integer element.
     *
     * @return The decoded integer element.
     * @throws ASN1Exception If this element cannot be decoded as an integer
     *                       element.
     */
    @NonNull()
    public final ASN1Integer decodeAsInteger()
            throws ASN1Exception {
        return ASN1Integer.decodeAsInteger(this);
    }


    /**
     * Decodes this ASN.1 element as a long element.
     *
     * @return The decoded long element.
     * @throws ASN1Exception If this element cannot be decoded as a long
     *                       element.
     */
    @NonNull()
    public final ASN1Long decodeAsLong()
            throws ASN1Exception {
        return ASN1Long.decodeAsLong(this);
    }


    /**
     * Decodes this ASN.1 element as a big integer element.
     *
     * @return The decoded big integer element.
     * @throws ASN1Exception If this element cannot be decoded as a big integer
     *                       element.
     */
    @NonNull()
    public final ASN1BigInteger decodeAsBigInteger()
            throws ASN1Exception {
        return ASN1BigInteger.decodeAsBigInteger(this);
    }


    /**
     * Decodes this ASN.1 element as a null element.
     *
     * @return The decoded null element.
     * @throws ASN1Exception If this element cannot be decoded as a null
     *                       element.
     */
    @NonNull()
    public final ASN1Null decodeAsNull()
            throws ASN1Exception {
        return ASN1Null.decodeAsNull(this);
    }


    /**
     * Decodes this ASN.1 element as a numeric string element.
     *
     * @return The decoded numeric string element.
     * @throws ASN1Exception If this element cannot be decoded as a numeric
     *                       string element.
     */
    @NonNull()
    public final ASN1NumericString decodeAsNumericString()
            throws ASN1Exception {
        return ASN1NumericString.decodeAsNumericString(this);
    }


    /**
     * Decodes this ASN.1 element as an object identifier element.
     *
     * @return The decoded object identifier element.
     * @throws ASN1Exception If this element cannot be decoded as an object
     *                       identifier element.
     */
    @NonNull()
    public final ASN1ObjectIdentifier decodeAsObjectIdentifier()
            throws ASN1Exception {
        return ASN1ObjectIdentifier.decodeAsObjectIdentifier(this);
    }


    /**
     * Decodes this ASN.1 element as an octet string element.
     *
     * @return The decoded octet string element.
     */
    @NonNull()
    public final ASN1OctetString decodeAsOctetString() {
        return ASN1OctetString.decodeAsOctetString(this);
    }


    /**
     * Decodes this ASN.1 element as a printable string element.
     *
     * @return The decoded printable string element.
     * @throws ASN1Exception If this element cannot be decoded as a printable
     *                       string element.
     */
    @NonNull()
    public final ASN1PrintableString decodeAsPrintableString()
            throws ASN1Exception {
        return ASN1PrintableString.decodeAsPrintableString(this);
    }


    /**
     * Decodes this ASN.1 element as a sequence element.
     *
     * @return The decoded sequence element.
     * @throws ASN1Exception If this element cannot be decoded as a sequence
     *                       element.
     */
    @NonNull()
    public final ASN1Sequence decodeAsSequence()
            throws ASN1Exception {
        return ASN1Sequence.decodeAsSequence(this);
    }


    /**
     * Decodes this ASN.1 element as a set element.
     *
     * @return The decoded set element.
     * @throws ASN1Exception If this element cannot be decoded as a set
     *                       element.
     */
    @NonNull()
    public final ASN1Set decodeAsSet()
            throws ASN1Exception {
        return ASN1Set.decodeAsSet(this);
    }


    /**
     * Decodes this ASN.1 element as a UTC time element.
     *
     * @return The decoded UTC time element.
     * @throws ASN1Exception If this element cannot be decoded as a UTC time
     *                       element.
     */
    @NonNull()
    public final ASN1UTCTime decodeAsUTCTime()
            throws ASN1Exception {
        return ASN1UTCTime.decodeAsUTCTime(this);
    }


    /**
     * Decodes this ASN.1 element as a UTF-8 string element.
     *
     * @return The decoded UTF_8 string element.
     * @throws ASN1Exception If this element cannot be decoded as a UTF-8
     *                       string element.
     */
    @NonNull()
    public final ASN1UTF8String decodeAsUTF8String()
            throws ASN1Exception {
        return ASN1UTF8String.decodeAsUTF8String(this);
    }


    /**
     * Reads an ASN.1 element from the provided input stream.
     *
     * @param inputStream The input stream from which to read the element.
     * @return The element read from the input stream, or {@code null} if the end
     * of the input stream is reached without reading any data.
     * @throws IOException   If a problem occurs while attempting to read from the
     *                       input stream.
     * @throws ASN1Exception If a problem occurs while attempting to decode the
     *                       element.
     */
    @Nullable()
    public static ASN1Element readFrom(@NonNull final InputStream inputStream)
            throws IOException, ASN1Exception {
        return readFrom(inputStream, -1);
    }


    /**
     * Reads an ASN.1 element from the provided input stream.
     *
     * @param inputStream The input stream from which to read the element.
     * @param maxSize     The maximum value size in bytes that will be allowed.
     *                    A value less than or equal to zero indicates that no
     *                    maximum size should be enforced.  An attempt to read
     *                    an element with a value larger than this will cause an
     *                    {@code ASN1Exception} to be thrown.
     * @return The element read from the input stream, or {@code null} if the end
     * of the input stream is reached without reading any data.
     * @throws IOException   If a problem occurs while attempting to read from the
     *                       input stream.
     * @throws ASN1Exception If a problem occurs while attempting to decode the
     *                       element.
     */
    @Nullable()
    public static ASN1Element readFrom(@NonNull final InputStream inputStream,
                                       final int maxSize)
            throws IOException, ASN1Exception {
        final int typeInt = inputStream.read();
        if (typeInt < 0) {
            return null;
        }

        final byte type = (byte) typeInt;

        int length = inputStream.read();
        if (length < 0) {
            throw new ASN1Exception(ERR_READ_END_BEFORE_FIRST_LENGTH.get());
        } else if (length > 127) {
            final int numLengthBytes = length & 0x7F;
            length = 0;
            if ((numLengthBytes < 1) || (numLengthBytes > 4)) {
                throw new ASN1Exception(ERR_READ_LENGTH_TOO_LONG.get(numLengthBytes));
            }

            for (int i = 0; i < numLengthBytes; i++) {
                final int lengthInt = inputStream.read();
                if (lengthInt < 0) {
                    throw new ASN1Exception(ERR_READ_END_BEFORE_LENGTH_END.get());
                }

                length <<= 8;
                length |= (lengthInt & 0xFF);
            }
        }

        if ((length < 0) || ((maxSize > 0) && (length > maxSize))) {
            throw new ASN1Exception(ERR_READ_LENGTH_EXCEEDS_MAX.get(length, maxSize));
        }

        int totalBytesRead = 0;
        int bytesRemaining = length;
        final byte[] value = new byte[length];
        while (totalBytesRead < length) {
            final int bytesRead = inputStream.read(value, totalBytesRead, bytesRemaining);
            if (bytesRead < 0) {
                throw new ASN1Exception(ERR_READ_END_BEFORE_VALUE_END.get());
            }

            totalBytesRead += bytesRead;
            bytesRemaining -= bytesRead;
        }

        final ASN1Element e = new ASN1Element(type, value);
        return e;
    }


    /**
     * Writes an encoded representation of this ASN.1 element to the provided
     * output stream.
     *
     * @param outputStream The output stream to which the element should be
     *                     written.
     * @return The total number of bytes written to the output stream.
     * @throws IOException If a problem occurs while attempting to write to the
     *                     provided output stream.
     * @see ASN1Writer#writeElement(ASN1Element, OutputStream)
     */
    public final int writeTo(@NonNull final OutputStream outputStream) throws IOException {
        final ByteStringBuffer buffer = new ByteStringBuffer();
        encodeTo(buffer);
        buffer.write(outputStream);
        return buffer.length();
    }


    /**
     * Retrieves a hash code for this ASN.1 BER element.
     *
     * @return A hash code for this ASN.1 BER element.
     */
    @Override()
    public final int hashCode() {
        if (hashCode == -1) {
            int hash = 0;
            for (final byte b : getValue()) {
                hash = hash * 31 + b;
            }
            hashCode = hash;
        }

        return hashCode;
    }


    /**
     * Indicates whether the provided object is equal to this ASN.1 BER element.
     * The object will only be considered equal to this ASN.1 element if it is a
     * non-null ASN.1 element with the same type and value as this element.
     *
     * @param o The object for which to make the determination.
     * @return {@code true} if the provided object is considered equal to this
     * ASN.1 element, or {@code false} if not.
     */
    @Override()
    public final boolean equals(@Nullable final Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        try {
            final ASN1Element e = (ASN1Element) o;
            return ((type == e.getType()) && Arrays.equals(getValue(), e.getValue()));
        } catch (final Exception e) {
            return false;
        }
    }


    /**
     * Indicates whether the provided ASN.1 element is equal to this element,
     * ignoring any potential difference in the BER type.
     *
     * @param element The ASN.1 BER element for which to make the determination.
     * @return {@code true} if the provided ASN.1 element is considered equal to
     * this element (ignoring type differences), or {@code false} if not.
     */
    public final boolean equalsIgnoreType(@Nullable final ASN1Element element) {
        if (element == null) {
            return false;
        }

        if (element == this) {
            return true;
        }

        return Arrays.equals(getValue(), element.getValue());
    }


    /**
     * Retrieves a string representation of the value for ASN.1 element.
     *
     * @return A string representation of the value for this ASN.1 element.
     */
    @Override()
    @NonNull()
    public final String toString() {
        final StringBuilder buffer = new StringBuilder();
        toString(buffer);
        return buffer.toString();
    }


    /**
     * Appends a string representation of the value for this ASN.1 element to the
     * provided buffer.
     *
     * @param buffer The buffer to which to append the information.
     */
    public void toString(@NonNull final StringBuilder buffer) {
        final byte[] v = getValue();
        buffer.append("ASN1Element(type=");
        Hex.toHex(type, buffer);
        buffer.append(", valueLength=");
        buffer.append(v.length);
        buffer.append(", valueBytes='");
        Hex.toHex(v, buffer);
        buffer.append("')");
    }
}
