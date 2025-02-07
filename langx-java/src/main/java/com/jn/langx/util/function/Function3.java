package com.jn.langx.util.function;

/**
 * Represents a function that accepts three arguments (arg1, arg2, arg3) and produces a result.
 * This interface is used to define methods that take three parameters and return a value.
 *
 * @param <I1> The type of the first argument to the function.
 * @param <I2> The type of the second argument to the function.
 * @param <I3> The type of the third argument to the function.
 * @param <O>  The type of the result produced by the function.
 */
public interface Function3<I1,I2,I3,O> {

    /**
     * Applies this function to the specified arguments.
     *
     * @param arg1 The first argument.
     * @param arg2 The second argument.
     * @param arg3 The third argument.
     * @return The result produced by the function.
     */
    O apply(I1 arg1, I2 arg2, I3 arg3);
}
