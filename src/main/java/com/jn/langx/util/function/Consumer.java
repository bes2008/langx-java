package com.jn.langx.util.function;

/**
 * Consume a data, it is similar to Java 8 Consumer.
 * It has only one argument
 */
public interface Consumer<E> {
    void accept(E e);
}
