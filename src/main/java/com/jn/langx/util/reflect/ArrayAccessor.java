package com.jn.langx.util.reflect;

import com.jn.langx.Accessor;
import com.jn.langx.exception.IllegalValueException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Arrs;
import com.jn.langx.util.Preconditions;

import java.lang.reflect.Array;

/**
 * An array accessor
 *
 * @param <E> an array
 * @author jinuo.fang
 */
public class ArrayAccessor<E> implements Accessor<Integer, E> {
    private E target;

    @Override
    public void setTarget(E target) {
        Preconditions.checkNotNull(target);
        Preconditions.checkArgument(Arrs.isArray(target));
        this.target = target;
    }

    @Override
    public E getTarget() {
        return target;
    }

    @Override
    public Object get(Integer index) {
        return Array.get(target, index);
    }

    @Override
    public String getString(Integer index) {
        return getString(index, null);
    }

    @Override
    public String getString(Integer index, String defaultValue) {
        Object o = get(index);
        return o == null ? defaultValue : o.toString();
    }

    @Override
    public Character getCharacter(Integer index) {
        return Array.getChar(target, index);
    }

    @Override
    public Character getCharacter(Integer index, Character defaultValue) {
        String str = getString(index, "" + defaultValue);
        if (str == null) {
            return defaultValue;
        }

        if (str.length() != 1) {
            throw new IllegalValueException(StringTemplates.formatWithoutIndex("'{}' is a string not a character", str));
        }

        return str.charAt(0);
    }

    @Override
    public Byte getByte(Integer index) {
        return Array.getByte(target, index);
    }

    @Override
    public Byte getByte(Integer index, Byte defaultValue) {
        return Byte.parseByte(getString(index, "" + defaultValue));
    }

    @Override
    public Integer getInteger(Integer index) {
        return Array.getInt(target, index);
    }

    @Override
    public Integer getInteger(Integer index, Integer defaultValue) {
        return Integer.parseInt(getString(index, "" + defaultValue));
    }

    @Override
    public Short getShort(Integer index) {
        return Array.getShort(target, index);
    }

    @Override
    public Short getShort(Integer index, Short defaultValue) {
        return Short.parseShort(getString(index, "" + defaultValue));
    }

    @Override
    public Double getDouble(Integer index) {
        return Array.getDouble(target, index);
    }

    @Override
    public Double getDouble(Integer index, Double defaultValue) {
        return Double.parseDouble(getString(index, "" + defaultValue));
    }

    @Override
    public Float getFloat(Integer index) {
        return Array.getFloat(target, index);
    }

    @Override
    public Float getFloat(Integer index, Float defaultValue) {
        return Float.parseFloat(getString(index, "" + defaultValue));
    }

    @Override
    public Long getLong(Integer index) {
        return Array.getLong(target, index);
    }

    @Override
    public Long getLong(Integer index, Long defaultValue) {
        return Long.parseLong(getString(index, "" + defaultValue));
    }

    @Override
    public Boolean getBoolean(Integer index) {
        return Array.getBoolean(target, index);
    }

    @Override
    public Boolean getBoolean(Integer index, Boolean defaultValue) {
        return Boolean.parseBoolean(getString(index, "" + defaultValue));
    }


    @Override
    public void set(Integer index, Object value) {
        Array.set(target, index, value);
    }

    @Override
    public void setString(Integer index, String value) {
        Array.set(target, index, value);
    }

    @Override
    public void setByte(Integer index, byte value) {
        Array.setByte(target, index, value);
    }

    @Override
    public void setShort(Integer index, short value) {
        Array.setShort(target, index, value);
    }

    @Override
    public void setInteger(Integer index, int value) {
        Array.setInt(target, index, value);
    }

    @Override
    public void setLong(Integer index, long value) {
        Array.setLong(target, index, value);
    }

    @Override
    public void setFloat(Integer index, float value) {
        Array.setFloat(target, index, value);
    }

    @Override
    public void setDouble(Integer index, double value) {
        Array.setDouble(target, index, value);
    }

    @Override
    public void setBoolean(Integer index, boolean value) {
        Array.setBoolean(target, index, value);
    }

    @Override
    public void setChar(Integer index, char value) {
        Array.setChar(target, index, value);
    }
}
