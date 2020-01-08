package com.jn.langx.util.function;

/**
 * @param <E> the element in a will be iterate
 * @param <C> the container will be return, also te container will be fill
 */
public interface Collector<E, C> {
    Supplier0<C> supplier();

    Consumer2<C, E> accumulator();
}
