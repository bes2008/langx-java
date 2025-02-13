package com.jn.langx.util.function;
/**
 * The Collector interface represents a reduction operation that can accumulate elements of a stream into a container.
 * This interface is similar to the Reducer interface, but it accumulates elements into a mutable result container.
 * For example, accumulating elements into a List or array.
 *
 * @param <E> the element in a will be iterate
 * @param <C> the container will be return, also te container will be fill
 */
public interface Collector<E, C> {
    /**
     * Provides a function to create an empty result container.
     *
     * @return A supplier function that returns an empty result container.
     */
    Supplier0<C> supplier();

    /**
     * Provides a function to accumulate elements into the result container.
     *
     * @return A consumer function that accumulates a single element into the result container.
     */
    Consumer2<C, E> accumulator();
}
