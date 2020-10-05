package com.jn.langx.util;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.function.Consumer;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Maths {

    /**
     * 求最大值
     */
    public static int max(int... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Integer>());
    }

    /**
     * 求最大值
     */
    public static float maxFloat(float... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Float>());
    }

    /**
     * 求最大值
     */
    public static long maxLong(long... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Long>());
    }

    /**
     * 求最大值
     */
    public static double maxDouble(double... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.max(array, new ComparableComparator<Double>());
    }

    /**
     * 求最小值
     */
    public static int min(int... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.<Integer>min(array, new ComparableComparator<Integer>());
    }

    /**
     * 求最小值
     */
    public static float minFloat(float... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.<Float>min(array, new ComparableComparator<Float>());
    }

    /**
     * 求最小值
     */
    public static long minLong(long... array) {
        Preconditions.checkTrue(array.length > 0);
        return Collects.<Long>min(array, new ComparableComparator<Long>());
    }

    /**
     * 求最小值
     */
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

    /**
     * 求绝对值
     */
    public static int abs(int value) {
        return value & Integer.MAX_VALUE;
    }

    /**
     * 求平均值
     */
    public static int avg(int... values) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length >= 1);
        return sum(values) / values.length;
    }

    /**
     * 求平均值
     */
    public static float avgFloat(float... values) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length >= 1);
        return sumFloat(values) / values.length;
    }

    /**
     * 求平均值
     */
    public static long avgLong(long... values) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length >= 1);
        return sumLong(values) / values.length;
    }

    /**
     * 求平均值
     */
    public static double avgDouble(double... values) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length >= 1);
        return sumDouble(values) / values.length;
    }

    /**
     * 求总和
     */
    public static int sum(int... values) {
        return Pipeline.of(values).sum().intValue();
    }

    /**
     * 求总和
     */
    public static float sumFloat(float... values) {
        return Pipeline.of(values).sum().floatValue();
    }

    /**
     * 求总和
     */
    public static long sumLong(long... values) {
        return Pipeline.of(values).sum().longValue();
    }

    /**
     * 求总和
     */
    public static double sumDouble(double... values) {
        return Pipeline.of(values).sum();
    }

    /**
     * 格式化小数点后多少位
     *
     * @param value     要格式化的值
     * @param precision 小数点后保留位数
     * @return 格式化后的值
     */
    public static Double formatPrecision(double value, int precision) {
        return formatPrecision(value, precision, RoundingMode.HALF_UP);
    }

    /**
     * 格式化小数点后多少位
     *
     * @param value     要格式化的值
     * @param precision 小数点后保留位数
     * @return 格式化后的值
     */
    public static Double formatPrecision(double value, int precision, RoundingMode roundingMode) {
        Preconditions.checkArgument(precision >= 0);
        final StringBuilder pattern = precision > 0 ? new StringBuilder("#.") : new StringBuilder("#");
        Collects.forEach(Arrs.range(precision), new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                pattern.append("0");
            }
        });

        NumberFormat nf = new DecimalFormat(pattern.toString());
        nf.setMaximumFractionDigits(precision);
        nf.setRoundingMode(roundingMode == null ? RoundingMode.HALF_UP : roundingMode);
        String string = nf.format(value);
        return Double.parseDouble(string);
    }

    public static int pow2Int(int pow) {
        return Numbers.toInt(Math.pow(2, pow));
    }

    public static int pow2Long(int pow) {
        return Numbers.toInt(Math.pow(2, pow));
    }
}
