package com.jn.langx.util.collection;

import com.jn.langx.Accessor;
import com.jn.langx.exception.IllegalValueException;
import com.jn.langx.text.StringTemplates;

/**
 * A accessor for a StringMap
 *
 * @author jinuo.fang
 */
public class StringMapAccessor implements Accessor<String, StringMap> {
    private StringMap map;

    public StringMapAccessor() {
    }

    public StringMapAccessor(StringMap target) {
        this();
        setTarget(target);
    }

    @Override
    public void setTarget(StringMap target) {
        this.map = target;
    }

    @Override
    public StringMap getTarget() {
        return map;
    }

    @Override
    public Object get(String key) {
        return getString(key);
    }

    @Override
    public String getString(String key) {
        return getString(key, null);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return map.get(key);
    }


    @Override
    public Character getCharacter(String key) {
        return getCharacter(key, null);
    }

    @Override
    public Character getCharacter(String key, Character defaultValue) {
        String string = getString(key);
        if (string == null) {
            return defaultValue;
        }
        if (string.length() != 1) {
            throw new IllegalValueException(StringTemplates.formatWithoutIndex("'{}' is a string not a character", string));
        }
        return string.charAt(0);
    }

    @Override
    public Byte getByte(String key) {
        return getByte(key, null);
    }

    @Override
    public Byte getByte(String key, Byte defaultValue) {
        return Byte.parseByte(getString(key, "" + defaultValue));
    }

    @Override
    public Integer getInteger(String key) {
        return getInteger(key, 0);
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        return Integer.parseInt(getString(key, "" + defaultValue));
    }

    @Override
    public Short getShort(String key) {
        return getShort(key, Short.valueOf("" + 0));
    }

    @Override
    public Short getShort(String key, Short defaultValue) {
        return Short.parseShort(getString(key, "" + defaultValue));
    }

    @Override
    public Double getDouble(String key) {
        return getDouble(key, 0.0d);
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return Double.parseDouble(getString(key, "" + defaultValue));
    }

    @Override
    public Float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        return Float.parseFloat(getString(key, "" + defaultValue));
    }

    @Override
    public Long getLong(String key) {
        return getLong(key, 0L);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return Long.parseLong(getString(key, "" + defaultValue));
    }

    @Override
    public Boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return Boolean.parseBoolean(getString(key, "" + defaultValue));
    }


    @Override
    public void set(String key, Object value) {
        if (value == null) {
            map.remove(key);
        } else {
            map.put(key, value.toString());
        }
    }

    @Override
    public void setString(String key, String value) {
        set(key, value);
    }

    @Override
    public void setByte(String key, byte value) {
        set(key, value);
    }

    @Override
    public void setShort(String key, short value) {
        set(key, value);
    }

    @Override
    public void setInteger(String key, int value) {
        set(key, value);
    }

    @Override
    public void setLong(String key, long value) {
        set(key, value);
    }

    @Override
    public void setFloat(String key, float value) {
        set(key, value);
    }

    @Override
    public void setDouble(String key, double value) {
        set(key, value);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        set(key, value);
    }

    @Override
    public void setChar(String key, char value) {
        set(key, value);
    }
}
