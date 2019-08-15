package com.jn.langx.util;

import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.type.Primitives;

import java.lang.reflect.Array;

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
    public static boolean isArray(Object o) {
        return Emptys.isNull(o) ? false : o.getClass().isArray();
    }

    /**
     * wrap a string using new String[]{string}
     */
    public static String[] wrapAsArray(String string) {
        if (Emptys.isNull(string)) {
            return new String[0];
        }
        return new String[]{string};
    }

    /**
     * wrap a number using new String[]{number}
     */
    public static Number[] wrapAsArray(Number number) {
        if (Emptys.isNull(number)) {
            return new Number[0];
        }
        return new Number[]{number};
    }

    /**
     * wrap a boolean using new String[]{bool}
     */
    public static Boolean[] wrapAsArray(Boolean bool) {
        if (Emptys.isNull(bool)) {
            return new Boolean[0];
        }
        return new Boolean[]{bool};
    }

    /**
     * Wrap any object using new Object[]{object};
     */
    public static Object[] wrapAsArray(Object o) {
        if (Emptys.isNull(o)) {
            return new Object[0];
        }
        return new Object[]{o};
    }

    /**
     * Create an array with the specified length
     */
    public static <E> E[] createArray(Class<E> clazz, int length) {
        return (E[]) Array.newInstance(clazz, length);
    }

    /**
     * Create an array with the specified length and every element's value is the specified initValue
     */
    public static <E> E[] createArray(Class<E> clazz, int length, final E initValue) {
        return createArray(clazz, length, new Supplier<Integer, E>() {
            @Override
            public E get(Integer index) {
                return initValue;
            }
        });
    }

    /**
     * Create an array with the specified length and every element's value is supplied by the specified initSupplier
     */
    public static <E> E[] createArray(Class<E> clazz, int length, Supplier<Integer, E> initSupplier) {
        Preconditions.checkNotNull(initSupplier);
        if (Primitives.isPrimitive(clazz)) {
            clazz = Primitives.wrap(clazz);
        }
        E[] array = (E[]) Array.newInstance(clazz, length);
        for (int i = 0; i < array.length; i++) {
            array[i] = initSupplier.get(i);
        }
        return array;
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
        Preconditions.checkArgument(start >= 0);
        Preconditions.checkArgument(end >= start);
        Preconditions.checkArgument(step >= 1);
        int length = new Double(Math.floor((end - 1 - start) / step)).intValue() + 1;
        Preconditions.checkTrue(length >= 0);
        return createArray(Integer.class, length, new Supplier<Integer, Integer>() {
            @Override
            public Integer get(Integer index) {
                return start + step * index;
            }
        });
    }


}
