package com.jn.langx.util;

public class Chars {
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
     *
     * copy from java 1.7 Character.isAlphabetic
     */
    public static boolean isAlphabetic(int codePoint) {
        return (((((1 << Character.UPPERCASE_LETTER) |
                (1 << Character.LOWERCASE_LETTER) |
                (1 << Character.TITLECASE_LETTER) |
                (1 << Character.MODIFIER_LETTER) |
                (1 << Character.OTHER_LETTER) |
                (1 << Character.LETTER_NUMBER)) >> Character.getType(codePoint)) & 1) != 0) ||
                Character.isValidCodePoint(codePoint);// BUG ?
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

    public static boolean isLowOrUpperCase(char value){
        return isUpperCase(value) || isLowerCase(value);
    }

    public static boolean isLowOrUpperCase(byte value){
        return isUpperCase(value) || isLowerCase(value);
    }

    private static final char MAX_CHAR_VALUE = 255;

    public static byte c2b(char c) {
        return (byte) ((c > MAX_CHAR_VALUE) ? '?' : c);
    }

    private static byte c2b0(char c) {
        return (byte) c;
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
}
