package com.jn.langx.util.collection;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Primitive array wrapper
 */
public class PrimitiveArrays {
    public static final Class BYTE_ARRAY_CLASS = byte[].class;
    public static final Class SHORT_ARRAY_CLASS = short[].class;
    public static final Class INT_ARRAY_CLASS = int[].class;
    public static final Class LONG_ARRAY_CLASS = long[].class;
    public static final Class FLOAT_ARRAY_CLASS = float[].class;
    public static final Class DOUBLE_ARRAY_CLASS = double[].class;
    public static final Class CHAR_ARRAY_CLASS = char[].class;
    public static final Class BOOLEAN_ARRAY_CLASS = boolean[].class;

    private static final List<Class> PRIMITIVE_ARRAY_CLASSES = Collects.asList(
            BYTE_ARRAY_CLASS,
            SHORT_ARRAY_CLASS,
            INT_ARRAY_CLASS,
            LONG_ARRAY_CLASS,
            FLOAT_ARRAY_CLASS,
            DOUBLE_ARRAY_CLASS,
            CHAR_ARRAY_CLASS,
            BOOLEAN_ARRAY_CLASS
    );

    public static boolean isPrimitiveArray(Class clazz) {
        return clazz != null && PRIMITIVE_ARRAY_CLASSES.contains(clazz);
    }

    public static <E> E[] wrap(Object o) {
        Preconditions.checkArgument(Arrs.isArray(o));
        if (isPrimitiveArray(o.getClass())) {
            int length = Arrs.getLength(o);
            Class componentType = o.getClass().getComponentType();
            E[] array = (E[]) Arrs.createArray(componentType, length);
            for (int i = 0; i < length; i++) {
                Array.set(array, i, Array.get(o, i));
            }
            return array;
        }
        return (E[]) o;
    }

