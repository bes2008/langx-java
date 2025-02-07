package com.jn.langx.util.function;

/**
 * Represents a function that accepts one argument and produces a result.
 * This interface is similar to Java 8's Function interface, providing a typed functional interface
 * for use in functional programming scenarios, allowing the definition of a function that takes a single
 * parameter and returns a result.
 *
 * @param <I> The type of the input to the function
 * @param <O> The type of the result from the function
 */
public interface Function<I, O> {
    /**
     * Applies this function to the given argument.
     * This is the only method defined in the interface, used to implement the conversion from input to output.
     *
     * @param input The input to the function, cannot be null
     * @return The function's result
     */
    O apply(I input);
}
