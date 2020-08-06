package com.jn.langx;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.function.Function;

/**
 * A object's accessor, use it, you can get value from T object;
 * <pre>
 * getXxx(K key): get the Xxx value association to specified key from target
 * getXxx(K key, Xxx default): get the Xxx value association to specified key from target, if can't find the key, return the specified default value
 * </pre>
 *
 * @author jinuo.fang
 * @see com.jn.langx.util.collection.StringMapAccessor
 * @see com.jn.langx.util.reflect.FieldAccessor
 * @see com.jn.langx.text.properties.PropertiesAccessor
 * @see com.jn.langx.text.HttpQueryStringAccessor
 * @see com.jn.langx.util.reflect.ArrayAccessor
 */
public interface Accessor<K, T> {
    T getTarget();

    void setTarget(T target);

    boolean has(K key);

    Object get(K key);

    Object get(K key, @NonNull Function<Object, Object> mapper);

    String getString(K key);

    String getString(K key, String defaultValue);

    String getString(K key, @NonNull Function<Object, String> mapper);

    Character getCharacter(K key);

    Character getCharacter(K key, Character defaultValue);

    Character getCharacter(K key, @NonNull Function<Object, Character> mapper);

    Byte getByte(K key);

    Byte getByte(K key, Byte defaultValue);

    Byte getByte(K key, @NonNull Function<Object, Byte> mapper);

    Short getShort(K key);

    Short getShort(K key, Short defaultValue);

    Short getShort(K key, @NonNull Function<Object, Short> mapper);

    Integer getInteger(K key);

    Integer getInteger(K key, Integer defaultValue);

    Integer getInteger(K key, @NonNull Function<Object, Integer> mapper);

    Double getDouble(K key);

    Double getDouble(K key, Double defaultValue);

    Double getDouble(K key, @NonNull Function<Object, Double> mapper);

    Float getFloat(K key);

    Float getFloat(K key, Float defaultValue);

    Float getFloat(K key, @NonNull Function<Object, Float> mapper);

    Long getLong(K key);

    Long getLong(K key, Long defaultValue);

    Long getLong(K key, @NonNull Function<Object, Long> mapper);

    Boolean getBoolean(K key);

    Boolean getBoolean(K key, Boolean defaultValue);

    Boolean getBoolean(K key, @NonNull Function<Object, Boolean> mapper);

    void set(K key, Object value);

    void setString(K key, String value);

    void setByte(K key, byte value);

    void setShort(K key, short value);

    void setInteger(K key, int value);

    void setLong(K key, long value);

    void setFloat(K key, float value);

    void setDouble(K key, double value);

    void setBoolean(K key, boolean value);

    void setChar(K key, char value);
}
