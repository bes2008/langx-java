package com.jn.langx.util;

public class Maths {
    public static int max(int a, int b){
        return a > b ? a : b;
    }

    public static int min(int a, int b){
        return a > b ? b : a;
    }

    /**
     * Determine if the requested {@code index} and {@code length} will fit within {@code capacity}.
     * @param index The starting index.
     * @param length The length which will be utilized (starting from {@code index}).
     * @param capacity The capacity that {@code index + length} is allowed to be within.
     * @return {@code true} if the requested {@code index} and {@code length} will fit within {@code capacity}.
     * {@code false} if this would result in an index out of bounds exception.
     */
    public static boolean isOutOfBounds(int index, int length, int capacity) {
        return (index | length | (index + length) | (capacity - (index + length))) < 0;
    }

}
