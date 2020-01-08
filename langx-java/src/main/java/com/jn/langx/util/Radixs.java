package com.jn.langx.util;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

/**
 * 10进制		二进制		8进制		16进制
 * 1		    1		    1		    1
 * 10		    1010		12		    A
 * 15		    1111		17		    F
 * 16		    10000		20		    10
 * 18		    10010		22		    12
 * 100		    1100100		144		    64
 * 100=64+32+4=1100100
 */
public class Radixs {
    public static String toRadix(int b, int radix) {
        if (radix <= 0) {
            radix = 10;
        }
        return Integer.toString(b, radix);
    }

    public static String toBinary(byte b) {
        return Integer.toString(b, 2);
    }

    public static String toBinary(char b) {
        return toRadix(b, 2);
    }

    public static String toBinary(short b) {
        return toRadix(b, 2);
    }

    public static String toBinary(int b) {
        return toRadix(b, 2);
    }

    public static String toBinary(long b) {
        return toRadix(new Long(b).intValue(), 2);
    }

    public static String toOtc(byte b) {
        return Integer.toString(b, 8);
    }

    public static String toOtc(char b) {
        return toRadix(b, 8);
    }

    public static String toOtc(short b) {
        return toRadix(b, 8);
    }

    public static String toOtc(int b) {
        return toRadix(b, 8);
    }

    public static String toOtc(long b) {
        return toRadix(new Long(b).intValue(), 8);
    }

    public static String toDecimal(byte b) {
        return Integer.toString(b, 10);
    }

    public static String toDecimal(char b) {
        return toRadix(b, 10);
    }

    public static String toDecimal(short b) {
        return toRadix(b, 10);
    }

    public static String toDecimal(int b) {
        return toRadix(b, 10);
    }

    public static String toDecimal(long b) {
        return toRadix(new Long(b).intValue(), 10);
    }

    public static String toHex2(byte[] bytes) {
        return toHex(bytes, true);
    }

    public static String toHex(byte[] bytes) {
        return toHex(bytes, false);
    }

    private static String toHex(byte[] bytes, final boolean twoLength) {
        final StringBuilder str = new StringBuilder();
        Collects.forEach(bytes, new Consumer<Byte>() {
            @Override
            public void accept(Byte b) {
                str.append(twoLength ? toHex2(b) : toHex(b));
            }
        });
        return str.toString();
    }

    public static String toHex(byte b) {
        return toRadix(b, 16);
    }

    public static String toHex2(byte b) {
        return Strings.completingLength(toRadix(b, 16), 2, '0', true);
    }

    public static String toHex(char b) {
        return toRadix(b, 16);
    }

    public static String toHex2(char b) {
        return Strings.completingLength(toRadix(b, 16), 2, '0', true);
    }

    public static String toHex(short b) {
        return toRadix(b, 16);
    }

    public static String toHex2(short b) {
        return Strings.completingLength(toRadix(b, 16), 2, '0', true);
    }

    public static String toHex(int b) {
        return toRadix(b, 16);
    }

    public static String toHex2(int b) {
        return Strings.completingLength(toRadix(b, 16), 2, '0', true);
    }

    public static String toHex(long b) {
        return toRadix(new Long(b).intValue(), 16);
    }

    public static String toHex2(long b) {
        return Strings.completingLength(toRadix(new Long(b).intValue(), 16), 2, '0', true);
    }
}
