package com.jn.langx.util;

public class Chars {
    public static boolean isAscii(final char ch) {
        return ch < 128;
    }

    public static boolean isAsciiPrintable(final char ch) {
        return ch >= 32 && ch < 127;
    }

    public static boolean isAsciiUpper(final char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    public static boolean isAsciiLower(final char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    public boolean isNumber(final char c) {
        return c >= '0' && c <= '9';
    }

    public int toInt(char c) {
        Preconditions.checkArgument(isNumber(c));
        return c - 48;
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
