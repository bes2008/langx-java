package com.jn.langx.util;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.comparator.ComparableComparator;

import java.math.RoundingMode;
import java.text.NumberFormat;

public class Maths {

    public static int max(int... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Integer>());
    }

    public static float maxFloat(float... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Float>());
    }

    public static long maxLong(long... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Long>());
    }

    public static double maxDouble(double... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Double>());
    }

    public static int min(int... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.<Integer>min(array, new ComparableComparator<Integer>());
    }

    public static float minFloat(float... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.<Float>min(array, new ComparableComparator<Float>());
    }

    public static long minLong(long... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.<Long>min(array, new ComparableComparator<Long>());
    }

    public static double minDouble(double... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.<Double>min(array, new ComparableComparator<Double>());
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

    public static int avg(int... values) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length >= 1);
        return sum(values) / values.length;
    }

    public static float avgFloat(float... values) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length >= 1);
        return sumFloat(values) / values.length;
    }

    public static long avgLong(long... values) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length >= 1);
        return sumLong(values) / values.length;
    }

    public static double avgDouble(double... values) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length >= 1);
        return sumDouble(values) / values.length;
    }

    public static int sum(int... values) {
        return Pipeline.of(values).sum().intValue();
    }

    public static float sumFloat(float... values) {
        return Pipeline.of(values).sum().floatValue();
    }

    public static long sumLong(long... values) {
        return Pipeline.of(values).sum().longValue();
    }


    public static double sumDouble(double... values) {
        return Pipeline.of(values).sum();
    }


    public static Double formatPrecision(double value, int precision) {
        return formatPrecision(value, precision, RoundingMode.HALF_UP);
    }

    public static Double formatPrecision(double value, int precision, RoundingMode roundingMode) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(precision);
        nf.setRoundingMode(roundingMode == null ? RoundingMode.HALF_UP : roundingMode);
        String string = nf.format(value);
        return Double.parseDouble(string);
    }
}
