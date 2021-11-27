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

    /**
     * 判断是否为 2的 power 值
     *
     * @param value
     * @return
     */
    public static boolean isPower2(int value) {
        return value >= 1 && ((value & (value - 1)) == 0);
    }

    public static int pow2Int(int pow) {
        return Numbers.toInt(Math.pow(2, pow));
    }

    public static int pow2Long(int pow) {
        return Numbers.toInt(Math.pow(2, pow));
    }

    /**
     * Returns the sum of {@code a} and {@code b} unless it would overflow or underflow in which case
     * {@code Long.MAX_VALUE} or {@code Long.MIN_VALUE} is returned, respectively.
     *
     * @since 2.10.3
     */
    public static long saturatedAdd(long a, long b) {
        long naiveSum = a + b;
        if ((a ^ b) < 0 | (a ^ naiveSum) >= 0) {
            // If a and b have different signs or a has the same sign as the result then there was no
            // overflow, return.
            return naiveSum;
        }
        // we did over/under flow, if the sign is negative we should return MAX otherwise MIN
        return Long.MAX_VALUE + ((naiveSum >>> (Long.SIZE - 1)) ^ 1);
    }

    /**
     * Returns the difference of {@code a} and {@code b} unless it would overflow or underflow in
     * which case {@code Long.MAX_VALUE} or {@code Long.MIN_VALUE} is returned, respectively.
     *
     * @since 2.10.3
     */
    public static long saturatedSubtract(long a, long b) {
        long naiveDifference = a - b;
        if ((a ^ b) >= 0 | (a ^ naiveDifference) >= 0) {
            // If a and b have the same signs or a has the same sign as the result then there was no
            // overflow, return.
            return naiveDifference;
        }
        // we did over/under flow
        return Long.MAX_VALUE + ((naiveDifference >>> (Long.SIZE - 1)) ^ 1);
    }

    /**
     * Returns the product of {@code a} and {@code b} unless it would overflow or underflow in which
     * case {@code Long.MAX_VALUE} or {@code Long.MIN_VALUE} is returned, respectively.
     *
     * @since 2.10.3
     */
    public static long saturatedMultiply(long a, long b) {
        // see checkedMultiply for explanation
        int leadingZeros =
                Long.numberOfLeadingZeros(a)
                        + Long.numberOfLeadingZeros(~a)
                        + Long.numberOfLeadingZeros(b)
                        + Long.numberOfLeadingZeros(~b);
        if (leadingZeros > Long.SIZE + 1) {
            return a * b;
        }
        // the return value if we will overflow (which we calculate by overflowing a long :) )
        long limit = Long.MAX_VALUE + ((a ^ b) >>> (Long.SIZE - 1));
        if (leadingZeros < Long.SIZE | (a < 0 & b == Long.MIN_VALUE)) {
            // overflow
            return limit;
        }
        long result = a * b;
        if (a == 0 || result / a == b) {
            return result;
        }
        return limit;
    }

    static final long FLOOR_SQRT_MAX_LONG = 3037000499L;

    /**
     * Returns the {@code b} to the {@code k}th power, unless it would overflow or underflow in which
     * case {@code Long.MAX_VALUE} or {@code Long.MIN_VALUE} is returned, respectively.
     *
     * @since 2.10.3
     */

    public static long saturatedPow(long b, int k) {
        if (k < 0) {
            throw new IllegalArgumentException(" Argument k must be >= 0");
        }
        if (b >= -2 & b <= 2) {
            switch ((int) b) {
                case 0:
                    return (k == 0) ? 1 : 0;
                case 1:
                    return 1;
                case (-1):
                    return ((k & 1) == 0) ? 1 : -1;
                case 2:
                    if (k >= Long.SIZE - 1) {
                        return Long.MAX_VALUE;
                    }
                    return 1L << k;
                case (-2):
                    if (k >= Long.SIZE) {
                        return Long.MAX_VALUE + (k & 1);
                    }
                    return ((k & 1) == 0) ? (1L << k) : (-1L << k);
                default:
                    throw new AssertionError();
            }
        }
        long accum = 1;
        // if b is negative and k is odd then the limit is MIN otherwise the limit is MAX
        long limit = Long.MAX_VALUE + ((b >>> Long.SIZE - 1) & (k & 1));
        while (true) {
            switch (k) {
                case 0:
                    return accum;
                case 1:
                    return saturatedMultiply(accum, b);
                default:
                    if ((k & 1) != 0) {
                        accum = saturatedMultiply(accum, b);
                    }
                    k >>= 1;
                    if (k > 0) {
                        if (-FLOOR_SQRT_MAX_LONG > b | b > FLOOR_SQRT_MAX_LONG) {
                            return limit;
                        }
                        b *= b;
                    }
            }
        }
    }

    /**
     * @since 4.1.0
     */
    public static final class HashMaths{
        private static final int PRESELECTED_PRIME = 1299827;
        private HashMaths() {
        }

        /**
         * Round the given value up to the next positive power of two.
         *
         * @param value the value (must not be negative and must be less than or equal to {@code 2^31})
         * @return the rounded power of two value
         */
        public static int roundToPowerOfTwo(int value) {
            Preconditions.checkArgument(value>=0,"min value is 0");
            Preconditions.checkArgument(value<=0x40000000,"min value is {}",0x40000000);
            return value <= 1 ? value : Integer.highestOneBit(value - 1) << 1;
        }

        /**
         * A hash function which combines an accumulated hash with a next hash such that {@code f(f(k, p2, b), p1, a) ≠ₙ f(f(k, p1, a), p2, b)}.
         * This function is suitable for object chains whose order affects the overall equality of the hash code.
         * <p>
         * The exact algorithm is not specified and is therefore subject to change and should not be relied upon for hash
         * codes that persist outside of the JVM process.
         *
         * @param accumulatedHash the accumulated hash code of the previous stage
         * @param prime a prime multiplier
         * @param nextHash the hash code of the next single item
         * @return the new accumulated hash code
         */
        public static int multiHashOrdered(int accumulatedHash, int prime, int nextHash) {
            return multiplyWrap(accumulatedHash, prime) + nextHash;
        }

        /**
         * A hash function which combines an accumulated hash with a next hash such that {@code f(f(k, p2, b), p1, a) = f(f(k, p1, a), p2, b)}.
         * This function is suitable for object chains whose order does not affect the overall equality of the hash code.
         * <p>
         * The exact algorithm is not specified and is therefore subject to change and should not be relied upon for hash
         * codes that persist outside of the JVM process.
         *
         * @param accumulatedHash the accumulated hash code of the previous stage
         * @param prime a prime multiplier
         * @param nextHash the hash code of the next single item
         * @return the new accumulated hash code
         */
        public static int multiHashUnordered(int accumulatedHash, int prime, int nextHash) {
            return multiplyWrap(nextHash, prime) + accumulatedHash;
        }

        /**
         * A hash function which combines an accumulated hash with a next hash such that {@code f(f(k, b), a) ≠ₙ f(f(k, a), b)}.
         * This function is suitable for object chains whose order affects the overall equality of the hash code.
         * <p>
         * The exact algorithm is not specified and is therefore subject to change and should not be relied upon for hash
         * codes that persist outside of the JVM process.
         *
         * @param accumulatedHash the accumulated hash code of the previous stage
         * @param nextHash the hash code of the next single item
         * @return the new accumulated hash code
         */
        public static int multiHashOrdered(int accumulatedHash, int nextHash) {
            return multiHashOrdered(accumulatedHash, PRESELECTED_PRIME, nextHash);
        }

        /**
         * A hash function which combines an accumulated hash with a next hash such that {@code f(f(k, b), a) = f(f(k, a), b)}.
         * This function is suitable for object chains whose order does not affect the overall equality of the hash code.
         * <p>
         * The exact algorithm is not specified and is therefore subject to change and should not be relied upon for hash
         * codes that persist outside of the JVM process.
         *
         * @param accumulatedHash the accumulated hash code of the previous stage
         * @param nextHash the hash code of the next single item
         * @return the new accumulated hash code
         */
        public static int multiHashUnordered(int accumulatedHash, int nextHash) {
            return multiHashUnordered(accumulatedHash, PRESELECTED_PRIME, nextHash);
        }

        /**
         * Multiply two unsigned integers together.  If the result overflows a 32-bit number, XOR the overflowed bits back into the result.
         * This operation is commutative, i.e. if we designate the {@code ⨰} symbol to represent this operation, then {@code a ⨰ b = b ⨰ a}.
         * This operation is <em>not</em> associative, i.e. {@code (a ⨰ b) ⨰ c ≠ₙ a ⨰ (b ⨰ c)} (the symbol {@code ≠ₙ} meaning "not necessarily equal to"),
         * therefore this operation is suitable for ordered combinatorial hash functions.
         *
         * @param a the first number to multiply
         * @param b the second number to multiply
         * @return the wrapped multiply result
         */
        public static int multiplyWrap(int a, int b) {
            long r1 = (long) a * b;
            return (int) r1 ^ (int) (r1 >>> 32);
        }
    }

}
