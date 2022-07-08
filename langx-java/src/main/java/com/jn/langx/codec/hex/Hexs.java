package com.jn.langx.codec.hex;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.asn1.bytestring.ByteStringBuffer;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.io.Utf8s;

import java.text.ParseException;

import static com.jn.langx.util.io.LineDelimiter.EOL;

public class Hexs {
    /**
     * Indicates whether the provided character is a valid hexadecimal digit.
     *
     * @param  c  The character for which to make the determination.
     *
     * @return  {@code true} if the provided character does represent a valid
     *          hexadecimal digit, or {@code false} if not.
     */
    public static boolean isHex(final char c)
    {
        switch (c)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case 'a':
            case 'A':
            case 'b':
            case 'B':
            case 'c':
            case 'C':
            case 'd':
            case 'D':
            case 'e':
            case 'E':
            case 'f':
            case 'F':
                return true;

            default:
                return false;
        }
    }



    /**
     * Retrieves a hexadecimal representation of the provided byte.
     *
     * @param  b  The byte to encode as hexadecimal.
     *
     * @return  A string containing the hexadecimal representation of the provided
     *          byte.
     */
    @NonNull()
    public static String toHex(final byte b)
    {
        final StringBuilder buffer = new StringBuilder(2);
        toHex(b, buffer);
        return buffer.toString();
    }



    /**
     * Appends a hexadecimal representation of the provided byte to the given
     * buffer.
     *
     * @param  b       The byte to encode as hexadecimal.
     * @param  buffer  The buffer to which the hexadecimal representation is to be
     *                 appended.
     */
    public static void toHex(final byte b, @NonNull final StringBuilder buffer)
    {
        switch (b & 0xF0)
        {
            case 0x00:
                buffer.append('0');
                break;
            case 0x10:
                buffer.append('1');
                break;
            case 0x20:
                buffer.append('2');
                break;
            case 0x30:
                buffer.append('3');
                break;
            case 0x40:
                buffer.append('4');
                break;
            case 0x50:
                buffer.append('5');
                break;
            case 0x60:
                buffer.append('6');
                break;
            case 0x70:
                buffer.append('7');
                break;
            case 0x80:
                buffer.append('8');
                break;
            case 0x90:
                buffer.append('9');
                break;
            case 0xA0:
                buffer.append('a');
                break;
            case 0xB0:
                buffer.append('b');
                break;
            case 0xC0:
                buffer.append('c');
                break;
            case 0xD0:
                buffer.append('d');
                break;
            case 0xE0:
                buffer.append('e');
                break;
            case 0xF0:
                buffer.append('f');
                break;
        }

        switch (b & 0x0F)
        {
            case 0x00:
                buffer.append('0');
                break;
            case 0x01:
                buffer.append('1');
                break;
            case 0x02:
                buffer.append('2');
                break;
            case 0x03:
                buffer.append('3');
                break;
            case 0x04:
                buffer.append('4');
                break;
            case 0x05:
                buffer.append('5');
                break;
            case 0x06:
                buffer.append('6');
                break;
            case 0x07:
                buffer.append('7');
                break;
            case 0x08:
                buffer.append('8');
                break;
            case 0x09:
                buffer.append('9');
                break;
            case 0x0A:
                buffer.append('a');
                break;
            case 0x0B:
                buffer.append('b');
                break;
            case 0x0C:
                buffer.append('c');
                break;
            case 0x0D:
                buffer.append('d');
                break;
            case 0x0E:
                buffer.append('e');
                break;
            case 0x0F:
                buffer.append('f');
                break;
        }
    }



    /**
     * Appends a hexadecimal representation of the provided byte to the given
     * buffer.
     *
     * @param  b       The byte to encode as hexadecimal.
     * @param  buffer  The buffer to which the hexadecimal representation is to be
     *                 appended.
     */
    public static void toHex(final byte b, @NonNull final ByteStringBuffer buffer)
    {
        switch (b & 0xF0)
        {
            case 0x00:
                buffer.append((byte) '0');
                break;
            case 0x10:
                buffer.append((byte) '1');
                break;
            case 0x20:
                buffer.append((byte) '2');
                break;
            case 0x30:
                buffer.append((byte) '3');
                break;
            case 0x40:
                buffer.append((byte) '4');
                break;
            case 0x50:
                buffer.append((byte) '5');
                break;
            case 0x60:
                buffer.append((byte) '6');
                break;
            case 0x70:
                buffer.append((byte) '7');
                break;
            case 0x80:
                buffer.append((byte) '8');
                break;
            case 0x90:
                buffer.append((byte) '9');
                break;
            case 0xA0:
                buffer.append((byte) 'a');
                break;
            case 0xB0:
                buffer.append((byte) 'b');
                break;
            case 0xC0:
                buffer.append((byte) 'c');
                break;
            case 0xD0:
                buffer.append((byte) 'd');
                break;
            case 0xE0:
                buffer.append((byte) 'e');
                break;
            case 0xF0:
                buffer.append((byte) 'f');
                break;
        }

        switch (b & 0x0F)
        {
            case 0x00:
                buffer.append((byte) '0');
                break;
            case 0x01:
                buffer.append((byte) '1');
                break;
            case 0x02:
                buffer.append((byte) '2');
                break;
            case 0x03:
                buffer.append((byte) '3');
                break;
            case 0x04:
                buffer.append((byte) '4');
                break;
            case 0x05:
                buffer.append((byte) '5');
                break;
            case 0x06:
                buffer.append((byte) '6');
                break;
            case 0x07:
                buffer.append((byte) '7');
                break;
            case 0x08:
                buffer.append((byte) '8');
                break;
            case 0x09:
                buffer.append((byte) '9');
                break;
            case 0x0A:
                buffer.append((byte) 'a');
                break;
            case 0x0B:
                buffer.append((byte) 'b');
                break;
            case 0x0C:
                buffer.append((byte) 'c');
                break;
            case 0x0D:
                buffer.append((byte) 'd');
                break;
            case 0x0E:
                buffer.append((byte) 'e');
                break;
            case 0x0F:
                buffer.append((byte) 'f');
                break;
        }
    }



    /**
     * Retrieves a hexadecimal representation of the contents of the provided byte
     * array.  No delimiter character will be inserted between the hexadecimal
     * digits for each byte.
     *
     * @param  b  The byte array to be represented as a hexadecimal string.  It
     *            must not be {@code null}.
     *
     * @return  A string containing a hexadecimal representation of the contents
     *          of the provided byte array.
     */
    @NonNull()
    public static String toHex(@NonNull final byte[] b)
    {
        Preconditions.checkNotNull(b);

        final StringBuilder buffer = new StringBuilder(2 * b.length);
        toHex(b, buffer);
        return buffer.toString();
    }



    /**
     * Retrieves a hexadecimal representation of the contents of the provided byte
     * array.  No delimiter character will be inserted between the hexadecimal
     * digits for each byte.
     *
     * @param  b       The byte array to be represented as a hexadecimal string.
     *                 It must not be {@code null}.
     * @param  buffer  A buffer to which the hexadecimal representation of the
     *                 contents of the provided byte array should be appended.
     */
    public static void toHex(@NonNull final byte[] b,
                             @NonNull final StringBuilder buffer)
    {
        toHex(b, null, buffer);
    }



    /**
     * Retrieves a hexadecimal representation of the contents of the provided byte
     * array.  No delimiter character will be inserted between the hexadecimal
     * digits for each byte.
     *
     * @param  b          The byte array to be represented as a hexadecimal
     *                    string.  It must not be {@code null}.
     * @param  delimiter  A delimiter to be inserted between bytes.  It may be
     *                    {@code null} if no delimiter should be used.
     * @param  buffer     A buffer to which the hexadecimal representation of the
     *                    contents of the provided byte array should be appended.
     */
    public static void toHex(@NonNull final byte[] b,
                             @Nullable final String delimiter,
                             @NonNull final StringBuilder buffer)
    {
        boolean first = true;
        for (final byte bt : b)
        {
            if (first)
            {
                first = false;
            }
            else if (delimiter != null)
            {
                buffer.append(delimiter);
            }

            toHex(bt, buffer);
        }
    }



    /**
     * Retrieves a hex-encoded representation of the contents of the provided
     * array, along with an ASCII representation of its contents next to it.  The
     * output will be split across multiple lines, with up to sixteen bytes per
     * line.  For each of those sixteen bytes, the two-digit hex representation
     * will be appended followed by a space.  Then, the ASCII representation of
     * those sixteen bytes will follow that, with a space used in place of any
     * byte that does not have an ASCII representation.
     *
     * @param  array   The array whose contents should be processed.
     * @param  indent  The number of spaces to insert on each line prior to the
     *                 first hex byte.
     *
     * @return  A hex-encoded representation of the contents of the provided
     *          array, along with an ASCII representation of its contents next to
     *          it.
     */
    @NonNull()
    public static String toHexPlusASCII(@NonNull final byte[] array,
                                        final int indent)
    {
        final StringBuilder buffer = new StringBuilder();
        toHexPlusASCII(array, indent, buffer);
        return buffer.toString();
    }



    /**
     * Appends a hex-encoded representation of the contents of the provided array
     * to the given buffer, along with an ASCII representation of its contents
     * next to it.  The output will be split across multiple lines, with up to
     * sixteen bytes per line.  For each of those sixteen bytes, the two-digit hex
     * representation will be appended followed by a space.  Then, the ASCII
     * representation of those sixteen bytes will follow that, with a space used
     * in place of any byte that does not have an ASCII representation.
     *
     * @param  array   The array whose contents should be processed.
     * @param  indent  The number of spaces to insert on each line prior to the
     *                 first hex byte.
     * @param  buffer  The buffer to which the encoded data should be appended.
     */
    public static void toHexPlusASCII(@Nullable final byte[] array,
                                      final int indent,
                                      @NonNull final StringBuilder buffer)
    {
        if ((array == null) || (array.length == 0))
        {
            return;
        }

        for (int i=0; i < indent; i++)
        {
            buffer.append(' ');
        }

        int pos = 0;
        int startPos = 0;
        while (pos < array.length)
        {
            toHex(array[pos++], buffer);
            buffer.append(' ');

            if ((pos % 16) == 0)
            {
                buffer.append("  ");
                for (int i=startPos; i < pos; i++)
                {
                    if ((array[i] < ' ') || (array[i] > '~'))
                    {
                        buffer.append(' ');
                    }
                    else
                    {
                        buffer.append((char) array[i]);
                    }
                }
                buffer.append(EOL);
                startPos = pos;

                if (pos < array.length)
                {
                    for (int i=0; i < indent; i++)
                    {
                        buffer.append(' ');
                    }
                }
            }
        }

        // If the last line isn't complete yet, then finish it off.
        if ((array.length % 16) != 0)
        {
            final int missingBytes = (16 - (array.length % 16));
            for (int i=0; i < missingBytes; i++)
            {
                buffer.append("   ");
            }
            buffer.append("  ");
            for (int i=startPos; i < array.length; i++)
            {
                if ((array[i] < ' ') || (array[i] > '~'))
                {
                    buffer.append(' ');
                }
                else
                {
                    buffer.append((char) array[i]);
                }
            }
            buffer.append(EOL);
        }
    }



    /**
     * Retrieves the bytes that correspond to the provided hexadecimal string.
     *
     * @param  hexString  The hexadecimal string for which to retrieve the bytes.
     *                    It must not be {@code null}, and there must not be any
     *                    delimiter between bytes.
     *
     * @return  The bytes that correspond to the provided hexadecimal string.
     *
     * @throws ParseException  If the provided string does not represent valid
     *                          hexadecimal data, or if the provided string does
     *                          not contain an even number of characters.
     */
    @NonNull()
    public static byte[] fromHex(@NonNull final String hexString)
            throws ParseException
    {
        if ((hexString.length() % 2) != 0)
        {
            String err = "Unable to decode the provided hexadecimal string to a byte array because the provided string had a length of {} characters, but it is only possible to process strings with an even number of characters.";
            err = StringTemplates.formatWithPlaceholder(err, hexString.length());
            throw new ParseException(err,hexString.length());
        }

        final byte[] decodedBytes = new byte[hexString.length() / 2];
        for (int i=0, j=0; i < decodedBytes.length; i++, j+= 2)
        {
            switch (hexString.charAt(j))
            {
                case '0':
                    // No action is required.
                    break;
                case '1':
                    decodedBytes[i] = 0x10;
                    break;
                case '2':
                    decodedBytes[i] = 0x20;
                    break;
                case '3':
                    decodedBytes[i] = 0x30;
                    break;
                case '4':
                    decodedBytes[i] = 0x40;
                    break;
                case '5':
                    decodedBytes[i] = 0x50;
                    break;
                case '6':
                    decodedBytes[i] = 0x60;
                    break;
                case '7':
                    decodedBytes[i] = 0x70;
                    break;
                case '8':
                    decodedBytes[i] = (byte) 0x80;
                    break;
                case '9':
                    decodedBytes[i] = (byte) 0x90;
                    break;
                case 'a':
                case 'A':
                    decodedBytes[i] = (byte) 0xA0;
                    break;
                case 'b':
                case 'B':
                    decodedBytes[i] = (byte) 0xB0;
                    break;
                case 'c':
                case 'C':
                    decodedBytes[i] = (byte) 0xC0;
                    break;
                case 'd':
                case 'D':
                    decodedBytes[i] = (byte) 0xD0;
                    break;
                case 'e':
                case 'E':
                    decodedBytes[i] = (byte) 0xE0;
                    break;
                case 'f':
                case 'F':
                    decodedBytes[i] = (byte) 0xF0;
                    break;
                default:
                    String err = "Unable to decode the provided hexadecimal string to a byte array because the provided string had a non-hex character at index {}.";
                    err = StringTemplates.formatWithPlaceholder(err, j);
                    throw new ParseException(err, j);
            }

            switch (hexString.charAt(j+1))
            {
                case '0':
                    // No action is required.
                    break;
                case '1':
                    decodedBytes[i] |= 0x01;
                    break;
                case '2':
                    decodedBytes[i] |= 0x02;
                    break;
                case '3':
                    decodedBytes[i] |= 0x03;
                    break;
                case '4':
                    decodedBytes[i] |= 0x04;
                    break;
                case '5':
                    decodedBytes[i] |= 0x05;
                    break;
                case '6':
                    decodedBytes[i] |= 0x06;
                    break;
                case '7':
                    decodedBytes[i] |= 0x07;
                    break;
                case '8':
                    decodedBytes[i] |= 0x08;
                    break;
                case '9':
                    decodedBytes[i] |= 0x09;
                    break;
                case 'a':
                case 'A':
                    decodedBytes[i] |= 0x0A;
                    break;
                case 'b':
                case 'B':
                    decodedBytes[i] |= 0x0B;
                    break;
                case 'c':
                case 'C':
                    decodedBytes[i] |= 0x0C;
                    break;
                case 'd':
                case 'D':
                    decodedBytes[i] |= 0x0D;
                    break;
                case 'e':
                case 'E':
                    decodedBytes[i] |= 0x0E;
                    break;
                case 'f':
                case 'F':
                    decodedBytes[i] |= 0x0F;
                    break;
                default:
                    String err = "Unable to decode the provided hexadecimal string to a byte array because the provided string had a non-hex character at index {}.";
                    err = StringTemplates.formatWithPlaceholder(err, j+1);
                    throw new ParseException(err,
                            j+1);
            }
        }

        return decodedBytes;
    }



    /**
     * Appends a hex-encoded representation of the provided character to the given
     * buffer.  Each byte of the hex-encoded representation will be prefixed with
     * a backslash.
     *
     * @param  c       The character to be encoded.
     * @param  buffer  The buffer to which the hex-encoded representation should
     *                 be appended.
     */
    public static void hexEncode(final char c,
                                 @NonNull final StringBuilder buffer)
    {
        final byte[] charBytes;
        if (c <= 0x7F)
        {
            charBytes = new byte[] { (byte) (c & 0x7F) };
        }
        else
        {
            charBytes = Utf8s.getBytes(String.valueOf(c));
        }

        for (final byte b : charBytes)
        {
            buffer.append('\\');
            toHex(b, buffer);
        }
    }

    /**
     * Appends a hex-encoded representation of the provided code point to the
     * given buffer.  Each byte of the hex-encoded representation will be prefixed
     * with a backslash.
     *
     * @param  codePoint  The code point to be encoded.
     * @param  buffer     The buffer to which the hex-encoded representation
     *                    should be appended.
     */
    public static void hexEncode(final int codePoint,
                                 @NonNull final StringBuilder buffer)
    {
        final byte[] charBytes = Utf8s.getBytes(new String(new int[]{codePoint}, 0, 1));

        for (final byte b : charBytes)
        {
            buffer.append('\\');
            toHex(b, buffer);
        }
    }

}
