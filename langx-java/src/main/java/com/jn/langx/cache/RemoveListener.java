package com.jn.langx.cache;

/**
 * 定义一个移除监听器接口，用于在键值对被移除时进行通知
 *
 * @param <K> 键的类型
 * @param <V> 值的类型
 */
public interface RemoveListener<K, V> {
    /**
     * 当键值对被移除时调用的方法
     *
     * @param key 被移除的键
     * @param value 被移除的值
     * @param cause 移除的原因，说明了移除操作的上下文
     */
    void onRemove(K key, V value, RemoveCause cause);
}
