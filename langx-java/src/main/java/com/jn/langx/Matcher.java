package com.jn.langx;

/**
 * @since 4.7.6
 */
public interface Matcher<E,R> {
    R matches(E e);
}
