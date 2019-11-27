package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.type.Primitives;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Array tools
 */
public class Arrs {

    /**
     * get the length if argument is an array, else -1
     *
     * @param object any object
     * @return the length if argument is an array, else -1
     */
    public static int getLength(Object object) {
        if (isArray(object)) {
            return Array.getLength(object);
        }
        return -1;
    }

    /**
     * judge whether an object is an Array
     */
    public static boolean isArray(@Nullable Object o) {
        return Emptys.isNotNull(o) && o.getClass().isArray();
    }


    /**
     * Wrap any object using new Object[]{object};
     */
    public static <E> E[] wrapAsArray(@Nullable E o) {
        if (Emptys.isNull(o)) {
            return (E[]) new Object[0];
        }
        E[] array = (E[]) createArray(o.getClass(), 1);
        initArray(array, o);
        return array;
    }

    /**
     * Create an array with the specified length
     */
    public static <E extends Object> E[] createArray(@Nullable Class<E> componentType, int length) {
        Preconditions.checkTrue(length >= 0);
        if (componentType == null) {
            return (E[]) Array.newInstance(Object.class, length);
        }
        if (Primitives.isPrimitive(componentType)) {
            componentType = Primitives.wrap(componentType);
        }
        return (E[]) Array.newInstance(componentType, length);
    }

    /**
     * Create an array with the specified length and every element's value is the specified initValue
     */
    public static <E extends Object> E[] createArray(@Nullable Class<E> componentType, int length, @Nullable final E initValue) {
        E[] array = createArray(componentType, length);
        initArray(array, initValue);
        return array;
    }


    /**
     * Create an array with the specified length and every element's value is supplied by the specified initSupplier
     */
    public static <E extends Object> E[] createArray(@Nullable Class<E> componentType, int length, @NonNull Supplier<Integer, E> initSupplier) {
        E[] array = createArray(componentType, length);
        initArray(array, initSupplier);
        return array;
    }

    public static <E> void initArray(@NonNull E[] array, @Nullable final E initValue) {
        Preconditions.checkNotNull(array);
        initArray(array, new Supplier<Integer, E>() {
            @Override
            public E get(Integer index) {
                return initValue;
            }
        });
    }

    public static <E> void initArray(@NonNull E[] array, @NonNull Supplier<Integer, E> initSupplier) {
        Preconditions.checkNotNull(initSupplier);
        for (int i = 0; i < array.length; i++) {
            array[i] = initSupplier.get(i);
        }
    }


    /**
     * It is similar to Python's range(start, end, step)
     * [start, end)
     *
     * @see #range(int, int, int)
     */
    public static Integer[] range(int end) {
        return range(0, end);
    }

    /**
     * It is similar to Python's range(start, end, step)
     * [start, end)
     *
     * @see #range(int, int, int)
     */
    public static Integer[] range(int start, int end) {
        return range(start, end, 1);
    }

    /**
     * It is similar to Python's range(start, end, step)
     * [start, end)
     */
    public static Integer[] range(final int start, int end, final int step) {
        Preconditions.checkTrue(start >= 0);
        Preconditions.checkTrue(end >= start);
        Preconditions.checkTrue(step >= 1);
        int length = new Double(Math.floor((end - 1 - start) / step)).intValue() + 1;
        Preconditions.checkTrue(length >= 0);
        return createArray(Integer.class, length, new Supplier<Integer, Integer>() {
            @Override
            public Integer get(Integer index) {
                return start + step * index;
            }
        });
    }

    public static boolean deepEquals(Object[] a1, Object[] a2) {
        return Arrays.deepEquals(a1, a2);
    }

    public static boolean deepEquals(Object e1, Object e2) {
        if (e1 == e2) {
            return true;
        }
        if (e1 == null || e2 == null) {
            return false;
        }
        boolean eq;
        if (e1 instanceof Object[] && e2 instanceof Object[]) {
            eq = Arrays.deepEquals((Object[]) e1, (Object[]) e2);
        } else if (e1 instanceof byte[] && e2 instanceof byte[]) {
            eq = PrimitiveArrays.equals((byte[]) e1, (byte[]) e2);
        } else if (e1 instanceof short[] && e2 instanceof short[]) {
            eq = PrimitiveArrays.equals((short[]) e1, (short[]) e2);
        } else if (e1 instanceof int[] && e2 instanceof int[]) {
            eq = PrimitiveArrays.equals((int[]) e1, (int[]) e2);
        } else if (e1 instanceof long[] && e2 instanceof long[]) {
            eq = PrimitiveArrays.equals((long[]) e1, (long[]) e2);
        } else if (e1 instanceof char[] && e2 instanceof char[]) {
            eq = PrimitiveArrays.equals((char[]) e1, (char[]) e2);
        } else if (e1 instanceof float[] && e2 instanceof float[]) {
            eq = PrimitiveArrays.equals((float[]) e1, (float[]) e2);
        } else if (e1 instanceof double[] && e2 instanceof double[]) {
            eq = PrimitiveArrays.equals((double[]) e1, (double[]) e2);
        } else if (e1 instanceof boolean[] && e2 instanceof boolean[]) {
            eq = PrimitiveArrays.equals((boolean[]) e1, (boolean[]) e2);
        } else {
            eq = e1.equals(e2);
        }
        return eq;
    }

    /**
     * Swaps the two specified elements in the specified array.
     */
    public static <E>void swap(@NonNull E[] arr, int i, int j) {
        E tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
