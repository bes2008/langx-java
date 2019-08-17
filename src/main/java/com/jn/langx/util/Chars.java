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

    public boolean isNumber(final char c){
        return c>='0' && c<='9';
    }

    public int toInt(char c){
        Preconditions.checkArgument(isNumber(c));
        return c - 48;
    }
}
