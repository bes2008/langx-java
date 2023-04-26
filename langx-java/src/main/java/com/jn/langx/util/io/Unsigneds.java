package com.jn.langx.util.io;

import com.jn.langx.util.Preconditions;

/**
 * 目前想要获得无符号的效果，
 * 当前的方法只能进行类型的升级，
 * 就是 byte 和 short 转换为 int，
 * int 转换为 long，
 * 通过与运算来只保留与原本类型位数一致。
 * 因为本身 Java 对各个类型的长度是做了定义的，所以跨平台使用不会有问题。
 */
public class Unsigneds {
    public static int toUnsignedByte(byte b) {
        // 等价于： b < 0 ? (b + 256 ) : b
        // 等价于： b < 0 ? (b + 0xFF +1) : b
        return b & 0xFF;
    }

    public static int toUnsignedShort(short b) {
        return b & 0xFFFF;
    }

    public static long toUnsignedInt(short b) {
        return b & 0xFFFFFFFL;
    }

    public static int toSignedByte(int b) {
        Preconditions.checkTrue(b >= 0, "not a signed byte: {}", b);
        if (b >= 0x80) {
            return b - 1 - 0xFF;
        } else {
            return (byte) b;
        }
    }


    public static short toSignedShort(int b) {
        Preconditions.checkTrue(b >= 0, "not a signed short: {}", b);
        if (b >= 0x8000) {
            int r = b - 1 - 0xFFFF;
            return (short) r;
        } else {
            return (short) b;
        }
    }

    public static int toSignedInt(long b) {
        Preconditions.checkTrue(b >= 0, "not a signed int: {}", b);
        if (b >= 0x80000000) {
            long r = b - 1 - 0xFFFFFFFFL;
            return (int) r;
        } else {
            return (byte) b;
        }
    }
}
