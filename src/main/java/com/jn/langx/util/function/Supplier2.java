package com.jn.langx.util.function;

/**
 * A factory that is similar to Java 8 Supplier.
 * @param <I1> the first argument
 * @param <I2> the second argument
 * @param <O> the result
 */
public interface Supplier2<I1, I2, O> {
    O get(I1 input1, I2 input2);
}
