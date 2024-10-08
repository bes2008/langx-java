package com.jn.langx.codec.h64;


import java.io.IOException;

/**
 * Codec for <a href="http://en.wikipedia.org/wiki/Crypt_(Unix)">Unix Crypt</a>-style encoding.  While similar to
 * Base64, it is not compatible with Base64.
 * <p/>
 * This implementation is based on encoding algorithms found in the Apache Portable Runtime library's
 * <a href="http://svn.apache.org/viewvc/apr/apr/trunk/crypto/apr_md5.c?revision=HEAD&view=markup">apr_md5.c</a>
 * implementation for its {@code crypt}-style support.  The APR team in turn received inspiration for its encoding
 * implementation based on FreeBSD 3.0's {@code /usr/src/lib/libcrypt/crypt.c} implementation.  The
 * accompanying license headers have been retained at the top of this source file.
 * <p/>
 * This file and all that it contains is ASL 2.0 compatible.
 *
 * @since 3.4.1
 */
public class H64 {
    private H64(){

    }
    private static final char[] itoa64 = new char[]{
            '.', '/', '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    private static short toShort(byte b) {
        return (short) (b & 0xff);
    }

    private static int toInt(byte[] bytes, int offset, int numBytes) {
        if (numBytes < 1 || numBytes > 4) {
            throw new IllegalArgumentException("numBytes must be between 1 and 4.");
        }
        int val = toShort(bytes[offset]); //1st byte
        for (int i = 1; i < numBytes; i++) { //any remaining bytes:
            short s = toShort(bytes[offset + i]);
            switch (i) {
                case 1: val |= s << 8; break;
                case 2: val |= s << 16; break;
                case 3: val |= s << 24; break;
                default: break;
            }
        }
        return val;
    }

    /**
     * Appends the specified character into the buffer, rethrowing any encountered
     * {@link IOException} as an {@link IllegalStateException} (since this method is used for internal
     * implementation needs and we only ever use StringBuilders, we should never encounter an IOException).
     *
     * @param buf the buffer to append to
     * @param c   the character to append.
     */
    private static void append(Appendable buf, char c) {
        try {
            buf.append(c);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to append character to internal buffer.", e);
        }
    }

    /**
     * Encodes the specified integer to {@code numChars} H64-compatible characters and appends them into {@code buf}.
     *
     * @param value    the integer to encode to H64-compatible characters
     * @param buf      the output buffer
     * @param numChars the number of characters the value should be converted to.  3, 2 or 1.
     */
    private static void encodeAndAppend(int value, Appendable buf, int numChars) {
        for (int i = 0; i < numChars; i++) {
            append(buf, itoa64[value & 0x3f]);
            value >>= 6;
        }
    }

    /**
     * Encodes the specified bytes to an {@code H64}-encoded String.
     *
     */
    public static String encodeToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null;

        StringBuilder buf = new StringBuilder();

        int length = bytes.length;
        int remainder = length % 3;
        int i = 0; //starting byte
        int last3ByteIndex = length - remainder; //last byte whose index is a multiple of 3

        for(; i < last3ByteIndex; i += 3) {
            int twentyFourBit = toInt(bytes, i, 3);
            encodeAndAppend(twentyFourBit, buf, 4);
        }
        if (remainder > 0) {
            //one or two bytes that we still need to encode:
            int a = toInt(bytes, i, remainder);
            encodeAndAppend(a, buf, remainder + 1);
        }
        return buf.toString();
    }
}
