package com.jn.langx.util.function;

/**
 * A factory that is similar to Java 8 Supplier.
 *
 * @param <I> the argument
 * @param <O> the result
 */
public interface Supplier<I, O> {
    /**
     * Gets the result.
     *
     * @param input the input argument
     * @return the result
     */
    O get(I input);
}

