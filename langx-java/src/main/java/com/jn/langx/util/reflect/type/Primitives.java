package com.jn.langx.util.reflect.type;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Lists;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

    /**
     * @since 3.4.1
     */
    private static final HashMap<String, Class<?>> primClasses = new HashMap<String, Class<?>>(8, 1.0F);
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

        // @since 3.4.1
        primClasses.put("boolean", boolean.class);
        primClasses.put("byte", byte.class);
        primClasses.put("char", char.class);
        primClasses.put("short", short.class);
        primClasses.put("int", int.class);
        primClasses.put("long", long.class);
        primClasses.put("float", float.class);
        primClasses.put("double", double.class);
        primClasses.put("void", void.class);
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

    public static boolean isPrimitiveOrPrimitiveWrapperType(Type type) {
        return isPrimitive(type) || isWrapperType(type);
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


    public static short sizeOf(Class<?> type) {
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

    public static boolean isIntegerCompatible(Class clazz) {
        return isInteger(clazz) || isByte(clazz) || isShort(clazz);
    }

    public static boolean isChar(Type type) {
        // char.class 等价于  Character.TYPE
        return type == char.class || type == Character.class ;
    }

    public static boolean isByte(Type type) {
        // byte.class 等价于  Byte.TYPE
        return type == byte.class || type == Byte.class ;
    }

    public static boolean isShort(Type type) {
        // short.class 等价于  Short.TYPE
        return type == short.class || type == Short.class ;
    }

    public static boolean isInteger(Type type) {
        // int.class 等价于  Integer.TYPE
        return type == int.class || type == Integer.class ;
    }

    public static boolean isLong(Type type) {
        // long.class 等价于  Long.TYPE
        return type == long.class || type == Long.class ;
    }


    public static boolean isFloat(Type type) {
        // float.class 等价于  Float.TYPE
        return type == float.class || type == Float.class ;
    }

    public static boolean isDouble(Type type) {
        // double.class 等价于  Double.TYPE
        return type == double.class || type == Double.class ;
    }

    public static boolean isBoolean(Type type) {
        // boolean.class 等价于  Boolean.TYPE
        return type == boolean.class || type == Boolean.class ;
    }

    public static Class get(String name){
        return primClasses.get(name);
    }

    /**
     * @since 5.4.6
     */
    public List<Class<?>> getTypes(boolean onlyPrimitive){
        List<Class<?>> result = Lists.newArrayList(PRIMITIVE_TO_WRAPPER_TYPE.keySet());
        if(!onlyPrimitive){
            result.addAll(WRAPPER_TO_PRIMITIVE_TYPE.keySet());
        }
        return result;
    }
}
