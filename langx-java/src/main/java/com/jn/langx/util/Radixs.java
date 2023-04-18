package com.jn.langx.util;

import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

import java.math.BigInteger;

/**
 * 进制转换工具
 * <pre>
 * 10进制		二进制		8进制		16进制
 * 1		    1		    1		    1
 * 10		    1010		12		    A
 * 15		    1111		17		    F
 * 16		    10000		20		    10
 * 18		    10010		22		    12
 * 100		    1100100		144		    64
 * 100=64+32+4=1100100
 * </pre>
 *
 * binary: 二进制
 * otc: 八进制
 * decimal: 十进制
 * hex：十六进制
 */
public class Radixs {
    private Radixs(){

    }
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
        return toRadix(Numbers.toInt(b), 2);
    }

    public static boolean isOctal(int b) {
        return b >= 0 && b <= 7;
    }

    private static Regexp OCTAL_STRING_PATTERN = Regexps.createRegexp("^[0-7]+$");

    public static boolean isOctal(String str) {
        if (Emptys.isEmpty(str)) {
            return false;
        }
        return OCTAL_STRING_PATTERN.matcher(str).matches();
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
        return toRadix(Numbers.toInt(b), 8);
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
        return toRadix(Numbers.toInt(b), 10);
    }

    public static String toHex2(byte[] bytes) {
        return toHex(bytes, true);
    }

    public static String toHex(byte[] bytes) {
        return toHex(bytes, false);
    }

    private static String toHex(byte[] bytes, final boolean twoLength) {
        final StringBuilder str = new StringBuilder();
        for (byte b : bytes) {
            str.append(twoLength ? toHex2(b) : toHex(b));
        }
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
        return toRadix(Numbers.toInt(b), 16);
    }

    public static String toHex2(long b) {
        return Strings.completingLength(toRadix(Numbers.toInt(b), 16), 2, '0', true);
    }

    public static int binaryToDecimal(String binary) {
        return binaryToDecimal2(binary).intValue();
    }

    public static String binaryToOctal(String binary) {
        return toOtc(binaryToDecimal(binary));
    }

    public static BigInteger binaryToDecimal2(String binary) {
        return new BigInteger(binary, 2);
    }
}
