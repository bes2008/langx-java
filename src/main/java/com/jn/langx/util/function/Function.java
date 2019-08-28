package com.jn.langx.util.function;

/**
 * Any function, it is similar to Java 8 Function.
 * It has only one argument
 *
 * @see Function2
 */
public interface Function<I, O> {
    O apply(I input);
}
