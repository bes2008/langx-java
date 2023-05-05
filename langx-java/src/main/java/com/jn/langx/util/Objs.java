package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.function.Supplier0;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class Objs{
    private Objs(){}
    /**
     * Returns {@code true} if the arguments are equal to each other
     * and {@code false} otherwise.
     * Consequently, if both arguments are {@code null}, {@code true}
     * is returned and if exactly one argument is {@code null}, {@code
     * false} is returned.  Otherwise, equality is determined by using
     * the {@link Object#equals equals} method of the first
     * argument.
     *
     * @param a an object
     * @param b an object to be compared with {@code a} for equality
     * @return {@code true} if the arguments are equal to each other
     * and {@code false} otherwise
     * @see Object#equals(Object)
     */
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    /**
     * Returns {@code true} if the arguments are deeply equal to each other
     * and {@code false} otherwise.
     * <p>
     * Two {@code null} values are deeply equal.  If both arguments are
     * arrays, the algorithm in {@link Arrays#deepEquals(Object[],
     * Object[]) Arrays.deepEquals} is used to determine equality.
     * Otherwise, equality is determined by using the {@link
     * Object#equals equals} method of the first argument.
     *
     * @param a an object
     * @param b an object to be compared with {@code a} for deep equality
     * @return {@code true} if the arguments are deeply equal to each other
     * and {@code false} otherwise
     * @see Arrays#deepEquals(Object[], Object[])
     * @see Objs#equals(Object, Object)
     */
    public static boolean deepEquals(Object a, Object b) {
        if (a == b) {
            return true;
        } else if (a == null || b == null) {
            return false;
        }
        if (a.equals(b)) {
            return true;
        }
        if (a.getClass().isArray() && b.getClass().isArray()) {
            return Arrs.deepEquals(a,b);
        }
        return false;
    }

    /**
     * Returns the hash code of a non-{@code null} argument and 0 for
     * a {@code null} argument.
     *
     * @param o an object
     * @return the hash code of a non-{@code null} argument and 0 for
     * a {@code null} argument
     * @see Object#hashCode
     */
    public static int hashCode(Object o) {
        return hash(o);
    }

    /**
     * Generates a hash code for a sequence of input values. The hash
     * code is generated as if all the input values were placed into an
     * array, and that array were hashed by calling {@link
     * Arrays#hashCode(Object[])}.
     * <p>
     * <p>This method is useful for implementing {@link
     * Object#hashCode()} on objects containing multiple fields. For
     * example, if an object that has three fields, {@code x}, {@code
     * y}, and {@code z}, one could write:
     * <p>
     * <blockquote><pre>
     * &#064;Override public int hashCode() {
     *     return Objects.hash(x, y, z);
     * }
     * </pre></blockquote>
     * <p>
     * <b>Warning: When a single object reference is supplied, the returned
     * value does not equal the hash code of that object reference.</b> This
     * value can be computed by calling {@link #hashCode(Object)}.
     *
     * @param values the values to be hashed
     * @return a hash value of the sequence of input values
     * @see Arrays#hashCode(Object[])
     * @see List#hashCode
     */
    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    /**
     * Returns the result of calling {@code toString} for a non-{@code
     * null} argument and {@code "null"} for a {@code null} argument.
     *
     * @param o an object
     * @return the result of calling {@code toString} for a non-{@code
     * null} argument and {@code "null"} for a {@code null} argument
     * @see Object#toString
     * @see String#valueOf(Object)
     */
    public static String toString(Object o) {
        return String.valueOf(o);
    }

    /**
     * Returns the result of calling {@code toString} on the first
     * argument if the first argument is not {@code null} and returns
     * the second argument otherwise.
     *
     * @param o           an object
     * @param nullDefault string to return if the first argument is
     *                    {@code null}
     * @return the result of calling {@code toString} on the first
     * argument if it is not {@code null} and the second argument
     * otherwise.
     * @see Objs#toString(Object)
     */
    public static String toString(Object o, String nullDefault) {
        return (o != null) ? o.toString() : nullDefault;
    }

    public static String toStringOrNull(Object o) {
        return (o != null) ? o.toString() : null;
    }

    /**
     * Returns 0 if the arguments are identical and {@code
     * c.compare(a, b)} otherwise.
     * Consequently, if both arguments are {@code null} 0
     * is returned.
     * <p>
     * <p>Note that if one of the arguments is {@code null}, a {@code
     * NullPointerException} may or may not be thrown depending on
     * what ordering policy, if any, the {@link Comparator Comparator}
     * chooses to have for {@code null} values.
     *
     * @param <T> the type of the objects being compared
     * @param a   an object
     * @param b   an object to be compared with {@code a}
     * @param c   the {@code Comparator} to compare the first two arguments
     * @return 0 if the arguments are identical and {@code
     * c.compare(a, b)} otherwise.
     * @see Comparable
     * @see Comparator
     */
    public static <T> int compare(T a, T b, Comparator<? super T> c) {
        return (a == b) ? 0 : c.compare(a, b);
    }

    /**
     * Checks that the specified object reference is not {@code null}. This
     * method is designed primarily for doing parameter validation in methods
     * and constructors, as demonstrated below:
     * <blockquote><pre>
     * public Foo(Bar bar) {
     *     this.bar = Objects.requireNonNull(bar);
     * }
     * </pre></blockquote>
     *
     * @param obj the object reference to check for nullity
     * @param <T> the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNonNull(T obj) {
        return Preconditions.checkNotNull(obj);
    }

    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link NullPointerException} if it is. This method
     * is designed primarily for doing parameter validation in methods and
     * constructors with multiple parameters, as demonstrated below:
     * <blockquote><pre>
     * public Foo(Bar bar, Baz baz) {
     *     this.bar = Objects.requireNonNull(bar, "bar must not be null");
     *     this.baz = Objects.requireNonNull(baz, "baz must not be null");
     * }
     * </pre></blockquote>
     *
     * @param obj     the object reference to check for nullity
     * @param message detail message to be used in the event that a {@code
     *                NullPointerException} is thrown
     * @param <T>     the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNonNull(T obj, String message) {
        return Preconditions.checkNotNull(obj, message);
    }

    /**
     * Returns {@code true} if the provided reference is {@code null} otherwise
     * returns {@code false}.
     *
     * @param obj a reference to be checked against {@code null}
     * @return {@code true} if the provided reference is {@code null} otherwise
     * {@code false}
     * Note: This method exists to be used as a
     * {@link com.jn.langx.util.function.Predicate}, {@code filter(Objects::isNull)}
     * @see com.jn.langx.util.function.Predicate
     */
    public static boolean isNull(Object obj) {
        return Emptys.isNull(obj);
    }

    public static boolean isNotNull(Object obj) {
        return Emptys.isNotNull(obj);
    }

    /**
     * Returns {@code true} if the provided reference is non-{@code null}
     * otherwise returns {@code false}.
     *
     * @param obj a reference to be checked against {@code null}
     * @return {@code true} if the provided reference is non-{@code null}
     * otherwise {@code false}
     *  This method exists to be used as a
     * {@link com.jn.langx.util.function.Predicate}, {@code filter(Objects::nonNull)}
     * @see com.jn.langx.util.function.Predicate
     */
    public static boolean nonNull(Object obj) {
        return Emptys.isNotNull(obj);
    }

    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link NullPointerException} if it is.
     * <p>
     * <p>Unlike the method {@link #requireNonNull(Object, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param obj             the object reference to check for nullity
     * @param messageSupplier supplier of the detail message to be
     *                        used in the event that a {@code NullPointerException} is thrown
     * @param <T>             the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNonNull(T obj, Supplier<Object[], String> messageSupplier, Object... params) {
        return Preconditions.checkNotNull(obj, messageSupplier, params);
    }

    public static <T> T requireNonNull(T obj, final Supplier0<String> messageSupplier) {
        return Preconditions.checkNotNull(obj, new Supplier<Object[], String>() {
            @Override
            public String get(Object[] input) {
                return messageSupplier.get();
            }
        });
    }

    public static <T> T useValueIfNull(T value, T defaultValue) {
        return useValueIfMatch(value, Functions.<T>nullPredicate(), defaultValue);
    }

    public static <T> T useValueIfEmpty(T value, T defaultValue) {
        return useValueIfMatch(value, Functions.<T>emptyPredicate(), defaultValue);
    }

    public static <T> T useValueIfNull(T value, Supplier<T, T> supplier) {
        return useValueIfMatch(value, Functions.<T>nullPredicate(), supplier);
    }

    public static <T> T useValueIfEmpty(T value, Supplier<T, T> supplier) {
        return useValueIfMatch(value, Functions.<T>emptyPredicate(), supplier);
    }

    public static <T> T useValueIfMatch(T value, Predicate<T> predicate, T defaultValue) {
        if (predicate.test(value)) {
            return defaultValue;
        }
        return value;
    }

    public static <T> T useValueIfNotMatch(T value, Predicate<T> predicate, T defaultValue) {
        if (!predicate.test(value)) {
            return defaultValue;
        }
        return value;
    }

    public static <T> T useValueIfMatch(T value, Predicate<T> predicate, Supplier<T, T> defaultSupplier) {
        if (predicate.test(value)) {
            return defaultSupplier.get(value);
        }
        return value;
    }

    public static <T> T useValueIfMatch(@NonNull Supplier0<T> valueSupplier, Predicate<T> predicate, Supplier<T, T> defaultSupplier) {
        T value = null;
        if (valueSupplier != null) {
            value = valueSupplier.get();
        }
        return useValueIfMatch(value, predicate, defaultSupplier);
    }

    public static <T> T useValueIfNotMatch(T value, Predicate<T> predicate, Supplier<T, T> supplier) {
        if (!predicate.test(value)) {
            return supplier.get(value);
        }
        return value;
    }

    public static <T> T useValueIfNotMatch(@NonNull Supplier0<T> valueSupplier, Predicate<T> predicate, Supplier<T, T> defaultSupplier) {
        T value = null;
        if (valueSupplier != null) {
            value = valueSupplier.get();
        }
        return useValueIfMatch(value, predicate, defaultSupplier);
    }

    /**
     * Checks if the {@code index} is within the bounds of the range from
     * {@code 0} (inclusive) to {@code length} (exclusive).
     *
     * <p>The {@code index} is defined to be out of bounds if any of the
     * following inequalities is true:
     * <ul>
     * <li>{@code index < 0}</li>
     * <li>{@code index >= length}</li>
     * <li>{@code length < 0}, which is implied from the former inequalities</li>
     * </ul>
     *
     * @param index  the index
     * @param length the upper-bound (exclusive) of the range
     * @return {@code index} if it is within bounds of the range
     * @throws IndexOutOfBoundsException if the {@code index} is out of bounds
     */
    public static int checkIndex(int index, int length) {
        return Preconditions.checkIndex(index, length, null);
    }

    /**
     * Checks if the sub-range from {@code fromIndex} (inclusive) to
     * {@code toIndex} (exclusive) is within the bounds of range from {@code 0}
     * (inclusive) to {@code length} (exclusive).
     *
     * <p>The sub-range is defined to be out of bounds if any of the following
     * inequalities is true:
     * <ul>
     * <li>{@code fromIndex < 0}</li>
     * <li>{@code fromIndex > toIndex}</li>
     * <li>{@code toIndex > length}</li>
     * <li>{@code length < 0}, which is implied from the former inequalities</li>
     * </ul>
     *
     * @param fromIndex the lower-bound (inclusive) of the sub-range
     * @param toIndex   the upper-bound (exclusive) of the sub-range
     * @param length    the upper-bound (exclusive) the range
     * @return {@code fromIndex} if the sub-range within bounds of the range
     * @throws IndexOutOfBoundsException if the sub-range is out of bounds
     * @since 9
     */
    public static int checkFromToIndex(int fromIndex, int toIndex, int length) {
        return Preconditions.checkFromToIndex(fromIndex, toIndex, length, null);
    }

    /**
     * Checks if the sub-range from {@code fromIndex} (inclusive) to
     * {@code fromIndex + size} (exclusive) is within the bounds of range from
     * {@code 0} (inclusive) to {@code length} (exclusive).
     *
     * <p>The sub-range is defined to be out of bounds if any of the following
     * inequalities is true:
     * <ul>
     * <li>{@code fromIndex < 0}</li>
     * <li>{@code size < 0}</li>
     * <li>{@code fromIndex + size > length}, taking into account integer overflow</li>
     * <li>{@code length < 0}, which is implied from the former inequalities</li>
     * </ul>
     *
     * @param fromIndex the lower-bound (inclusive) of the sub-interval
     * @param size      the size of the sub-range
     * @param length    the upper-bound (exclusive) of the range
     * @return {@code fromIndex} if the sub-range within bounds of the range
     * @throws IndexOutOfBoundsException if the sub-range is out of bounds
     */
    public static int checkFromIndexSize(int fromIndex, int size, int length) {
        return Preconditions.checkFromIndexSize(fromIndex, size, length, null);
    }

    public static boolean isEmpty(Object o) {
        return Emptys.isEmpty(o);
    }

    public static boolean isNotEmpty(Object o) {
        return Emptys.isNotEmpty(o);
    }

    public static <T> int length(T object) {
        return Emptys.getLength(object);
    }

    public static Object[] swap(Object obj1,Object obj2){
        Object temp = obj1;
        obj1 = obj2;
        obj2 = temp;
        return new Object[]{obj1, obj2};
    }

    public static <T> T cast(Object o) {
        return (T)o;
    }
}
