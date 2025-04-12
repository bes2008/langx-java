package com.jn.langx.util.function;

/**
 * Operator接口扩展了Function接口，用于定义操作类型的功能
 * 它表示一个可以接受一个参数并产生一个结果的函数
 * 主要用于在函数式编程中作为函数参数或返回值
 *
 * @param <V> 表示操作的输入和输出类型
 */
public interface Operator<V> extends Function<V, V> {
    @Override
    V apply(V input);
}

