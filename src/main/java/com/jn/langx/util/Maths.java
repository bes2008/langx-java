package com.jn.langx.util;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.comparator.ComparableComparator;

public class Maths {
    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static float max(float a, float b) {
        return a > b ? a : b;
    }

    public static long max(long a, long b) {
        return a > b ? a : b;
    }

    public static double max(double a, double b) {
        return a > b ? a : b;
    }

    public static int max(int... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Integer>());
    }

    public static float max(float... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Float>());
    }

    public static long max(long... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Long>());
    }

    public static double max(double... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Double>());
    }

    public static int min(int a, int b) {
        return a > b ? b : a;
    }

    public static float min(float a, float b) {
        return a > b ? b : a;
    }

    public static long min(long a, long b) {
        return a > b ? b : a;
    }

    public static double min(double a, double b) {
        return a > b ? b : a;
    }

    public static int min(int... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.min(array, new ComparableComparator<Integer>());
    }

    public static float min(float... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.min(array, new ComparableComparator<Float>());
    }

    public static long min(Long... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.min(array, new ComparableComparator<Long>());
    }

    public static double min(Double... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.min(array, new ComparableComparator<Double>());
    }

    /**
     * Determine if the requested {@code index} and {@code length} will fit within {@code capacity}.
     *
     * @param index    The starting index.
     * @param length   The length which will be utilized (starting from {@code index}).
     * @param capacity The capacity that {@code index + length} is allowed to be within.
     * @return {@code true} if the requested {@code index} and {@code length} will fit within {@code capacity}.
     * {@code false} if this would result in an index out of bounds exception.
     */
    public static boolean isOutOfBounds(int index, int length, int capacity) {
        return (index | length | (index + length) | (capacity - (index + length))) < 0;
    }

    public static int abs(int value) {
        return value & Integer.MAX_VALUE;
    }

    public static int sum(int a, int b) {
        return a + b;
    }

    public static float sum(float a, float b) {
        return a + b;
    }

    public static long sum(long a, long b) {
        return a + b;
    }

    public static double sum(double a, double b) {
        return a + b;
    }
}
