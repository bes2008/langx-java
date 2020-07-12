/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Supplier;

import java.util.List;

public class Preconditions {
    private Preconditions() {
        throw new UnsupportedOperationException();
    }

    public static <T> T test(@NonNull Predicate<T> predicate, @Nullable T argument) {
        return test(predicate, argument, null);
    }

    public static <T> T test(@NonNull Predicate<T> predicate, @Nullable T argument, String message) {
        if (predicate.test(argument)) {
            return argument;
        }
        if (Strings.isNotEmpty(message)) {
            throw new IllegalArgumentException(message);
        } else {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("Illegal argument: {}", argument));
        }
    }

    public static <T> T test(@NonNull Predicate<T> predicate, @Nullable T argument, Supplier<Object[], String> messageSupplier, Object... params) {
        if (predicate.test(argument)) {
            return argument;
        }
        if (Objects.isNull(messageSupplier)) {
            throw new IllegalArgumentException();
        } else {
            throw new IllegalArgumentException(messageSupplier.get(params));
        }
    }

    public static <T> T checkNotNull(@NonNull T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static <T> T checkNotNull(@NonNull T obj, @Nullable String errorMessage) {
        if (obj == null) {
            if (errorMessage == null) {
                throw new NullPointerException();
            }
            throw new NullPointerException(errorMessage);
        }
        return obj;
    }

    public static <T> T checkNotNull(@NonNull T obj, @Nullable Supplier<Object[], String> errorMessageSupplier, Object... params) {
        if (obj == null) {
            if (errorMessageSupplier == null) {
                throw new NullPointerException();
            }
            String errorMessage = errorMessageSupplier.get(params);
            if (errorMessage == null) {
                throw new NullPointerException();
            }
            throw new NullPointerException(errorMessage);
        }
        return obj;
    }

    public static <T> T checkNotEmpty(@Nullable T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static <T> T checkNotEmpty(@NonNull T obj, @Nullable String errorMessage) {
        if (Emptys.isEmpty(obj)) {
            if (errorMessage == null) {
                throw new NullPointerException();
            }
            throw new NullPointerException(errorMessage);
        }
        return obj;
    }

    public static <T> T checkNotEmpty(@NonNull T obj, @Nullable Supplier<Object[], String> errorMessageSupplier, Object... params) {
        if (Emptys.isEmpty(obj)) {
            if (Objects.isNull(errorMessageSupplier)) {
                throw new NullPointerException();
            }
            throw new NullPointerException(errorMessageSupplier.get(params));
        }
        return obj;
    }

    /** Ensures that the state expression is true. */
    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }
    
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }


    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            if (Emptys.isEmpty(errorMessage)) {
                throw new IllegalArgumentException();
            } else {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void checkArgument(boolean expression, Supplier<Object[], String> errorMessageSupplier, Object... params) {
        if (!expression) {
            if (Objects.isNull(errorMessageSupplier)) {
                throw new IllegalArgumentException();
            } else {
                throw new IllegalArgumentException(errorMessageSupplier.get(params));
            }
        }
    }

    public static void checkTrue(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }


    public static void checkTrue(boolean expression, String errorMessage) {
        if (!expression) {
            if (Emptys.isEmpty(errorMessage)) {
                throw new IllegalArgumentException();
            } else {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static void checkTrue(boolean expression, Supplier<Object[], String> errorMessageSupplier, Object... params) {
        if (!expression) {
            if (Objects.isNull(errorMessageSupplier)) {
                throw new IllegalArgumentException();
            } else {
                throw new IllegalArgumentException(errorMessageSupplier.get(params));
            }
        }
    }

    /**
     * Maps out-of-bounds values to a runtime exception.
     *
     * @param checkKind the kind of bounds check, whose name may correspond
     *                  to the name of one of the range check methods, checkIndex,
     *                  checkFromToIndex, checkFromIndexSize
     * @param args      the out-of-bounds arguments that failed the range check.
     *                  If the checkKind corresponds a the name of a range check method
     *                  then the bounds arguments are those that can be passed in order
     *                  to the method.
     * @param oobef     the exception formatter that when applied with a checkKind
     *                  and a list out-of-bounds arguments returns a runtime exception.
     *                  If {@code null} then, it is as if an exception formatter was
     *                  supplied that returns {@link IndexOutOfBoundsException} for any
     *                  given arguments.
     * @return the runtime exception
     */
    private static RuntimeException outOfBounds(Function2<String, List<Integer>, ? extends RuntimeException> oobef, String checkKind, Integer... args) {
        List<Integer> largs = Collects.asList(args);
        RuntimeException e = oobef == null ? null : oobef.apply(checkKind, largs);
        return e == null ? new IndexOutOfBoundsException(outOfBoundsMessage(checkKind, largs)) : e;
    }

    private static RuntimeException outOfBoundsCheckIndex(Function2<String, List<Integer>, ? extends RuntimeException> oobe, int index, int length) {
        return outOfBounds(oobe, "checkIndex", index, length);
    }

    private static RuntimeException outOfBoundsCheckFromToIndex(Function2<String, List<Integer>, ? extends RuntimeException> oobe, int fromIndex, int toIndex, int length) {
        return outOfBounds(oobe, "checkFromToIndex", fromIndex, toIndex, length);
    }

    private static RuntimeException outOfBoundsCheckFromIndexSize(Function2<String, List<Integer>, ? extends RuntimeException> oobe, int fromIndex, int size, int length) {
        return outOfBounds(oobe, "checkFromIndexSize", fromIndex, size, length);
    }

    /**
     * Returns an out-of-bounds exception formatter from an given exception
     * factory.  The exception formatter is a function that formats an
     * out-of-bounds message from its arguments and applies that message to the
     * given exception factory to produce and relay an exception.
     *
     * <p>The exception formatter accepts two arguments: a {@code String}
     * describing the out-of-bounds range check that failed, referred to as the
     * <em>check kind</em>; and a {@code List<Integer>} containing the
     * out-of-bound integer values that failed the check.  The list of
     * out-of-bound values is not modified.
     *
     * <p>Three check kinds are supported {@code checkIndex},
     * {@code checkFromToIndex} and {@code checkFromIndexSize} corresponding
     * respectively to the specified application of an exception formatter as an
     * argument to the out-of-bounds range check methods
     * {@link #checkIndex(int, int, Function2)} checkIndex},
     * {@link #checkFromToIndex(int, int, int, Function2) checkFromToIndex}, and
     * {@link #checkFromIndexSize(int, int, int, Function2) checkFromIndexSize}.
     * Thus a supported check kind corresponds to a method name and the
     * out-of-bound integer values correspond to method argument values, in
     * order, preceding the exception formatter argument (similar in many
     * respects to the form of arguments required for a reflective invocation of
     * such a range check method).
     *
     * <p>Formatter arguments conforming to such supported check kinds will
     * produce specific exception messages describing failed out-of-bounds
     * checks.  Otherwise, more generic exception messages will be produced in
     * any of the following cases: the check kind is supported but fewer
     * or more out-of-bounds values are supplied, the check kind is not
     * supported, the check kind is {@code null}, or the list of out-of-bound
     * values is {@code null}.
     * <p>
     * This method produces an out-of-bounds exception formatter that can be
     * passed as an argument to any of the supported out-of-bounds range check
     * methods declared by {@code Objects}.  For example, a formatter producing
     * an {@code ArrayIndexOutOfBoundsException} may be produced and stored on a
     * {@code static final} field as follows:
     * <pre>{@code
     * static final
     * BiFunction<String, List<Integer>, ArrayIndexOutOfBoundsException> AIOOBEF =
     *     outOfBoundsExceptionFormatter(ArrayIndexOutOfBoundsException::new);
     * }</pre>
     * The formatter instance {@code AIOOBEF} may be passed as an argument to an
     * out-of-bounds range check method, such as checking if an {@code index}
     * is within the bounds of a {@code limit}:
     * <pre>{@code
     * checkIndex(index, limit, AIOOBEF);
     * }</pre>
     * If the bounds check fails then the range check method will throw an
     * {@code ArrayIndexOutOfBoundsException} with an appropriate exception
     * message that is a produced from {@code AIOOBEF} as follows:
     * <pre>{@code
     * AIOOBEF.apply("checkIndex", List.of(index, limit));
     * }</pre>
     *
     * @param f   the exception factory, that produces an exception from a message
     *            where the message is produced and formatted by the returned
     *            exception formatter.  If this factory is stateless and side-effect
     *            free then so is the returned formatter.
     *            Exceptions thrown by the factory are relayed to the caller
     *            of the returned formatter.
     * @param <X> the type of runtime exception to be returned by the given
     *            exception factory and relayed by the exception formatter
     * @return the out-of-bounds exception formatter
     */
    public static <X extends RuntimeException> Function2<String, List<Integer>, X> outOfBoundsExceptionFormatter(final Function<String, X> f) {
        // Use anonymous class to avoid bootstrap issues if this method is
        // used early in startup
        return new Function2<String, List<Integer>, X>() {
            @Override
            public X apply(String checkKind, List<Integer> args) {
                return f.apply(outOfBoundsMessage(checkKind, args));
            }
        };
    }

    private static String outOfBoundsMessage(String checkKind, List<Integer> args) {
        if (checkKind == null && args == null) {
            return String.format("Range check failed");
        } else if (checkKind == null) {
            return String.format("Range check failed: %s", args);
        } else if (args == null) {
            return String.format("Range check failed: %s", checkKind);
        }

        int argSize = 0;
        if ("checkIndex".equals(checkKind)) {
            argSize = 2;
        } else {
            if ("checkFromToIndex".equals(checkKind) || "checkFromIndexSize".equals(checkKind)) {
                argSize = 3;
            }
        }

        // Switch to default if fewer or more arguments than required are supplied
        checkKind = (args.size() != argSize) ? "" : checkKind;
        if ("checkIndex".equals(checkKind)) {
            return String.format("Index %d out of bounds for length %d",
                    args.get(0), args.get(1));
        }
        if ("checkFromToIndex".equals(checkKind)) {
            return String.format("Range [%d, %d) out of bounds for length %d",
                    args.get(0), args.get(1), args.get(2));
        }
        if ("checkFromIndexSize".equals(checkKind)) {
            return String.format("Range [%d, %<d + %d) out of bounds for length %d",
                    args.get(0), args.get(1), args.get(2));
        }
        return String.format("Range check failed: %s %s", checkKind, args);
    }

    public static <X extends RuntimeException> int checkIndex(int index, int length) {
        if (index < 0 || index >= length)
            throw new IndexOutOfBoundsException(StringTemplates.formatWithPlaceholder("index {} out of bounds, the length is: {}", index, length));
        return index;
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
     * <p>If the {@code index} is out of bounds, then a runtime exception is
     * thrown that is the result of applying the following arguments to the
     * exception formatter: the name of this method, {@code checkIndex};
     * and an unmodifiable list integers whose values are, in order, the
     * out-of-bounds arguments {@code index} and {@code length}.
     *
     * @param <X>    the type of runtime exception to throw if the arguments are
     *               out of bounds
     * @param index  the index
     * @param length the upper-bound (exclusive) of the range
     * @param oobef  the exception formatter that when applied with this
     *               method name and out-of-bounds arguments returns a runtime
     *               exception.  If {@code null} or returns {@code null} then, it is as
     *               if an exception formatter produced from an invocation of
     *               {@code outOfBoundsExceptionFormatter(IndexOutOfBounds::new)} is used
     *               instead (though it may be more efficient).
     *               Exceptions thrown by the formatter are relayed to the caller.
     * @return {@code index} if it is within bounds of the range
     * @throws X                         if the {@code index} is out of bounds and the exception
     *                                   formatter is non-{@code null}
     * @throws IndexOutOfBoundsException if the {@code index} is out of bounds
     *                                   and the exception formatter is {@code null}
     *                                   Note: This method is made intrinsic in optimizing compilers to guide them to
     *                                   perform unsigned comparisons of the index and length when it is known the
     *                                   length is a non-negative value (such as that of an array length or from
     *                                   the upper bound of a loop)
     */
    public static <X extends RuntimeException> int checkIndex(int index, int length, Function2<String, List<Integer>, X> oobef) {
        if (index < 0 || index >= length)
            throw outOfBoundsCheckIndex(oobef, index, length);
        return index;
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
     * <p>If the sub-range is out of bounds, then a runtime exception is
     * thrown that is the result of applying the following arguments to the
     * exception formatter: the name of this method, {@code checkFromToIndex};
     * and an unmodifiable list integers whose values are, in order, the
     * out-of-bounds arguments {@code fromIndex}, {@code toIndex}, and {@code length}.
     *
     * @param <X>       the type of runtime exception to throw if the arguments are
     *                  out of bounds
     * @param fromIndex the lower-bound (inclusive) of the sub-range
     * @param toIndex   the upper-bound (exclusive) of the sub-range
     * @param length    the upper-bound (exclusive) the range
     * @param oobef     the exception formatter that when applied with this
     *                  method name and out-of-bounds arguments returns a runtime
     *                  exception.  If {@code null} or returns {@code null} then, it is as
     *                  if an exception formatter produced from an invocation of
     *                  {@code outOfBoundsExceptionFormatter(IndexOutOfBounds::new)} is used
     *                  instead (though it may be more efficient).
     *                  Exceptions thrown by the formatter are relayed to the caller.
     * @return {@code fromIndex} if the sub-range within bounds of the range
     * @throws X                         if the sub-range is out of bounds and the exception factory
     *                                   function is non-{@code null}
     * @throws IndexOutOfBoundsException if the sub-range is out of bounds and
     *                                   the exception factory function is {@code null}
     */
    public static <X extends RuntimeException> int checkFromToIndex(int fromIndex, int toIndex, int length, Function2<String, List<Integer>, X> oobef) {
        if (fromIndex < 0 || fromIndex > toIndex || toIndex > length)
            throw outOfBoundsCheckFromToIndex(oobef, fromIndex, toIndex, length);
        return fromIndex;
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
     * <p>If the sub-range is out of bounds, then a runtime exception is
     * thrown that is the result of applying the following arguments to the
     * exception formatter: the name of this method, {@code checkFromIndexSize};
     * and an unmodifiable list integers whose values are, in order, the
     * out-of-bounds arguments {@code fromIndex}, {@code size}, and
     * {@code length}.
     *
     * @param <X>       the type of runtime exception to throw if the arguments are
     *                  out of bounds
     * @param fromIndex the lower-bound (inclusive) of the sub-interval
     * @param size      the size of the sub-range
     * @param length    the upper-bound (exclusive) of the range
     * @param oobef     the exception formatter that when applied with this
     *                  method name and out-of-bounds arguments returns a runtime
     *                  exception.  If {@code null} or returns {@code null} then, it is as
     *                  if an exception formatter produced from an invocation of
     *                  {@code outOfBoundsExceptionFormatter(IndexOutOfBounds::new)} is used
     *                  instead (though it may be more efficient).
     *                  Exceptions thrown by the formatter are relayed to the caller.
     * @return {@code fromIndex} if the sub-range within bounds of the range
     * @throws X                         if the sub-range is out of bounds and the exception factory
     *                                   function is non-{@code null}
     * @throws IndexOutOfBoundsException if the sub-range is out of bounds and
     *                                   the exception factory function is {@code null}
     */
    public static <X extends RuntimeException> int checkFromIndexSize(int fromIndex, int size, int length, Function2<String, List<Integer>, X> oobef) {
        if ((length | fromIndex | size) < 0 || size > length - fromIndex)
            throw outOfBoundsCheckFromIndexSize(oobef, fromIndex, size, length);
        return fromIndex;
    }

}
