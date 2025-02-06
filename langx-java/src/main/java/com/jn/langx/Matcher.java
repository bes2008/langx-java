package com.jn.langx;

/**
 * Matcher接口定义了一个用于匹配特定类型对象的通用方法。
 * 它允许实现者提供自定义的匹配逻辑，并返回匹配结果。
 *
 * @param <E> 要匹配的对象的类型。
 * @param <R> 匹配操作结果的类型。
 *
 * @since 4.7.6
 */
public interface Matcher<E,R> {
    /**
     * 尝试匹配给定的对象。
     *
     * @param e 要匹配的对象。
     * @return 匹配操作的结果。
     */
    R matches(E e);
}
