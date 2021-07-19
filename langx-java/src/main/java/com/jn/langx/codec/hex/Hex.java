package com.jn.langx.codec.hex;

import com.jn.langx.codec.CodecException;
import com.jn.langx.util.io.Charsets;

/**
 * 16 进制转换工具。
 * 16进制的字符串，就是人为可视化的 0-F 的字符拼在一起的字符串。
 */
public class Hex {
    /**
     * Used to build output as Hex
     *
     * 这是 16进制的16个字符，
     * 该数组的每一个元素代表了16进制的16个数元，
     * index是 0-15，共计16个，他们代表的是对应数元的10进制数。
     *
     *
     * 其实用 Map表示，更好理解一些，map的key是 0-15，映射后的字符是 0 到 f。
     */
    private static final char[] DECIMAL_TO_DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Used to build output as Hex
     *
     * 把他看做是一个Map：
     * key  value
     * 0    '0'
     * 1    '1'
     * 2    '2'
     * 3    '3'
     * ...
     * 9    '9'
     * 10   'A'
     * 11   'B'
     * 12   'C'
     * 13   'D'
     * 14   'E'
     * 15   'F'
     */
    private static final char[] DECIMAL_TO_DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};



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
     *
     * 转成16进制字符串后，再编码成 UTF-8 格式的bytes
     */
    public static byte[] encodeHexAsBytes(String data) {
        return encodeHexAsBytes(data, true);
    }

    /**
     * @since 3.4.2
     *
     * 转成16进制字符串后，再编码成 UTF-8 格式的bytes
     */
    public static byte[] encodeHexAsBytes(String data, boolean toLowerCase) {
        String hex = encodeHexToString(data, toLowerCase);
        return hex.getBytes(Charsets.UTF_8);
    }


    /**
     * @since 3.4.1
     * 转成16进制字符串后，再编码成 UTF-8 格式的bytes
     */
    public static String encodeHexToString(String data) {
        return encodeHexToString(data, true);
    }


    /**
     * @since 3.4.1
     * 转成16进制字符串后，再编码成 UTF-8 格式的bytes
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
        return encodeHex(data, toLowerCase ? DECIMAL_TO_DIGITS_LOWER : DECIMAL_TO_DIGITS_UPPER);
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     *
     *  首先要明白，
     *      2进制是以数字的方式来表示的，有效数字位：0,1
     *      8进制是以数字的方式来表示的，有效数字位：0,1,2,3,4,5,6,7   。我们把每一个字符称为一个数元。
     *      10进制是以数字的方式来表示的，有效数字位：0,1,2,3,4,5,6,7,8,9   。我们把每一个字符称为一个数元。
     *      16进制是以字符的方式来表示的，有效字符为：0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f。我们把每一个字符称为一个数元。
     *
     *  假若一个 字母 a, 它的 ASCII是 97, 也就是它的 byte表示 就是 97，它的二进制表示：0110 0001
     *  要把它转为16进制，16进制数，范围是0到F代表了0到15。所以就是最大的15，用二进制表示就是1111，
     *  那么也就是说一个16进制的数字，需要占用 4个bit。一个byte是8个bit，所以 一个 byte 就可以用2个16进制数元拼起来。
     *  那么： 因为 97 的二进制是 0110 0001，所以 97 的16进制表示就是：61 。反推一下就是：6*16^1+1*16^0 ==> 6*16 + 1*1= 97
     *
     *
     *  所以可以得到结论是，从二进制转到十六进制，则是将一个字节转成了2个16进制中的数元。上面的61就是两个字符数元。
     *
     * @param data     a byte[] to convert to Hex characters
     * @param toDigits the output alphabet
     * @return A char[] containing hexadecimal characters
     */
    private final static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int length = data.length;
        final char[] out = new char[length << 1];   // 长度 * 2

        // two characters form the hex value.
        for (int i = 0, j = 0; i < length; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];    // 把byte的高4位保持不变，低4位变成0，之后右移 4位(相当于除以16)，得到第一位的10进制数，根据数组，找到16进制的数元，
            out[j++] = toDigits[0x0F & data[i]];            // 把byte的高4位保持变成0，低4位不变，得到第二位的10进制数，根据映射表获取到对应字符
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
