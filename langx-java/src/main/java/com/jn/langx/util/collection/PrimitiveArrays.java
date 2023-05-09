package com.jn.langx.util.collection;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.sort.DualPivotQuicksort;
import com.jn.langx.util.function.Supplier;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Primitive array wrapper
 */
@SuppressWarnings({"unused", "rawtypes"})
public class PrimitiveArrays {
    public static final Class BYTE_ARRAY_CLASS = byte[].class;
    public static final Class SHORT_ARRAY_CLASS = short[].class;
    public static final Class INT_ARRAY_CLASS = int[].class;
    public static final Class LONG_ARRAY_CLASS = long[].class;
    public static final Class FLOAT_ARRAY_CLASS = float[].class;
    public static final Class DOUBLE_ARRAY_CLASS = double[].class;
    public static final Class CHAR_ARRAY_CLASS = char[].class;
    public static final Class BOOLEAN_ARRAY_CLASS = boolean[].class;

    /**
     * The index value when an element is not found in a list or array: {@code -1}.
     * This value is returned by methods in this class and can also be used in comparisons with values returned by
     * various method from {@link java.util.List}.
     */
    public static final int INDEX_NOT_FOUND = -1;
    protected static final List<Class> PRIMITIVE_ARRAY_CLASSES = Collects.asList(Collects.asList(
            BYTE_ARRAY_CLASS,
            SHORT_ARRAY_CLASS,
            INT_ARRAY_CLASS,
            LONG_ARRAY_CLASS,
            FLOAT_ARRAY_CLASS,
            DOUBLE_ARRAY_CLASS,
            CHAR_ARRAY_CLASS,
            BOOLEAN_ARRAY_CLASS
    ), false);

