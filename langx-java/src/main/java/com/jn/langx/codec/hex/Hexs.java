package com.jn.langx.codec.hex;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

class Hexs {
    /**
     * Indicates whether the provided character is a valid hexadecimal digit.
     *
     * @param c The character for which to make the determination.
     * @return {@code true} if the provided character does represent a valid
     * hexadecimal digit, or {@code false} if not.
     */
    public static boolean isHex(final char c) {
        // 0 -9 的 codepoint 范围 48 -57
        return (c - '0' >= 0 && c - '9' <= 0) || (c - 'a' >= 0 && c - 'f' <= 0) || (c - 'A' >= 0 && c - 'F' <= 0);
    }


    /**
     * Retrieves a hexadecimal representation of the provided byte.
     *
     * @param b The byte to encode as hexadecimal.
     * @return A string containing the hexadecimal representation of the provided
     * byte.
     */
    @NonNull()
    public static String toHex(final byte b) {
        final StringBuilder buffer = new StringBuilder(2);
        toHex(b, buffer);
        return buffer.toString();
    }


    /**
     * Appends a hexadecimal representation of the provided byte to the given
     * buffer.
     *
     * @param b      The byte to encode as hexadecimal.
     * @param buffer The buffer to which the hexadecimal representation is to be
     *               appended.
     */
    public static void toHex(final byte b, @NonNull final StringBuilder buffer) {
        switch (b & 0xF0) {
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

        switch (b & 0x0F) {
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
     * Retrieves a hexadecimal representation of the contents of the provided byte
     * array.  No delimiter character will be inserted between the hexadecimal
     * digits for each byte.
     *
     * @param bytes  The byte array to be represented as a hexadecimal string.
     *               It must not be {@code null}.
     * @param buffer A buffer to which the hexadecimal representation of the
     *               contents of the provided byte array should be appended.
     */
    public static void toHex(@NonNull final byte[] bytes,
                             @NonNull final StringBuilder buffer) {
        toHex(bytes, null, buffer);
    }


    /**
     * Retrieves a hexadecimal representation of the contents of the provided byte
     * array.  No delimiter character will be inserted between the hexadecimal
     * digits for each byte.
     *
     * @param bytes     The byte array to be represented as a hexadecimal
     *                  string.  It must not be {@code null}.
     * @param delimiter A delimiter to be inserted between bytes.  It may be
     *                  {@code null} if no delimiter should be used.
     * @param buffer    A buffer to which the hexadecimal representation of the
     *                  contents of the provided byte array should be appended.
     */
    public static void toHex(@NonNull final byte[] bytes,
                             @Nullable final String delimiter,
                             @NonNull final StringBuilder buffer) {
        boolean first = true;
        for (final byte bt : bytes) {
            if (first) {
                first = false;
            } else if (delimiter != null) {
                buffer.append(delimiter);
            }

            toHex(bt, buffer);
        }
    }

    protected Hexs(){

    }

}
