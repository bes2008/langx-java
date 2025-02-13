package com.jn.langx.util.io;

/**
 * WritableComparable接口继承自Writable和Comparable接口，用于定义可以写入且可以比较的对象
 * 该接口主要用于Hadoop框架中，以实现数据的序列化/反序列化以及排序功能
 *
 * @param <T> 可比较对象的类型，通常用于泛型实现，以确保类型安全
 */
public interface WritableComparable<T> extends Writable, Comparable<T> {
    /**
     * 比较此对象与指定对象的顺序
     *
     * @param o 要比较的对象，类型为T
     * @return 整数值，表示当前对象与指定对象的顺序关系如果当前对象小于、等于或大于指定对象，则返回负数、零或正数
     * @see Comparable#compareTo(Object)
     */
    @Override
    int compareTo(T o);
}
