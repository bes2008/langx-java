package com.jn.langx.util.collection.diff;

/**
 * KeyBuilder接口用于定义从特定对象中提取或构建键的策略
 * 它提供了一种标准化的方式来获取对象的唯一键，这对于在需要唯一标识对象的场景中非常有用
 *
 * @param <K> 键的类型，表示期望返回的键的类型
 * @param <O> 对象的类型，表示输入的对象类型
 */
public interface KeyBuilder<K, O> {
    /**
     * 获取对象的键
     *
     * @param object 输入的对象，从这个对象中提取键
     * @return 返回从对象中提取的键
     */
    K getKey(O object);
}
