package com.jn.langx.util;

import com.jn.langx.util.io.Charsets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

public class Chars {
    private Chars() {

    }

    /**
     * \r 回车
     */
    public static final int CR = 13; // <US-ASCII CR, carriage return (13)>
    /**
     * \n 换行符
     */
    public static final int LF = 10; // <US-ASCII LF, linefeed (10)>
    /**
     * ' ' 空格
     */
    public static final int SPACE = 32;  // <US-ASCII SP, space (32)>
    /**
     * \t tab 键
     */
    public static final int TAB = 9;  // <US-ASCII HT, horizontal-tab (9)>


    /**
     * Determines if the specified character (Unicode code point) is an alphabet.
     * <p>
     * A character is considered to be alphabetic if its general category type,
     * provided by {@link Character#getType(int) getType(codePoint)}, is any of
     * the following:
     * <ul>
     * <li> <code>UPPERCASE_LETTER</code>
     * <li> <code>LOWERCASE_LETTER</code>
     * <li> <code>TITLECASE_LETTER</code>
     * <li> <code>MODIFIER_LETTER</code>
     * <li> <code>OTHER_LETTER</code>
     * <li> <code>LETTER_NUMBER</code>
     * </ul>
     * or it has contributory property Other_Alphabetic as defined by the
     * Unicode Standard.
     *
     * @param codePoint the character (Unicode code point) to be tested.
     * @return <code>true</code> if the character is a Unicode alphabet
     * character, <code>false</code> otherwise.
     * <p>
     * copy from java 1.7 Character.isAlphabetic
     */
    public static boolean isAlphabetic(int codePoint) {
        return (((((1 << Character.UPPERCASE_LETTER) |
                (1 << Character.LOWERCASE_LETTER) |
                (1 << Character.TITLECASE_LETTER) |
                (1 << Character.MODIFIER_LETTER) |
                (1 << Character.OTHER_LETTER) |
                (1 << Character.LETTER_NUMBER)) >> Character.getType(codePoint)) & 1) != 0) ||
                (Character.isValidCodePoint(codePoint) && Character.isLetter(codePoint));// BUG ?
    }

    public static boolean isAscii(final char ch) {
        return ch < 128;
    }

    public static boolean isAsciiPrintable(final char ch) {
        return ch >= 32 && ch < 127;
    }

    public static boolean isNumber(final char c) {
        return c >= '0' && c <= '9';
    }

    public static int toInt(char c) {
        Preconditions.checkTrue(isNumber(c));
        return c - 48;
    }

    public static char from(int i) {
        return (char) (i + 48);
    }



    public static char toLowerCase(char c) {
        return isUpperCase(c) ? (char) (c + 32) : c;
    }

    public static byte toLowerCase(byte c) {
        return isUpperCase(c) ? (byte) (c + 32) : c;
    }

    public static byte toUpperCase(byte b) {
        return isLowerCase(b) ? (byte) (b - 32) : b;
    }


    public static char toUpperCase(char b) {
        return isLowerCase(b) ? (char) (b - 32) : b;
    }

    public static boolean isLowerCase(byte value) {
        return value >= 'a' && value <= 'z';
    }

    public static boolean isLowerCase(char value) {
        return value >= 'a' && value <= 'z';
    }


    public static boolean isUpperCase(byte value) {
        return value >= 'A' && value <= 'Z';
    }

    public static boolean isUpperCase(char value) {
        return value >= 'A' && value <= 'Z';
    }

    public static boolean isDigit(int ch) {
        return ((ch - '0') | ('9' - ch)) >= 0;
    }

    public static boolean isLowOrUpperCase(char value) {
        return isUpperCase(value) || isLowerCase(value);
    }

    public static boolean isLowOrUpperCase(byte value) {
        return isUpperCase(value) || isLowerCase(value);
    }

    private static final char MAX_CHAR_VALUE = 255;

    public static byte c2b(char c) {
        return (byte) ((c > MAX_CHAR_VALUE) ? '?' : c);
    }

    public static char b2c(byte b) {
        return (char) (b & 0xFF);
    }

    /**
     * <p>Compares two {@code char} values numerically. This is the same functionality as provided in Java 7.</p>
     *
     * @param x the first {@code char} to compare
     * @param y the second {@code char} to compare
     * @return the value {@code 0} if {@code x == y};
     * a value less than {@code 0} if {@code x < y}; and
     * a value greater than {@code 0} if {@code x > y}
     */
    public static int compare(final char x, final char y) {
        return x - y;
    }

    public static boolean isWhitespace(final char ch) {
        return ch == SPACE || ch == TAB || isCRorLF(ch);
    }

    public static boolean isCRorLF(final char ch) {
        return ch == CR || ch == LF;
    }

    public static boolean isNotCRAndLF(final char ch) {
        return ch != CR && ch != LF;
    }

    /**
     * Decodes the provided byte[] to a UTF-8 char[]. This is done while avoiding
     * conversions to String. The provided byte[] is not modified by this method, so
     * the caller needs to take care of clearing the value if it is sensitive.
     */
    public static char[] utf8BytesToChars(byte[] utf8Bytes) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(utf8Bytes);
        final CharBuffer charBuffer = Charsets.UTF_8.decode(byteBuffer);
        final char[] chars;
        if (charBuffer.hasArray()) {
            // there is no guarantee that the char buffers backing array is the right size
            // so we need to make a copy
            chars = Arrays.copyOfRange(charBuffer.array(), charBuffer.position(), charBuffer.limit());
            Arrays.fill(charBuffer.array(), (char) 0); // clear sensitive data
        } else {
            final int length = charBuffer.limit() - charBuffer.position();
            chars = new char[length];
            charBuffer.get(chars);
            // if the buffer is not read only we can reset and fill with 0's
            if (!charBuffer.isReadOnly()) {
                charBuffer.clear(); // reset
                for (int i = 0; i < charBuffer.limit(); i++) {
                    charBuffer.put((char) 0);
                }
            }
        }
        return chars;
    }

    /**
     * Encodes the provided char[] to a UTF-8 byte[]. This is done while avoiding
     * conversions to String. The provided char[] is not modified by this method, so
     * the caller needs to take care of clearing the value if it is sensitive.
     */
    public static byte[] toUtf8Bytes(char[] chars) {
        final CharBuffer charBuffer = CharBuffer.wrap(chars);
        final ByteBuffer byteBuffer = Charsets.UTF_8.encode(charBuffer);
        final byte[] bytes;
        if (byteBuffer.hasArray()) {
            // there is no guarantee that the byte buffers backing array is the right size
            // so we need to make a copy
            bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
            Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        } else {
            final int length = byteBuffer.limit() - byteBuffer.position();
            bytes = new byte[length];
            byteBuffer.get(bytes);
            // if the buffer is not read only we can reset and fill with 0's
            if (!byteBuffer.isReadOnly()) {
                byteBuffer.clear(); // reset
                for (int i = 0; i < byteBuffer.limit(); i++) {
                    byteBuffer.put((byte) 0);
                }
            }
        }
        return bytes;
    }

    public static boolean isBmpCodePoint(int codePoint) {
        return codePoint >>> 16 == 0;
    }

}
