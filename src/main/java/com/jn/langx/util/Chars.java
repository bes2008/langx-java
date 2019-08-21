package com.jn.langx.util;

public class Chars {
    public static boolean isAscii(final char ch) {
        return ch < 128;
    }

    public static boolean isAsciiPrintable(final char ch) {
        return ch >= 32 && ch < 127;
    }

    public boolean isNumber(final char c) {
        return c >= '0' && c <= '9';
    }

    public int toInt(char c) {
        Preconditions.checkArgument(isNumber(c));
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
