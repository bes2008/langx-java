package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.type.Primitives;
import com.jn.langx.util.struct.Holder;

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
     * Create an array with the specified length.
     * <p>
     * int.class => Integer[]
     * Integer.class => Integer[]
     */
    public static <E> E[] createArray(@Nullable Class<E> componentType, int length) {
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
        int length = (end - 1 - start) / step + 1;
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
    public static <E> void swap(@NonNull E[] arr, int i, int j) {
        E tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 反转索引
     */
    public static int reverseIndex(int length, int index) {
        Preconditions.checkTrue(isValidIndex(length, index), "index " + index + " is invalid");
        if (isPositiveIndex(length, index)) {
            return index - length;
        } else {
            return length + index;
        }
    }

    /**
     * 判断索引是否有效
     */
    public static boolean isValidIndex(int length, int index) {
        return isNegativeIndex(length, index) || isPositiveIndex(length, index);
    }

    /**
     * 判断是否为正数索引
     */
    public static boolean isPositiveIndex(int length, int index) {
        Preconditions.checkTrue(length > 0, "length " + length + " is invalid");
        int max = length - 1;
        int min = 0;
        return index >= min && index <= max;
    }

    /**
     * 判断是否为负数索引
     */
    public static boolean isNegativeIndex(int length, int index) {
        Preconditions.checkTrue(length > 0, "length " + length + " is invalid");
        int max = -1;
        int min = -length;
        return index >= min && index <= max;
    }

    public static boolean contains(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind) != -1;
    }

    public static <T> int indexOf(T[] array, Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
        if (array == null) {
            return -1;
        } else {
            if (startIndex < 0) {
                startIndex = 0;
            }

            int i;
            if (objectToFind == null) {
                for (i = startIndex; i < array.length; ++i) {
                    if (array[i] == null) {
                        return i;
                    }
                }
            } else {
                for (i = startIndex; i < array.length; ++i) {
                    if (objectToFind.equals(array[i])) {
                        return i;
                    }
                }
            }

            return -1;
        }
    }

    public static <E> E[] copy(final E... objs) {
        if (Objs.isEmpty(objs)) {
            return (E[]) new Object[0];
        }
        Class componentType = objs.getClass().getComponentType();
        E[] newArray = (E[]) createArray(componentType, objs.length);
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = objs[i];
        }
        return newArray;
    }

    public static <E> boolean isMixedArray(E[] objs) {
        final Holder<Class> elementType = new Holder<Class>();
        boolean isMixed = Collects.anyMatch(new Predicate<Object>() {
            @Override
            public boolean test(Object element) {
                if (element != null) {
                    if (elementType.isNull()) {
                        elementType.set(element.getClass());
                    } else {
                        return elementType.get() != element.getClass();
                    }
                }
                return false;
            }
        }, objs);
        objs.getClass().getComponentType();
        return isMixed;
    }
}