    /**
     * convert a boolean[] to a Boolean[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Boolean[] wrap(@Nullable boolean[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Boolean[0];
        }
        Boolean[] ret = new Boolean[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Boolean[] to a boolean[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static boolean[] unwrap(@Nullable Boolean[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new boolean[0];
        }
        boolean[] ret = new boolean[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a char[] to a Character[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Character[] wrap(@Nullable char[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Character[0];
        }
        Character[] ret = new Character[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Character[] to a char[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static char[] unwrap(@Nullable Character[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new char[0];
        }
        char[] ret = new char[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a byte[] to a Byte[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Byte[] wrap(@Nullable byte[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Byte[0];
        }
        Byte[] ret = new Byte[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Byte[] to a byte[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static byte[] unwrap(@Nullable Byte[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new byte[0];
        }
        byte[] ret = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a short[] to a Short[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Short[] wrap(@Nullable short[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Short[0];
        }
        Short[] ret = new Short[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Short[] to a short[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static short[] unwrap(@Nullable Short[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new short[0];
        }
        short[] ret = new short[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a int[] to a Integer[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Integer[] wrap(@Nullable int[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Integer[0];
        }
        Integer[] ret = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Integer[] to a int[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static int[] unwrap(@Nullable Integer[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new int[0];
        }
        int[] ret = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a float[] to a Float[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Float[] wrap(@Nullable float[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Float[0];
        }
        Float[] ret = new Float[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Float[] to a float[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static float[] unwrap(@Nullable Float[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new float[0];
        }
        float[] ret = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a long[] to a Long[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Long[] wrap(@Nullable long[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Long[0];
        }
        Long[] ret = new Long[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Long[] to a long[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static long[] unwrap(@Nullable Long[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new long[0];
        }
        long[] ret = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a double[] to a Double[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Double[] wrap(@Nullable double[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Double[0];
        }
        Double[] ret = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Double[] to a double[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static double[] unwrap(@Nullable Double[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new double[0];
        }
        double[] ret = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of longs are
     * <i>equal</i> to one another.  Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal.  In other words, two arrays
     * are equal if they contain the same elements in the same order.  Also,
     * two array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a  one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(long[] a, long[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(long[] a, Long[] a2) {
        return Arrays.equals(a, unwrap(a2, true));
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of ints are
     * <i>equal</i> to one another.  Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal.  In other words, two arrays
     * are equal if they contain the same elements in the same order.  Also,
     * two array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a  one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(int[] a, int[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(int[] a, Integer[] a2) {
        return Arrays.equals(a, unwrap(a2, true));
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of shorts are
     * <i>equal</i> to one another.  Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal.  In other words, two arrays
     * are equal if they contain the same elements in the same order.  Also,
     * two array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a  one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(short[] a, short a2[]) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(short[] a, Short a2[]) {
        return Arrays.equals(a, unwrap(a2, true));
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of chars are
     * <i>equal</i> to one another.  Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal.  In other words, two arrays
     * are equal if they contain the same elements in the same order.  Also,
     * two array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a  one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(char[] a, char[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(char[] a, Character[] a2) {
        return Arrays.equals(a, unwrap(a2, true));
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of bytes are
     * <i>equal</i> to one another.  Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal.  In other words, two arrays
     * are equal if they contain the same elements in the same order.  Also,
     * two array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a  one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(byte[] a, byte[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(byte[] a, Byte[] a2) {
        return Arrays.equals(a, unwrap(a2, true));
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of booleans are
     * <i>equal</i> to one another.  Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal.  In other words, two arrays
     * are equal if they contain the same elements in the same order.  Also,
     * two array references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a  one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(boolean[] a, boolean[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(boolean[] a, Boolean[] a2) {
        return Arrays.equals(a, unwrap(a2, true));
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of doubles are
     * <i>equal</i> to one another.  Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal.  In other words, two arrays
     * are equal if they contain the same elements in the same order.  Also,
     * two array references are considered equal if both are <tt>null</tt>.<p>
     * <p>
     * Two doubles <tt>d1</tt> and <tt>d2</tt> are considered equal if:
     * <pre>    <tt>new Double(d1).equals(new Double(d2))</tt></pre>
     * (Unlike the <tt>==</tt> operator, this method considers
     * <tt>NaN</tt> equals to itself, and 0.0d unequal to -0.0d.)
     *
     * @param a  one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     * @see Double#equals(Object)
     */
    public static boolean equals(double[] a, double[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(double[] a, Double[] a2) {
        return Arrays.equals(a, unwrap(a2, true));
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of floats are
     * <i>equal</i> to one another.  Two arrays are considered equal if both
     * arrays contain the same number of elements, and all corresponding pairs
     * of elements in the two arrays are equal.  In other words, two arrays
     * are equal if they contain the same elements in the same order.  Also,
     * two array references are considered equal if both are <tt>null</tt>.<p>
     * <p>
     * Two floats <tt>f1</tt> and <tt>f2</tt> are considered equal if:
     * <pre>    <tt>new Float(f1).equals(new Float(f2))</tt></pre>
     * (Unlike the <tt>==</tt> operator, this method considers
     * <tt>NaN</tt> equals to itself, and 0.0f unequal to -0.0f.)
     *
     * @param a  one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     * @see Float#equals(Object)
     */
    public static boolean equals(float[] a, float[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(float[] a, Float[] a2) {
        return Arrays.equals(a, unwrap(a2, true));
    }

    /**
     * Returns <tt>true</tt> if the two specified arrays of Objects are
     * <i>equal</i> to one another.  The two arrays are considered equal if
     * both arrays contain the same number of elements, and all corresponding
     * pairs of elements in the two arrays are equal.  Two objects <tt>e1</tt>
     * and <tt>e2</tt> are considered <i>equal</i> if <tt>(e1==null ? e2==null
     * : e1.equals(e2))</tt>.  In other words, the two arrays are equal if
     * they contain the same elements in the same order.  Also, two array
     * references are considered equal if both are <tt>null</tt>.<p>
     *
     * @param a  one array to be tested for equality
     * @param a2 the other array to be tested for equality
     * @return <tt>true</tt> if the two arrays are equal
     */
    public static boolean equals(Object[] a, Object[] a2) {
        return Arrays.equals(a, a2);
    }
}
