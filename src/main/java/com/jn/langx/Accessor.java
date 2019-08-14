package com.jn.langx;

import com.jn.langx.util.collection.PropertiesAccessor;

/**
 * A object's accessor, use it, you can get value from T object;
 * <pre>
 * getXxx(String key): get the Xxx value association to specified key from target
 * getXxx(String key, Xxx default): get the Xxx value association to specified key from target, if can't find the key, return the specified default value
 * </pre>
 * @param <T> the target
 *
 * @see com.jn.langx.util.collection.StringMapAccessor
 * @see com.jn.langx.util.reflect.FieldAccessor
 * @see PropertiesAccessor
 * @see com.jn.langx.parser.HttpQueryStringAccessor
 */
public interface Accessor<T> {
    void setTarget(T target);
    T getTarget();

    Object get(String key);

    String getString(String key);

    String getString(String key, String defaultValue);

    Integer getInteger(String key);

    Integer getInteger(String key, Integer defaultValue);

    Short getShort(String key);

    Short getShort(String key, Short defaultValue);

    Double getDouble(String key);

    Double getDouble(String key, Double defaultValue);

    Float getFloat(String key);

    Float getFloat(String key, Float defaultValue);

    Long getLong(String key);

    Long getLong(String key, Long defaultValue);

    Boolean getBoolean(String key);

    Boolean getBoolean(String key, Boolean defaultValue);
}
