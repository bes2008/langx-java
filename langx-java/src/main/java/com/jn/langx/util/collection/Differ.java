package com.jn.langx.util.collection;

/**
 * 定义一个差异比较接口，用于比较两个值之间的差异
 * 此接口旨在提供一个标准化的方法，以处理和表示两个对象之间的差异
 * 它可以用于各种类型的对象，只要这些对象实现了与此接口相关的逻辑
 *
 * @param <V> 值的类型，即需要进行比较的两个对象的类型
 * @param <R> 结果的类型，即表示比较结果的对象类型，必须是DiffResult的子类
 */
public interface Differ<V, R extends DiffResult> {
    /**
     * 比较两个值的差异
     * 此方法接受两个同类型的参数，一个是旧值，另一个是新值
     * 它们之间的差异将被计算并以R类型的结果返回
     *
     * @param oldValue 旧值，与newValue进行比较的对象
     * @param newValue 新值，与oldValue进行比较的对象
     * @return 返回一个R类型的对象，表示比较的结果
     */
    R diff(V oldValue, V newValue);
}
