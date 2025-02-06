package com.jn.langx.cache;

import java.util.Map;

/**
 * Loader接口定义了通过键(K)加载值(V)的标准化方法
 * 它主要用途是为那些需要根据键来加载或获取值的场景提供一个通用的接口
 * 通过实现这个接口，可以为特定的数据存储或数据源提供加载实现
 *
 * @param <K> 键的类型，用于指定加载时使用的键的类型
 * @param <V> 值的类型，用于指定加载成功后返回的值的类型
 */
public interface Loader<K, V> {
    /**
     * 根据给定的键加载值
     *
     * @param key 用于加载值的键，不应为null
     * @return 返回加载的值，如果找不到对应的值，则返回null
     *         这里不抛出异常，以允许调用者区分null值和不存在的值
     */
    V load(K key);

    /**
     * 批量获取多个键对应的值
     *
     * @param keys 一个包含多个键的Iterable对象，用于指定需要加载的值的键集合
     * @return 返回一个Map，其中包含所有成功加载的键值对
     *         如果某个键没有对应的值，则该键不会出现在返回的Map中
     *         这种设计允许高效地批量加载多个值，减少多次单独加载的开销
     */
    Map<K, V> getAll(Iterable<K> keys);
}