    protected PrimitiveArrays() {
    }

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
    public static Boolean[] wrap(@Nullable final boolean[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Boolean[0];
        }
        return Arrs.createArray(Boolean.class, values.length, new Supplier<Integer, Boolean>() {
            @Override
            public Boolean get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a Boolean[] to a boolean[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static boolean[] unwrap(@Nullable final Boolean[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new boolean[0];
        }
        return createBooleanArray(values.length, new Supplier<Integer, Boolean>() {
            @Override
            public Boolean get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a char[] to a Character[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Character[] wrap(@Nullable final char[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Character[0];
        }
        return Arrs.createArray(Character.class, values.length, new Supplier<Integer, Character>() {
            @Override
            public Character get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a Character[] to a char[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static char[] unwrap(@Nullable final Character[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new char[0];
        }
        return createCharArray(values.length, new Supplier<Integer, Character>() {
            @Override
            public Character get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a byte[] to a Byte[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Byte[] wrap(@Nullable final byte[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Byte[0];
        }
        return Arrs.createArray(Byte.class, values.length, new Supplier<Integer, Byte>() {
            @Override
            public Byte get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a Byte[] to a byte[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static byte[] unwrap(@Nullable final Byte[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new byte[0];
        }
        return createByteArray(values.length, new Supplier<Integer, Byte>() {
            @Override
            public Byte get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a short[] to a Short[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Short[] wrap(@Nullable final short[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Short[0];
        }
        return Arrs.createArray(Short.class, values.length, new Supplier<Integer, Short>() {
            @Override
            public Short get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a Short[] to a short[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static short[] unwrap(@Nullable final Short[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new short[0];
        }
        return createShortArray(values.length, new Supplier<Integer, Short>() {
            @Override
            public Short get(Integer i) {
                return values[i];
            }
        });
    }

    /**
     * convert a int[] to a Integer[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Integer[] wrap(@Nullable final int[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Integer[0];
        }
        return Arrs.createArray(Integer.class, values.length, new Supplier<Integer, Integer>() {
            @Override
            public Integer get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a Integer[] to a int[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static int[] unwrap(@Nullable final Integer[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new int[0];
        }
        return createIntArray(values.length, new Supplier<Integer, Integer>() {
            @Override
            public Integer get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a float[] to a Float[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Float[] wrap(@Nullable final float[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Float[0];
        }

        return Arrs.createArray(Float.class, values.length, new Supplier<Integer, Float>() {
            @Override
            public Float get(Integer index) {
                return values[index];
            }
        });

    }

    /**
     * convert a Float[] to a float[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static float[] unwrap(@Nullable final Float[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new float[0];
        }
        return createFloatArray(values.length, new Supplier<Integer, Float>() {
            @Override
            public Float get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a long[] to a Long[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Long[] wrap(@Nullable final long[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Long[0];
        }
        return Arrs.createArray(Long.class, values.length, new Supplier<Integer, Long>() {
            @Override
            public Long get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a Long[] to a long[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static long[] unwrap(@Nullable final Long[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new long[0];
        }
        return createLongArray(values.length, new Supplier<Integer, Long>() {
            @Override
            public Long get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a double[] to a Double[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Double[] wrap(@Nullable final double[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Double[0];
        }
        return Arrs.createArray(Double.class, values.length, new Supplier<Integer, Double>() {
            @Override
            public Double get(Integer index) {
                return values[index];
            }
        });
    }

    /**
     * convert a Double[] to a double[]
     *
     * @param values         source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static double[] unwrap(@Nullable final Double[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new double[0];
        }
        return createDoubleArray(values.length, new Supplier<Integer, Double>() {
            @Override
            public Double get(Integer index) {
                return values[index];
            }
        });
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
    public static boolean equals(short[] a, short[] a2) {
        return Arrays.equals(a, a2);
    }

    public static boolean equals(short[] a, Short[] a2) {
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

    public static byte[] createByteArray(int length, final byte initValue) {
        return createByteArray(length, new Supplier<Integer, Byte>() {
            @Override
            public Byte get(Integer index) {
                return initValue;
            }
        });
    }

    public static byte[] createByteArray(int length, Supplier<Integer, Byte> initValueSupplier) {
        byte[] array = new byte[length];
        if (initValueSupplier != null) {
            for (int i = 0; i < length; i++) {
                array[i] = initValueSupplier.get(i);
            }
        }
        return array;
    }

    public static short[] createShortArray(int length, final short initValue) {
        return createShortArray(length, new Supplier<Integer, Short>() {
            @Override
            public Short get(Integer index) {
                return initValue;
            }
        });
    }

    public static short[] createShortArray(int length, Supplier<Integer, Short> initValueSupplier) {
        short[] array = new short[length];
        if (initValueSupplier != null) {
            for (int i = 0; i < length; i++) {
                array[i] = initValueSupplier.get(i);
            }
        }
        return array;
    }

    public static char[] createCharArray(int length, final char initValue) {
        return createCharArray(length, new Supplier<Integer, Character>() {
            @Override
            public Character get(Integer index) {
                return initValue;
            }
        });
    }

    public static char[] createCharArray(int length, Supplier<Integer, Character> initValueSupplier) {
        char[] array = new char[length];
        if (initValueSupplier != null) {
            for (int i = 0; i < length; i++) {
                array[i] = initValueSupplier.get(i);
            }
        }
        return array;
    }

    public static int[] createIntArray(int length, final int initValue) {
        return createIntArray(length, new Supplier<Integer, Integer>() {
            @Override
            public Integer get(Integer index) {
                return initValue;
            }
        });
    }

    public static int[] createIntArray(int length, Supplier<Integer, Integer> initValueSupplier) {
        int[] array = new int[length];
        if (initValueSupplier != null) {
            for (int i = 0; i < length; i++) {
                array[i] = initValueSupplier.get(i);
            }
        }
        return array;
    }

    public static long[] createLongArray(int length, final long initValue) {
        return createLongArray(length, new Supplier<Integer, Long>() {
            @Override
            public Long get(Integer index) {
                return initValue;
            }
        });
    }

    public static long[] createLongArray(int length, Supplier<Integer, Long> initValueSupplier) {
        long[] array = new long[length];
        if (initValueSupplier != null) {
            for (int i = 0; i < length; i++) {
                array[i] = initValueSupplier.get(i);
            }
        }
        return array;
    }

    public static float[] createFloatArray(int length, final float initValue) {
        return createFloatArray(length, new Supplier<Integer, Float>() {
            @Override
            public Float get(Integer index) {
                return initValue;
            }
        });
    }

    public static float[] createFloatArray(int length, Supplier<Integer, Float> initValueSupplier) {
        float[] array = new float[length];
        if (initValueSupplier != null) {
            for (int i = 0; i < length; i++) {
                array[i] = initValueSupplier.get(i);
            }
        }
        return array;
    }

    public static double[] createDoubleArray(int length, final Double initValue) {
        return createDoubleArray(length, new Supplier<Integer, Double>() {
            @Override
            public Double get(Integer index) {
                return initValue;
            }
        });
    }

    public static double[] createDoubleArray(int length, Supplier<Integer, Double> initValueSupplier) {
        double[] array = new double[length];
        if (initValueSupplier != null) {
            for (int i = 0; i < length; i++) {
                array[i] = initValueSupplier.get(i);
            }
        }
        return array;
    }

    public static boolean[] createBooleanArray(int length, final boolean initValue) {
        return createBooleanArray(length, new Supplier<Integer, Boolean>() {
            @Override
            public Boolean get(Integer index) {
                return initValue;
            }
        });
    }

    public static boolean[] createBooleanArray(int length, Supplier<Integer, Boolean> initValueSupplier) {
        boolean[] array = new boolean[length];
        if (initValueSupplier != null) {
            for (int i = 0; i < length; i++) {
                array[i] = initValueSupplier.get(i);
            }
        }
        return array;
    }

    public static byte[] copy(final byte[] values) {
        return createByteArray(values.length, new Supplier<Integer, Byte>() {
            @Override
            public Byte get(Integer index) {
                return values[index];
            }
        });
    }

    public static short[] copy(final short[] values) {
        return createShortArray(values.length, new Supplier<Integer, Short>() {
            @Override
            public Short get(Integer input) {
                return values[input];
            }
        });
    }

    public static int[] copy(final int[] values) {
        return createIntArray(values.length, new Supplier<Integer, Integer>() {
            @Override
            public Integer get(Integer input) {
                return values[input];
            }
        });
    }

    public static boolean[] copy(final boolean[] values) {
        return createBooleanArray(values.length, new Supplier<Integer, Boolean>() {
            @Override
            public Boolean get(Integer input) {
                return values[input];
            }
        });
    }

    public static char[] copy(final char[] values) {
        return createCharArray(values.length, new Supplier<Integer, Character>() {
            @Override
            public Character get(Integer input) {
                return values[input];
            }
        });
    }

    public static float[] copy(final float[] values) {
        return createFloatArray(values.length, new Supplier<Integer, Float>() {
            @Override
            public Float get(Integer input) {
                return values[input];
            }
        });
    }

    public static double[] copy(final double[] values) {
        return createDoubleArray(values.length, new Supplier<Integer, Double>() {
            @Override
            public Double get(Integer input) {
                return values[input];
            }
        });
    }

    public static long[] copy(final long[] values) {
        return createLongArray(values.length, new Supplier<Integer, Long>() {
            @Override
            public Long get(Integer input) {
                return values[input];
            }
        });
    }


    // boolean IndexOf
    //-----------------------------------------------------------------------

    /**
     * <p>Finds the index of the given value in the array.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final boolean[] array, final boolean valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>Finds the index of the given value in the array starting at the given index.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex  the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null}
     * array input
     */
    public static int indexOf(final boolean[] array, final boolean valueToFind, int startIndex) {
        if (Objs.isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // byte IndexOf
    //-----------------------------------------------------------------------

    /**
     * <p>Finds the index of the given value in the array.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final byte[] array, final byte valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>Finds the index of the given value in the array starting at the given index.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex  the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final byte[] array, final byte valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // char IndexOf
    //-----------------------------------------------------------------------

    /**
     * <p>Finds the index of the given value in the array.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     * @since 2.1
     */
    public static int indexOf(final char[] array, final char valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>Finds the index of the given value in the array starting at the given index.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex  the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     * @since 2.1
     */
    public static int indexOf(final char[] array, final char valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // double IndexOf
    //-----------------------------------------------------------------------

    /**
     * <p>Finds the index of the given value in the array.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array, final double valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>Finds the index of the given value within a given tolerance in the array.
     * This method will return the index of the first value which falls between the region
     * defined by valueToFind - tolerance and valueToFind + tolerance.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param tolerance   tolerance of the search
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array, final double valueToFind, final double tolerance) {
        return indexOf(array, valueToFind, 0, tolerance);
    }

    /**
     * <p>Finds the index of the given value in the array starting at the given index.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex  the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array, final double valueToFind, int startIndex) {
        if (Objs.isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        final boolean searchNaN = Double.isNaN(valueToFind);
        for (int i = startIndex; i < array.length; i++) {
            final double element = array[i];
            if (valueToFind == element || (searchNaN && Double.isNaN(element))) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>Finds the index of the given value in the array starting at the given index.
     * This method will return the index of the first value which falls between the region
     * defined by valueToFind - tolerance and valueToFind + tolerance.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex  the index to start searching at
     * @param tolerance   tolerance of the search
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array, final double valueToFind, int startIndex, final double tolerance) {
        if (Objs.isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        final double min = valueToFind - tolerance;
        final double max = valueToFind + tolerance;
        for (int i = startIndex; i < array.length; i++) {
            if (array[i] >= min && array[i] <= max) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // float IndexOf
    //-----------------------------------------------------------------------

    /**
     * <p>Finds the index of the given value in the array.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final float[] array, final float valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>Finds the index of the given value in the array starting at the given index.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex  the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final float[] array, final float valueToFind, int startIndex) {
        if (Objs.isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        final boolean searchNaN = Float.isNaN(valueToFind);
        for (int i = startIndex; i < array.length; i++) {
            final float element = array[i];
            if (valueToFind == element || (searchNaN && Float.isNaN(element))) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // int IndexOf
    //-----------------------------------------------------------------------

    /**
     * <p>Finds the index of the given value in the array.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final int[] array, final int valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>Finds the index of the given value in the array starting at the given index.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex  the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final int[] array, final int valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // long IndexOf
    //-----------------------------------------------------------------------

    /**
     * <p>Finds the index of the given value in the array.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final long[] array, final long valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>Finds the index of the given value in the array starting at the given index.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex  the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final long[] array, final long valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }


    // short IndexOf
    //-----------------------------------------------------------------------

    /**
     * <p>Finds the index of the given value in the array.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final short[] array, final short valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>Finds the index of the given value in the array starting at the given index.
     *
     * <p>This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array       the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex  the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final short[] array, final short valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>Inserts elements into an array at the given index (starting from zero).</p>
     *
     * <p>When an array is returned, it is always a new array.</p>
     *
     * <pre>
     * ArrayUtils.insert(index, null, null)      = null
     * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
     * ArrayUtils.insert(index, null, values)    = null
     * </pre>
     *
     * @param index  the position within {@code array} to insert the new values
     * @param array  the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return The new array.
     * @throws IndexOutOfBoundsException if {@code array} is provided
     *                                   and either {@code index < 0} or {@code index > array.length}
     * @since 3.6
     */
    public static boolean[] insert(final int index, final boolean[] array, final boolean... values) {
        if (array == null) {
            return array;
        }
        if (Objs.isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final boolean[] result = new boolean[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * <p>Inserts elements into an array at the given index (starting from zero).</p>
     *
     * <p>When an array is returned, it is always a new array.</p>
     *
     * <pre>
     * ArrayUtils.insert(index, null, null)      = null
     * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
     * ArrayUtils.insert(index, null, values)    = null
     * </pre>
     *
     * @param index  the position within {@code array} to insert the new values
     * @param array  the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return The new array.
     * @throws IndexOutOfBoundsException if {@code array} is provided
     *                                   and either {@code index < 0} or {@code index > array.length}
     * @since 3.6
     */
    public static byte[] insert(final int index, final byte[] array, final byte... values) {
        if (array == null) {
            return array;
        }
        if (Objs.isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final byte[] result = new byte[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * <p>Inserts elements into an array at the given index (starting from zero).</p>
     *
     * <p>When an array is returned, it is always a new array.</p>
     *
     * <pre>
     * ArrayUtils.insert(index, null, null)      = null
     * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
     * ArrayUtils.insert(index, null, values)    = null
     * </pre>
     *
     * @param index  the position within {@code array} to insert the new values
     * @param array  the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return The new array.
     * @throws IndexOutOfBoundsException if {@code array} is provided
     *                                   and either {@code index < 0} or {@code index > array.length}
     * @since 3.6
     */
    public static char[] insert(final int index, final char[] array, final char... values) {
        if (array == null) {
            return array;
        }
        if (Objs.isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final char[] result = new char[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * <p>Inserts elements into an array at the given index (starting from zero).</p>
     *
     * <p>When an array is returned, it is always a new array.</p>
     *
     * <pre>
     * ArrayUtils.insert(index, null, null)      = null
     * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
     * ArrayUtils.insert(index, null, values)    = null
     * </pre>
     *
     * @param index  the position within {@code array} to insert the new values
     * @param array  the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return The new array.
     * @throws IndexOutOfBoundsException if {@code array} is provided
     *                                   and either {@code index < 0} or {@code index > array.length}
     * @since 3.6
     */
    public static double[] insert(final int index, final double[] array, final double... values) {
        if (array == null) {
            return array;
        }
        if (Objs.isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final double[] result = new double[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * <p>Inserts elements into an array at the given index (starting from zero).</p>
     *
     * <p>When an array is returned, it is always a new array.</p>
     *
     * <pre>
     * ArrayUtils.insert(index, null, null)      = null
     * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
     * ArrayUtils.insert(index, null, values)    = null
     * </pre>
     *
     * @param index  the position within {@code array} to insert the new values
     * @param array  the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return The new array.
     * @throws IndexOutOfBoundsException if {@code array} is provided
     *                                   and either {@code index < 0} or {@code index > array.length}
     * @since 3.6
     */
    public static float[] insert(final int index, final float[] array, final float... values) {
        if (array == null) {
            return array;
        }
        if (Objs.isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final float[] result = new float[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * <p>Inserts elements into an array at the given index (starting from zero).</p>
     *
     * <p>When an array is returned, it is always a new array.</p>
     *
     * <pre>
     * ArrayUtils.insert(index, null, null)      = null
     * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
     * ArrayUtils.insert(index, null, values)    = null
     * </pre>
     *
     * @param index  the position within {@code array} to insert the new values
     * @param array  the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return The new array.
     * @throws IndexOutOfBoundsException if {@code array} is provided
     *                                   and either {@code index < 0} or {@code index > array.length}
     * @since 3.6
     */
    public static int[] insert(final int index, final int[] array, final int... values) {
        if (array == null) {
            return array;
        }
        if (Objs.isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final int[] result = new int[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * <p>Inserts elements into an array at the given index (starting from zero).</p>
     *
     * <p>When an array is returned, it is always a new array.</p>
     *
     * <pre>
     * ArrayUtils.insert(index, null, null)      = null
     * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
     * ArrayUtils.insert(index, null, values)    = null
     * </pre>
     *
     * @param index  the position within {@code array} to insert the new values
     * @param array  the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return The new array.
     * @throws IndexOutOfBoundsException if {@code array} is provided
     *                                   and either {@code index < 0} or {@code index > array.length}
     * @since 3.6
     */
    public static long[] insert(final int index, final long[] array, final long... values) {
        if (array == null) {
            return array;
        }
        if (Objs.isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final long[] result = new long[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * <p>Inserts elements into an array at the given index (starting from zero).</p>
     *
     * <p>When an array is returned, it is always a new array.</p>
     *
     * <pre>
     * ArrayUtils.insert(index, null, null)      = null
     * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
     * ArrayUtils.insert(index, null, values)    = null
     * </pre>
     *
     * @param index  the position within {@code array} to insert the new values
     * @param array  the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return The new array.
     * @throws IndexOutOfBoundsException if {@code array} is provided
     *                                   and either {@code index < 0} or {@code index > array.length}
     * @since 3.6
     */
    public static short[] insert(final int index, final short[] array, final short... values) {
        if (array == null) {
            return array;
        }
        if (Objs.isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final short[] result = new short[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * <p>Inserts elements into an array at the given index (starting from zero).</p>
     *
     * <p>When an array is returned, it is always a new array.</p>
     *
     * <pre>
     * ArrayUtils.insert(index, null, null)      = null
     * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
     * ArrayUtils.insert(index, null, values)    = null
     * </pre>
     *
     * @param <T>    The type of elements in {@code array} and {@code values}
     * @param index  the position within {@code array} to insert the new values
     * @param array  the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return The new array.
     * @throws IndexOutOfBoundsException if {@code array} is provided
     *                                   and either {@code index < 0} or {@code index > array.length}
     * @since 3.6
     */
    public static <T> T[] insert(final int index, final T[] array, final T... values) {
        /*
         * Note on use of @SafeVarargs:
         *
         * By returning null when 'array' is null, we avoid returning the vararg
         * array to the caller. We also avoid relying on the type of the vararg
         * array, by inspecting the component type of 'array'.
         */

        if (array == null) {
            return array;
        }
        if (Objs.isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final Class<?> type = array.getClass().getComponentType();
        @SuppressWarnings("unchecked") // OK, because array and values are of type T
        final T[] result = (T[]) Array.newInstance(type, array.length + values.length);

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    public static boolean[] clone(final boolean[] array) {
        if (array == null) {
            return array;
        }
        return array.clone();
    }

    /**
     * <p>Clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>This method returns {@code null} for a {@code null} input array.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static byte[] clone(final byte[] array) {
        if (array == null) {
            return array;
        }
        return array.clone();
    }

    /**
     * <p>Clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>This method returns {@code null} for a {@code null} input array.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static char[] clone(final char[] array) {
        if (array == null) {
            return array;
        }
        return array.clone();
    }

    /**
     * <p>Clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>This method returns {@code null} for a {@code null} input array.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static double[] clone(final double[] array) {
        if (array == null) {
            return array;
        }
        return array.clone();
    }

    /**
     * <p>Clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>This method returns {@code null} for a {@code null} input array.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static float[] clone(final float[] array) {
        if (array == null) {
            return array;
        }
        return array.clone();
    }

    /**
     * <p>Clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>This method returns {@code null} for a {@code null} input array.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static int[] clone(final int[] array) {
        if (array == null) {
            return array;
        }
        return array.clone();
    }

    /**
     * <p>Clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>This method returns {@code null} for a {@code null} input array.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static long[] clone(final long[] array) {
        if (array == null) {
            return array;
        }
        return array.clone();
    }

    /**
     * <p>Clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>This method returns {@code null} for a {@code null} input array.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static short[] clone(final short[] array) {
        if (array == null) {
            return array;
        }
        return array.clone();
    }

    // Clone
    //-----------------------------------------------------------------------

    /**
     * <p>Shallow clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>The objects in the array are not cloned, thus there is no special
     * handling for multi-dimensional arrays.
     *
     * <p>This method returns {@code null} for a {@code null} input array.
     *
     * @param <T>   the component type of the array
     * @param array the array to shallow clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static <T> T[] clone(final T[] array) {
        if (array == null) {
            return array;
        }
        return array.clone();
    }

    /**
     * Sorts the specified array into ascending numerical order.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a the array to be sorted
     */
    public static void sort(int[] a) {
        DualPivotQuicksort.sort(a, 0, a.length - 1, null, 0, 0);
    }

    /**
     * Sorts the specified range of the array into ascending order. The range
     * to be sorted extends from the index {@code fromIndex}, inclusive, to
     * the index {@code toIndex}, exclusive. If {@code fromIndex == toIndex},
     * the range to be sorted is empty.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a         the array to be sorted
     * @param fromIndex the index of the first element, inclusive, to be sorted
     * @param toIndex   the index of the last element, exclusive, to be sorted
     * @throws IllegalArgumentException       if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0} or {@code toIndex > a.length}
     */
    public static void sort(int[] a, int fromIndex, int toIndex) {
        Preconditions.checkFromToIndex(fromIndex, toIndex, a.length);
        DualPivotQuicksort.sort(a, fromIndex, toIndex - 1, null, 0, 0);
    }

    /**
     * Sorts the specified array into ascending numerical order.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a the array to be sorted
     */
    public static void sort(long[] a) {
        DualPivotQuicksort.sort(a, 0, a.length - 1, null, 0, 0);
    }

    /**
     * Sorts the specified range of the array into ascending order. The range
     * to be sorted extends from the index {@code fromIndex}, inclusive, to
     * the index {@code toIndex}, exclusive. If {@code fromIndex == toIndex},
     * the range to be sorted is empty.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a         the array to be sorted
     * @param fromIndex the index of the first element, inclusive, to be sorted
     * @param toIndex   the index of the last element, exclusive, to be sorted
     * @throws IllegalArgumentException       if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0} or {@code toIndex > a.length}
     */
    public static void sort(long[] a, int fromIndex, int toIndex) {
        Preconditions.checkFromToIndex(fromIndex, toIndex, a.length);
        DualPivotQuicksort.sort(a, fromIndex, toIndex - 1, null, 0, 0);
    }

    /**
     * Sorts the specified array into ascending numerical order.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a the array to be sorted
     */
    public static void sort(short[] a) {
        DualPivotQuicksort.sort(a, 0, a.length - 1, null, 0, 0);
    }

    /**
     * Sorts the specified range of the array into ascending order. The range
     * to be sorted extends from the index {@code fromIndex}, inclusive, to
     * the index {@code toIndex}, exclusive. If {@code fromIndex == toIndex},
     * the range to be sorted is empty.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a         the array to be sorted
     * @param fromIndex the index of the first element, inclusive, to be sorted
     * @param toIndex   the index of the last element, exclusive, to be sorted
     * @throws IllegalArgumentException       if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0} or {@code toIndex > a.length}
     */
    public static void sort(short[] a, int fromIndex, int toIndex) {
        Preconditions.checkFromToIndex(fromIndex, toIndex, a.length);
        DualPivotQuicksort.sort(a, fromIndex, toIndex - 1, null, 0, 0);
    }

    /**
     * Sorts the specified array into ascending numerical order.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a the array to be sorted
     */
    public static void sort(char[] a) {
        DualPivotQuicksort.sort(a, 0, a.length - 1, null, 0, 0);
    }

    /**
     * Sorts the specified range of the array into ascending order. The range
     * to be sorted extends from the index {@code fromIndex}, inclusive, to
     * the index {@code toIndex}, exclusive. If {@code fromIndex == toIndex},
     * the range to be sorted is empty.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a         the array to be sorted
     * @param fromIndex the index of the first element, inclusive, to be sorted
     * @param toIndex   the index of the last element, exclusive, to be sorted
     * @throws IllegalArgumentException       if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0} or {@code toIndex > a.length}
     */
    public static void sort(char[] a, int fromIndex, int toIndex) {
        Preconditions.checkFromToIndex(fromIndex, toIndex, a.length);
        DualPivotQuicksort.sort(a, fromIndex, toIndex - 1, null, 0, 0);
    }

    /**
     * Sorts the specified array into ascending numerical order.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a the array to be sorted
     */
    public static void sort(byte[] a) {
        DualPivotQuicksort.sort(a, 0, a.length - 1);
    }

    /**
     * Sorts the specified range of the array into ascending order. The range
     * to be sorted extends from the index {@code fromIndex}, inclusive, to
     * the index {@code toIndex}, exclusive. If {@code fromIndex == toIndex},
     * the range to be sorted is empty.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a         the array to be sorted
     * @param fromIndex the index of the first element, inclusive, to be sorted
     * @param toIndex   the index of the last element, exclusive, to be sorted
     * @throws IllegalArgumentException       if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0} or {@code toIndex > a.length}
     */
    public static void sort(byte[] a, int fromIndex, int toIndex) {
        Preconditions.checkFromToIndex(fromIndex, toIndex, a.length);
        DualPivotQuicksort.sort(a, fromIndex, toIndex - 1);
    }

    /**
     * Sorts the specified array into ascending numerical order.
     *
     * <p>The {@code <} relation does not provide a total order on all float
     * values: {@code -0.0f == 0.0f} is {@code true} and a {@code Float.NaN}
     * value compares neither less than, greater than, nor equal to any value,
     * even itself. This method uses the total order imposed by the method
     * {@link Float#compareTo}: {@code -0.0f} is treated as less than value
     * {@code 0.0f} and {@code Float.NaN} is considered greater than any
     * other value and all {@code Float.NaN} values are considered equal.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a the array to be sorted
     */
    public static void sort(float[] a) {
        DualPivotQuicksort.sort(a, 0, a.length - 1, null, 0, 0);
    }

    /**
     * Sorts the specified range of the array into ascending order. The range
     * to be sorted extends from the index {@code fromIndex}, inclusive, to
     * the index {@code toIndex}, exclusive. If {@code fromIndex == toIndex},
     * the range to be sorted is empty.
     *
     * <p>The {@code <} relation does not provide a total order on all float
     * values: {@code -0.0f == 0.0f} is {@code true} and a {@code Float.NaN}
     * value compares neither less than, greater than, nor equal to any value,
     * even itself. This method uses the total order imposed by the method
     * {@link Float#compareTo}: {@code -0.0f} is treated as less than value
     * {@code 0.0f} and {@code Float.NaN} is considered greater than any
     * other value and all {@code Float.NaN} values are considered equal.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a         the array to be sorted
     * @param fromIndex the index of the first element, inclusive, to be sorted
     * @param toIndex   the index of the last element, exclusive, to be sorted
     * @throws IllegalArgumentException       if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0} or {@code toIndex > a.length}
     */
    public static void sort(float[] a, int fromIndex, int toIndex) {
        Preconditions.checkFromToIndex(fromIndex, toIndex, a.length);
        DualPivotQuicksort.sort(a, fromIndex, toIndex - 1, null, 0, 0);
    }

    /**
     * Sorts the specified array into ascending numerical order.
     *
     * <p>The {@code <} relation does not provide a total order on all double
     * values: {@code -0.0d == 0.0d} is {@code true} and a {@code Double.NaN}
     * value compares neither less than, greater than, nor equal to any value,
     * even itself. This method uses the total order imposed by the method
     * {@link Double#compareTo}: {@code -0.0d} is treated as less than value
     * {@code 0.0d} and {@code Double.NaN} is considered greater than any
     * other value and all {@code Double.NaN} values are considered equal.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a the array to be sorted
     */
    public static void sort(double[] a) {
        DualPivotQuicksort.sort(a, 0, a.length - 1, null, 0, 0);
    }

    /**
     * Sorts the specified range of the array into ascending order. The range
     * to be sorted extends from the index {@code fromIndex}, inclusive, to
     * the index {@code toIndex}, exclusive. If {@code fromIndex == toIndex},
     * the range to be sorted is empty.
     *
     * <p>The {@code <} relation does not provide a total order on all double
     * values: {@code -0.0d == 0.0d} is {@code true} and a {@code Double.NaN}
     * value compares neither less than, greater than, nor equal to any value,
     * even itself. This method uses the total order imposed by the method
     * {@link Double#compareTo}: {@code -0.0d} is treated as less than value
     * {@code 0.0d} and {@code Double.NaN} is considered greater than any
     * other value and all {@code Double.NaN} values are considered equal.
     *
     * <p>Implementation note: The sorting algorithm is a Dual-Pivot Quicksort
     * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
     * offers O(n log(n)) performance on many data sets that cause other
     * quicksorts to degrade to quadratic performance, and is typically
     * faster than traditional (one-pivot) Quicksort implementations.
     *
     * @param a         the array to be sorted
     * @param fromIndex the index of the first element, inclusive, to be sorted
     * @param toIndex   the index of the last element, exclusive, to be sorted
     * @throws IllegalArgumentException       if {@code fromIndex > toIndex}
     * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0} or {@code toIndex > a.length}
     */
    public static void sort(double[] a, int fromIndex, int toIndex) {
        Preconditions.checkFromToIndex(fromIndex, toIndex, a.length);
        DualPivotQuicksort.sort(a, fromIndex, toIndex - 1, null, 0, 0);
    }


}
