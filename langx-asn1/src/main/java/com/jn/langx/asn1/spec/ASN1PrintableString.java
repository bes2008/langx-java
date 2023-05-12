package com.jn.langx.asn1.spec;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.io.unicode.Utf8s;

import static com.jn.langx.asn1.spec.ASN1Messages.*;

/**
 * This class provides an ASN.1 printable string element that can hold any
 * empty or non-empty string comprised only of the characters listed below.
 * <UL>
 * <LI>The uppercase ASCII letters A through Z.</LI>
 * <LI>The lowercase ASCII letters a through z.</LI>
 * <LI>The ASCII digits 0 through 9.</LI>
 * <LI>The ASCII space.</LI>
 * <LI>The ASCII apostrophe (aka single quote).</LI>
 * <LI>The ASCII left parenthesis.</LI>
 * <LI>The ASCII right parenthesis.</LI>
 * <LI>The ASCII plus sign.</LI>
 * <LI>The ASCII comma.</LI>
 * <LI>The ASCII minus sign (aka hyphen).</LI>
 * <LI>The ASCII period (aka full stop).</LI>
 * <LI>The ASCII forward slash (aka solidus).</LI>
 * <LI>The ASCII colon.</LI>
 * <LI>The ASCII equal sign.</LI>
 * <LI>The ASCII question mark.</LI>
 * </UL>
 */
public final class ASN1PrintableString
        extends ASN1Element {
    /**
     * The serial version UID for this serializable class.
     */
    private static final long serialVersionUID = 7489436088285132189L;


    // The string value for this element.
    @NonNull
    private final String stringValue;


    /**
     * Creates a new ASN.1 printable string element with the default BER type and
     * the provided value.
     *
     * @param stringValue The string value to use for this element.  It may be
     *                    {@code null} or empty if the value should be empty.
     *                    It must only contain characters allowed in printable
     *                    strings.
     * @throws ASN1Exception If the provided string does not represent a valid
     *                       printable string.
     */
    public ASN1PrintableString(@Nullable final String stringValue)
            throws ASN1Exception {
        this(ASN1Constants.UNIVERSAL_PRINTABLE_STRING_TYPE, stringValue);
    }


    /**
     * Creates a new ASN.1 printable string element with the specified BER type
     * and the provided value.
     *
     * @param type        The BER type for this element.
     * @param stringValue The string value to use for this element.  It may be
     *                    {@code null} or empty if the value should be empty.
     *                    It must only contain characters allowed in printable
     *                    strings.
     * @throws ASN1Exception If the provided string does not represent a valid
     *                       printable string.
     */
    public ASN1PrintableString(final byte type,
                               @Nullable final String stringValue)
            throws ASN1Exception {
        this(type, stringValue, Utf8s.getBytes(stringValue));
    }


    /**
     * Creates a new ASN.1 printable string element with the specified BER type
     * and the provided value.
     *
     * @param type         The BER type for this element.
     * @param stringValue  The string value to use for this element.  It may be
     *                     {@code null} or empty if the value should be empty.
     *                     It must only contain characters allowed in printable
     *                     strings.
     * @param encodedValue The bytes that comprise the encoded element value.
     * @throws ASN1Exception If the provided string does not represent a valid
     *                       printable string.
     */
    private ASN1PrintableString(final byte type,
                                @Nullable final String stringValue,
                                @NonNull final byte[] encodedValue)
            throws ASN1Exception {
        super(type, encodedValue);

        if (stringValue == null) {
            this.stringValue = "";
        } else {
            this.stringValue = stringValue;
            if (!isPrintableString(encodedValue)) {
                throw new ASN1Exception(
                        ERR_PRINTABLE_STRING_DECODE_VALUE_NOT_PRINTABLE.get());
            }
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
     * Decodes the contents of the provided byte array as a printable string
     * element.
     *
     * @param elementBytes The byte array to decode as an ASN.1 printable string
     *                     element.
     * @return The decoded ASN.1 printable string element.
     * @throws ASN1Exception If the provided array cannot be decoded as a
     *                       printable string element.
     */
    @NonNull()
    public static ASN1PrintableString decodeAsPrintableString(
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

            return new ASN1PrintableString(elementBytes[0],
                    Utf8s.toString(elementValue), elementValue);
        } catch (final ASN1Exception ae) {
            throw ae;
        } catch (final Exception e) {
            throw new ASN1Exception(ERR_ELEMENT_DECODE_EXCEPTION.get(e), e);
        }
    }


    /**
     * Decodes the provided ASN.1 element as a printable string element.
     *
     * @param element The ASN.1 element to be decoded.
     * @return The decoded ASN.1 printable string element.
     * @throws ASN1Exception If the provided element cannot be decoded as a
     *                       printable string element.
     */
    @NonNull()
    public static ASN1PrintableString decodeAsPrintableString(
            @NonNull final ASN1Element element)
            throws ASN1Exception {
        final byte[] elementValue = element.getValue();
        return new ASN1PrintableString(element.getType(),
                Utf8s.toString(elementValue), elementValue);
    }


    /**
     * Indicates whether the contents of the provided byte array represent a
     * printable LDAP string, as per RFC 4517 section 3.2.  The only characters
     * allowed in a printable string are:
     * <UL>
     * <LI>All uppercase and lowercase ASCII alphabetic letters</LI>
     * <LI>All ASCII numeric digits</LI>
     * <LI>The following additional ASCII characters:  single quote, left
     * parenthesis, right parenthesis, plus, comma, hyphen, period, equals,
     * forward slash, colon, question mark, space.</LI>
     * </UL>
     * If the provided array contains anything other than the above characters
     * (i.e., if the byte array contains any non-ASCII characters, or any ASCII
     * control characters, or if it contains excluded ASCII characters like
     * the exclamation point, double quote, octothorpe, dollar sign, etc.), then
     * it will not be considered printable.
     *
     * @param b The byte array for which to make the determination.  It must
     *          not be {@code null}.
     * @return {@code true} if the contents of the provided byte array represent
     * a printable LDAP string, or {@code false} if not.
     */
    public static boolean isPrintableString(@NonNull final byte[] b) {
        for (final byte by : b) {
            if ((by & 0x80) == 0x80) {
                return false;
            }

            if (((by >= 'a') && (by <= 'z')) ||
                    ((by >= 'A') && (by <= 'Z')) ||
                    ((by >= '0') && (by <= '9'))) {
                continue;
            }

            switch (by) {
                case '\'':
                case '(':
                case ')':
                case '+':
                case ',':
                case '-':
                case '.':
                case '=':
                case '/':
                case ':':
                case '?':
                case ' ':
                    continue;
                default:
                    return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override()
    public void toString(@NonNull final StringBuilder buffer) {
        buffer.append(stringValue);
    }
}
