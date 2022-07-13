package com.jn.langx.util.io.unicode;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.text.placeholder.PlaceholderParser;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;


public class Utf8s {

    /**
     * Indicates whether the contents of the provided array are valid UTF-8.
     *
     * @param b The byte array to examine.  It must not be {@code null}.
     * @return {@code true} if the byte array can be parsed as a valid UTF-8
     * string, or {@code false} if not.
     */
    public static boolean isValidUTF8(@NonNull final byte[] b) {
        int i = 0;
        while (i < b.length) {
            final byte currentByte = b[i++];

            // If the most significant bit is not set, then this represents a valid
            // single-byte character.
            if ((currentByte & 0x80) == 0x00) {
                continue;
            }

            // If the first byte starts with 0b110, then it must be followed by
            // another byte that starts with 0b10.
            if ((currentByte & 0xE0) == 0xC0) {
                if (!hasExpectedSubsequentUTF8Bytes(b, i, 1)) {
                    return false;
                }

                i++;
                continue;
            }

            // If the first byte starts with 0b1110, then it must be followed by two
            // more bytes that start with 0b10.
            if ((currentByte & 0xF0) == 0xE0) {
                if (!hasExpectedSubsequentUTF8Bytes(b, i, 2)) {
                    return false;
                }

                i += 2;
                continue;
            }

            // If the first byte starts with 0b11110, then it must be followed by
            // three more bytes that start with 0b10.
            if ((currentByte & 0xF8) == 0xF0) {
                if (!hasExpectedSubsequentUTF8Bytes(b, i, 3)) {
                    return false;
                }

                i += 3;
                continue;
            }

            // If the first byte starts with 0b111110, then it must be followed by
            // four more bytes that start with 0b10.
            if ((currentByte & 0xFC) == 0xF8) {
                if (!hasExpectedSubsequentUTF8Bytes(b, i, 4)) {
                    return false;
                }

                i += 4;
                continue;
            }

            // If the first byte starts with 0b1111110, then it must be followed by
            // five more bytes that start with 0b10.
            if ((currentByte & 0xFE) == 0xFC) {
                if (!hasExpectedSubsequentUTF8Bytes(b, i, 5)) {
                    return false;
                }

                i += 5;
                continue;
            }

            // This is not a valid first byte for a UTF-8 character.
            return false;
        }


        // If we've gotten here, then the provided array represents a valid UTF-8
        // string.
        return true;
    }


    /**
     * Ensures that the provided array has the expected number of bytes that start
     * with 0b10 starting at the specified position in the array.
     *
     * @param b The byte array to examine.
     * @param p The position in the byte array at which to start looking.
     * @param n The number of bytes to examine.
     * @return {@code true} if the provided byte array has the expected number of
     * bytes that start with 0b10, or {@code false} if not.
     */
    private static boolean hasExpectedSubsequentUTF8Bytes(@NonNull final byte[] b, final int p, final int n) {
        if (b.length < (p + n)) {
            return false;
        }

        for (int i = 0; i < n; i++) {
            if ((b[p + i] & 0xC0) != 0x80) {
                return false;
            }
        }

        return true;
    }


    /**
     * Retrieves a UTF-8 byte representation of the provided string.
     *
     * @param s The string for which to retrieve the UTF-8 byte representation.
     * @return The UTF-8 byte representation for the provided string.
     */
    @NonNull
    public static byte[] getBytes(@Nullable final String s) {
        final int length;
        if ((s == null) || ((length = s.length()) == 0)) {
            return Emptys.EMPTY_BYTES;
        }

        final byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            final char c = s.charAt(i);
            if (c <= 0x7F) {
                b[i] = (byte) (c & 0x7F);
            } else {
                return s.getBytes(Charsets.UTF_8);
            }
        }

        return b;
    }


    /**
     * Retrieves a string generated from the provided byte array using the UTF-8
     * encoding.
     *
     * @param b The byte array for which to return the associated string.
     * @return The string generated from the provided byte array using the UTF-8
     * encoding.
     */
    @NonNull
    public static String toString(@NonNull final byte[] b) {
        return new String(b, Charsets.UTF_8);
    }


    /**
     * Retrieves a string generated from the specified portion of the provided
     * byte array using the UTF-8 encoding.
     *
     * @param b      The byte array for which to return the associated string.
     * @param offset The offset in the array at which the value begins.
     * @param length The number of bytes in the value to convert to a string.
     * @return The string generated from the specified portion of the provided
     * byte array using the UTF-8 encoding.
     */
    @NonNull
    public static String toString(@NonNull final byte[] b, final int offset, final int length) {
        return new String(b, offset, length, Charsets.UTF_8);
    }

    /**
     * e.g. : \x5C
     * e.g. : \uF23F
     * e.g. ：\x{we}
     */
    public final static Regexp ESCAPED_CHAR_REGEXP = Regexps.createRegexp("(?:(?:\\\\0)[0-3][0-7]{2})|(?:(?:\\\\0)[0-7]{1,2})|(?:(?:\\\\x)[0-9a-fA-F]{2})|(?:(?:\\\\u)[0-9a-fA-F]{4})");

    /**
     * 0x5C
     * \x5C
     * <p>
     * ASCII 码表：
     * https://www.habaijian.com/
     */
    public static char hexToChar(String hexChar) {
        return hexToChar(hexChar, true);
    }

    public static char hexToChar(String hex, boolean valid) {
        if (valid) {
            Preconditions.checkTrue(Regexps.match(ESCAPED_CHAR_REGEXP, hex), "illegal hex: {}", hex);
        }
        if (hex.startsWith("\\0")) {
            // unicode
            hex = hex.substring(2);
            int charCodePoint = Integer.parseInt(hex, 8);
            char c = (char) charCodePoint;
            return c;
        } else if (hex.startsWith("\\x") || hex.startsWith("\\u")) {
            hex = hex.substring(2);
            int charCodePoint = Integer.parseInt(hex, 16);
            char c = (char) charCodePoint;
            return c;
        }
        throw new UnsupportedOperationException();
    }

    public static String decodeChars(String text) {
        return StringTemplates.format(text, ESCAPED_CHAR_REGEXP, new PlaceholderParser() {
            @Override
            public String parse(String variable) {
                return "" + hexToChar(variable, false);
            }
        });
    }

}
