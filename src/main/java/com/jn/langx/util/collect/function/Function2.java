package com.jn.langx.util.collect.function;

/**
 * Any function, it is similar to Java 8 Function.
 * It has two arguments
 * @param <I1> the first argument
 * @param <I2> the second argument
 * @param <O> the result
 * @see Function
 */
public interface Function2<I1, I2, O> {
    O apply(I1 input1, I2 input2);
}
