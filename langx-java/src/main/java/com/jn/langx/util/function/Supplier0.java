package com.jn.langx.util.function;

/**
 * A factory that is similar to Java 8 Supplier.
 *
 * @param <O> the result
 */
public interface Supplier0<O> {
    /**
     * Gets the result of the supplier.
     *
     * @return the result
     */
    O get();
}
