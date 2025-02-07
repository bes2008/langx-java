package com.jn.langx.util.function;

/**
 * Consume a data, it is similar to Java 8 Consumer.
 * It has only one argument
 */
public interface Consumer<E> {
    /**
     * Performs the operation on the specified object.
     * This interface represents an operation that accepts a single input argument and returns no result.
     *
     * @param e the input argument to be consumed, which can be of any type E
     */
    void accept(E e);
}
