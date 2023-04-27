package com.jn.langx.util;


/**
 * 目前想要获得无符号的效果，
 * 当前的方法只能进行类型的升级，
 * 就是 byte 和 short 转换为 int，
 * int 转换为 long，
 * 通过与运算来只保留与原本类型位数一致。
 * 因为本身 Java 对各个类型的长度是做了定义的，所以跨平台使用不会有问题。
 */
public class Unsigneds {
    private Unsigneds(){

    }
    public static int toUnsignedByte(byte b) {
        // 等价于： b < 0 ? (b + 256 ) : b
        // 等价于： b < 0 ? (b + 0xFF +1) : b
        return b & 0xFF;
    }

    public static int toUnsignedShort(short b) {
        return b & 0xFFFF;
    }

    public static int toUnsignedChar(char b) {
        return b & 0xFFFF;
    }

    public static long toUnsignedInt(int b) {
        return b & 0xFFFFFFFFL;
    }

    public static byte toSignedByte(int b) {
        Preconditions.checkTrue(b >= 0, "not a signed byte: {}", b);
        int r = b;
        if (b >= 0x80) {
            r = b - 1 - 0xFF;
        }
        return (byte) r;
    }


    public static short toSignedShort(int b) {
        Preconditions.checkTrue(b >= 0, "not a signed short: {}", b);
        int r = b;
        if (b >= 0x8000) {
            r = b - 1 - 0xFFFF;
        }
        return (short) r;
    }

    public static char toSignedChar(int b) {
        Preconditions.checkTrue(b >= 0, "not a signed short: {}", b);
        int r = b;
        if (b >= 0x8000) {
            r = b - 1 - 0xFFFF;
        }
        return (char) r;
    }

    public static int toSignedInt(long b) {
        Preconditions.checkTrue(b >= 0, "not a signed int: {}", b);
        long r = b;
        if (b >= 0x80000000) {
            r = b - 1 - 0xFFFFFFFFL;

        }
        return (int) r;
    }
}
