package com.jn.langx.util.collection;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
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
     * Wrap any object using new Object[]{object};
     */
    public static <E> E[] wrapAsArray(E o) {
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
    public static <E extends Object> E[] createArray(Class<E> componentType, int length) {
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
    public static <E extends Object> E[] createArray(Class<E> componentType, int length, final E initValue) {
        E[] array = createArray(componentType, length);
        initArray(array, initValue);
        return array;
    }


    /**
     * Create an array with the specified length and every element's value is supplied by the specified initSupplier
     */
    public static <E extends Object> E[] createArray(Class<E> componentType, int length, Supplier<Integer, E> initSupplier) {
        E[] array = createArray(componentType, length);
        initArray(array, initSupplier);
        return array;
    }

    public static <E> void initArray(E[] array, final E initValue) {
        Preconditions.checkNotNull(array);
        initArray(array, new Supplier<Integer, E>() {
            @Override
            public E get(Integer index) {
                return initValue;
            }
        });
    }

    public static <E> void initArray(E[] array, Supplier<Integer, E> initSupplier) {
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
