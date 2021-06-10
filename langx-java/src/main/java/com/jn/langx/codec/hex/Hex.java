package com.jn.langx.codec.hex;

import com.jn.langx.codec.CodecException;
import com.jn.langx.util.io.Charsets;

public class Hex {
    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};



    public static byte[] decodeHex(String data) {
        return decodeHex(data.toCharArray());
    }

    /**
     * Converts an array of characters representing hexadecimal values into an array of bytes of those same values. The
     * returned array will be half the length of the passed array, as it takes two characters to represent any given
     * byte. An exception is thrown if the passed char array has an odd number of elements.
     *
     * @param data An array of characters containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied char array.
     * @throws CodecException Thrown if an odd number or illegal of characters is supplied
     */
    public static byte[] decodeHex(final char[] data) throws CodecException {

        final int len = data.length;

        if ((len & 0x01) != 0) {
            throw new CodecException("Odd number of characters.");
        }

        final byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * @since 3.4.2
     */
    public static byte[] encodeHexAsBytes(String data) {
        return encodeHexAsBytes(data, true);
    }

    /**
     * @since 3.4.2
     */
    public static byte[] encodeHexAsBytes(String data, boolean toLowerCase) {
        String hex = encodeHexToString(data, toLowerCase);
        return hex.getBytes(Charsets.UTF_8);
    }


    /**
     * @since 3.4.1
     */
    public static String encodeHexToString(String data) {
        return encodeHexToString(data, true);
    }


    /**
     * @since 3.4.1
     */
    public static String encodeHexToString(String data, boolean toLowerCase) {
        return new String(encodeHex(data, toLowerCase));
    }

    /**
     * @since 3.4.1
     */
    public static char[] encodeHex(String data) {
        return encodeHex(data, true);
    }

    /**
     * @since 3.4.1
     */
    public static char[] encodeHex(String data, boolean toLowerCase) {
        return encodeHex(data.getBytes(Charsets.UTF_8), toLowerCase);
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data a byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     */
    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data        a byte[] to convert to Hex characters
     * @param toLowerCase <code>true</code> converts to lowercase, <code>false</code> to uppercase
     * @return A char[] containing hexadecimal characters
     */
    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data     a byte[] to convert to Hex characters
     * @param toDigits the output alphabet
     * @return A char[] containing hexadecimal characters
     */
    protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * Converts an array of bytes into a String representing the hexadecimal values of each byte in order. The returned
     * String will be double the length of the passed array, as it takes two characters to represent any given byte.
     *
     * @param data a byte[] to convert to Hex characters
     * @return A String containing hexadecimal characters
     * @since 1.4
     */
    public static String encodeHexString(final byte[] data) {
        return encodeHexString(data, true);
    }

    /**
     * @param data
     * @param lowerCase
     * @return
     * @since 2.10.3
     */
    public static String encodeHexString(final byte[] data, boolean lowerCase) {
        return new String(encodeHex(data, lowerCase));
    }

    /**
     * Converts a hexadecimal character to an integer.
     *
     * @param ch    A character to convert to an integer digit
     * @param index The index of the character in the source
     * @return An integer
     * @throws CodecException Thrown if ch is an illegal hex character
     */
    protected static int toDigit(final char ch, final int index) throws CodecException {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new CodecException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }
}
