package com.jn.langx;

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
    void setTarget(T target);

    T getTarget();

    Object get(K key);

    String getString(K key);

    String getString(K key, String defaultValue);

    Character getCharacter(K key);

    Character getCharacter(K key, Character defaultValue);

    Byte getByte(K key);

    Byte getByte(K key, Byte defaultValue);

    Short getShort(K key);

    Short getShort(K key, Short defaultValue);

    Integer getInteger(K key);

    Integer getInteger(K key, Integer defaultValue);

    Double getDouble(K key);

    Double getDouble(K key, Double defaultValue);

    Float getFloat(K key);

    Float getFloat(K key, Float defaultValue);

    Long getLong(K key);

    Long getLong(K key, Long defaultValue);

    Boolean getBoolean(K key);

    Boolean getBoolean(K key, Boolean defaultValue);

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
