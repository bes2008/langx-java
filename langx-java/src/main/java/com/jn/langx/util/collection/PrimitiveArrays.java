package com.jn.langx.util.collection;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
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

    public static final List<Class> PRIMITIVE_ARRAY_CLASSES = Collects.asList(Collects.asList(
            BYTE_ARRAY_CLASS,
            SHORT_ARRAY_CLASS,
            INT_ARRAY_CLASS,
            LONG_ARRAY_CLASS,
            FLOAT_ARRAY_CLASS,
            DOUBLE_ARRAY_CLASS,
            CHAR_ARRAY_CLASS,
            BOOLEAN_ARRAY_CLASS
    ), false);

    private PrimitiveArrays(){}

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
}
