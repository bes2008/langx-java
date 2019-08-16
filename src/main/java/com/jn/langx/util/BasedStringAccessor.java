package com.jn.langx.util;

import com.jn.langx.Accessor;
import com.jn.langx.exception.IllegalValueException;
import com.jn.langx.text.StringTemplates;

public abstract class BasedStringAccessor<K, T> implements Accessor<K, T> {
    protected T t;

    @Override
    public void setTarget(T target) {
        Preconditions.checkNotNull(t);
    }

    @Override
    public T getTarget() {
        return t;
    }

    @Override
    public abstract Object get(K key);

    @Override
    public String getString(K key) {
        return getString(key, null);
    }

    @Override
    public abstract String getString(K key, String defaultValue);

    @Override
    public Character getCharacter(K key) {
        return getCharacter(key, null);
    }

    @Override
    public Character getCharacter(K key, Character defaultValue) {
        String value = getString(key);
        if (value == null) {
            return defaultValue;
        }
        if (value.length() != 1) {
            throw new IllegalValueException(StringTemplates.formatWithoutIndex("'{}' is a string not a character", value));
        }
        return value.charAt(0);
    }

    @Override
    public Byte getByte(K key) {
        return getByte(key, null);
    }

    @Override
    public Byte getByte(K key, Byte defaultValue) {
        return Byte.parseByte(getString(key, "" + defaultValue));
    }

    @Override
    public Integer getInteger(K key) {
        return getInteger(key, 0);
    }

    @Override
    public Integer getInteger(K key, Integer defaultValue) {
        return Integer.parseInt(getString(key, "" + defaultValue));
    }

    @Override
    public Short getShort(K key) {
        return getShort(key, Short.valueOf("" + 0));
    }

    @Override
    public Short getShort(K key, Short defaultValue) {
        return Short.parseShort(getString(key, "" + defaultValue));
    }

    @Override
    public Double getDouble(K key) {
        return getDouble(key, 0.0d);
    }

    @Override
    public Double getDouble(K key, Double defaultValue) {
        return Double.parseDouble(getString(key, "" + defaultValue));
    }

    @Override
    public Float getFloat(K key) {
        return getFloat(key, 0.0f);
    }

    @Override
    public Float getFloat(K key, Float defaultValue) {
        return Float.parseFloat(getString(key, "" + defaultValue));
    }

    @Override
    public Long getLong(K key) {
        return getLong(key, 0L);
    }

    @Override
    public Long getLong(K key, Long defaultValue) {
        return Long.parseLong(getString(key, "" + defaultValue));
    }

    @Override
    public Boolean getBoolean(K key) {
        return getBoolean(key, false);
    }

    @Override
    public Boolean getBoolean(K key, Boolean defaultValue) {
        return Boolean.parseBoolean(getString(key, "" + defaultValue));
    }


    @Override
    public abstract void set(K key, Object value);

    @Override
    public void setString(K key, String value) {
        set(key, value);
    }

    @Override
    public void setByte(K key, byte value) {
        set(key, value);
    }

    @Override
    public void setShort(K key, short value) {
        set(key, value);
    }

    @Override
    public void setInteger(K key, int value) {
        set(key, value);
    }

    @Override
    public void setLong(K key, long value) {
        set(key, value);
    }

    @Override
    public void setFloat(K key, float value) {
        set(key, value);
    }

    @Override
    public void setDouble(K key, double value) {
        set(key, value);
    }

    @Override
    public void setBoolean(K key, boolean value) {
        set(key, value);
    }

    @Override
    public void setChar(K key, char value) {
        set(key, value);
    }
}
