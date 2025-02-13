package com.jn.langx.util.function;

/**
 * Represents a function that takes two arguments and produces a result.
 * This interface is similar to the Function interface in Java 8, but accepts two parameters instead of one.
 *
 * @param <I1> the type of the first argument
 * @param <I2> the type of the second argument
 * @param <O>  the type of the result
 *
 * This interface is primarily used to represent operations that take two input arguments and return a result.
 * It is a functional interface (callback interface), which can be used as the assignment target for lambda expressions or method references.
 *
 * @see java.util.function.Function
 */
public interface Function2<I1, I2, O> {
    /**
     * Applies this function to the given arguments.
     *
     * @param input1 the first argument
     * @param input2 the second argument
     * @return the function result
     */
    O apply(I1 input1, I2 input2);
}
