package com.jn.langx.util;

public class Ints {
    public static final int BYTES = 4;
    public static final int SIZE = Integer.SIZE;

    public static int numberOfLeadingZeros(int i) {
        return Integer.numberOfLeadingZeros(i);
    }

    public static int numberOfTrailingZeros(int i) {
        return Integer.numberOfTrailingZeros(i);
    }

    public static int reverse(int i) {
        return Integer.reverse(i);
    }

    public static int reverseBytes(int i) {
        return Integer.reverseBytes(i);
    }

    public static int rotateLeft(int i, int distance) {
        return Integer.rotateLeft(i, distance);
    }

    public static int rotateRight(int i, int distance) {
        return Integer.rotateRight(i, distance);
    }

    public static Integer valueOf(int value) {
        return Integer.valueOf(value);
    }

}
