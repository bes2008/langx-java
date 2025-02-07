package com.jn.langx.util.function.supplier;

/**
 * The LongSupplier interface should be used to provide a supplier of long-valued results.
 * Use cases for this interface include situations where long values need to be generated,
 * such as generating sequences of long values or providing long values under specific conditions.
 */
public interface LongSupplier {

    /**
     * Gets a result.
     *
     * @return a result
     */
    long getAsLong();
}
