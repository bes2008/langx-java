package com.jn.langx;

import com.jn.langx.util.collection.PropertiesAccessor;

/**
 * A object's accessor, use it, you can get value from T object;
 * <pre>
 * getXxx(K key): get the Xxx value association to specified key from target
 * getXxx(K key, Xxx default): get the Xxx value association to specified key from target, if can't find the key, return the specified default value
 * </pre>
 * @param <T> the target
 *
 * @see com.jn.langx.util.collection.StringMapAccessor
 * @see com.jn.langx.util.reflect.FieldAccessor
 * @see PropertiesAccessor
 * @see com.jn.langx.parser.HttpQueryStringAccessor
 */
public interface Accessor<K, T> {
    void setTarget(T target);
    T getTarget();

    Object get(K key);

    String getString(K key);

    String getString(K key, String defaultValue);

    Integer getInteger(K key);

    Integer getInteger(K key, Integer defaultValue);

    Short getShort(K key);

    Short getShort(K key, Short defaultValue);

    Double getDouble(K key);

    Double getDouble(K key, Double defaultValue);

    Float getFloat(K key);

    Float getFloat(K key, Float defaultValue);

    Long getLong(K key);

    Long getLong(K key, Long defaultValue);

    Boolean getBoolean(K key);

    Boolean getBoolean(K key, Boolean defaultValue);
}
