package com.jn.langx.util.function;

/**
 * @param <E> the element
 * @param <C> the container
 */
public interface Collector<E, C> {
    Supplier0<C> supplier();
    Consumer2<C, E> accumulator();
}
