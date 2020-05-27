package com.jn.langx.util.reflect.type;

import com.jn.langx.util.Preconditions;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Primitives {
    private Primitives() {
    }

    /**
     * A map from primitive types to their corresponding wrapper types.
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;

    /**
     * A map from wrapper types to their corresponding primitive types.
     */
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;

    // Sad that we can't use a BiMap. :(

    static {
        Map<Class<?>, Class<?>> primToWrap = new HashMap<Class<?>, Class<?>>(16);
        Map<Class<?>, Class<?>> wrapToPrim = new HashMap<Class<?>, Class<?>>(16);

        add(primToWrap, wrapToPrim, boolean.class, Boolean.class);
        add(primToWrap, wrapToPrim, byte.class, Byte.class);
        add(primToWrap, wrapToPrim, char.class, Character.class);
        add(primToWrap, wrapToPrim, double.class, Double.class);
        add(primToWrap, wrapToPrim, float.class, Float.class);
        add(primToWrap, wrapToPrim, int.class, Integer.class);
        add(primToWrap, wrapToPrim, long.class, Long.class);
        add(primToWrap, wrapToPrim, short.class, Short.class);
        add(primToWrap, wrapToPrim, void.class, Void.class);

        PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);
        WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
    }

    private static void add(Map<Class<?>, Class<?>> forward,
                            Map<Class<?>, Class<?>> backward, Class<?> key, Class<?> value) {
        forward.put(key, value);
        backward.put(value, key);
    }

    /**
     * Returns true if this type is a primitive.
     */
    public static boolean isPrimitive(Type type) {
        return PRIMITIVE_TO_WRAPPER_TYPE.containsKey(type);
    }

    /**
     * Returns {@code true} if {@code type} is one of the nine
     * primitive-wrapper types, such as {@link Integer}.
     *
     * @see Class#isPrimitive
     */
    public static boolean isWrapperType(Type type) {
        return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(Preconditions.checkNotNull(type));
    }

    /**
     * Returns the corresponding wrapper type of {@code type} if it is a primitive
     * type; otherwise returns {@code type} itself. Idempotent.
     * <pre>
     *     wrap(int.class) == Integer.class
     *     wrap(Integer.class) == Integer.class
     *     wrap(String.class) == String.class
     * </pre>
     */
    public static <T> Class<T> wrap(Class<T> type) {
        // cast is safe: long.class and Long.class are both of type Class<Long>
        @SuppressWarnings("unchecked")
        Class<T> wrapped = (Class<T>) PRIMITIVE_TO_WRAPPER_TYPE.get(Preconditions.checkNotNull(type));
        return (wrapped == null) ? type : wrapped;
    }

    /**
     * wrap a primitive type to warped class
     */
    public static Class wrap(Type type) {
        if (isPrimitive(type)) {
            return PRIMITIVE_TO_WRAPPER_TYPE.get(Preconditions.checkNotNull(type));
        }
        return (Class) type;
    }

    /**
     * Returns the corresponding primitive type of {@code type} if it is a
     * wrapper type; otherwise returns {@code type} itself. Idempotent.
     * <pre>
     *     unwrap(Integer.class) == int.class
     *     unwrap(int.class) == int.class
     *     unwrap(String.class) == String.class
     * </pre>
     */
    public static <T> Class<T> unwrap(Class<T> type) {
        // cast is safe: long.class and Long.class are both of type Class<Long>
        @SuppressWarnings("unchecked")
        Class<T> unwrapped = (Class<T>) WRAPPER_TO_PRIMITIVE_TYPE.get(Preconditions.checkNotNull(type));
        return (unwrapped == null) ? type : unwrapped;
    }


    public static short sizeOf(Class<?> type){
        Preconditions.checkArgument(isPrimitive(type));
        if (type == boolean.class || type == byte.class) {
            return 1;
        }
        if (type == char.class || type == short.class) {
            return 2;
        }
        if (type == int.class || type == float.class) {
            return 4;
        }
        if (type == long.class || type == double.class) {
            return 8;
        }
        // void
        return 0;
    }
}
