package com.jn.langx;

/**
 * Get value from T object;
 * <p>
 * getXxx(String key): get the Xxx value association to specified key from target
 * getXxx(String key, Xxx default): get the Xxx value association to specified key from target, if can't find the key, return the specified default value
 *
 * @param <T> the target
 */
public interface Accessor<T> {
    void setTarget(T target);

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
