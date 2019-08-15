package com.jn.langx.util.reflect;

import com.jn.langx.Accessor;

/**
 * A field accessor based on reflect
 *
 * @author jinuo.fang
 */
public class FieldAccessor implements Accessor<String, Object> {
    private Object target;

    public FieldAccessor(Object target) {
        setTarget(target);
    }

    @Override
    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    private <V> V getFieldValue(String fieldName, V defaultValue) {
        V v;
        try {
            v = (V) Reflects.getAnyFieldValue(target, fieldName, true, true);
            if (v == null) {
                v = defaultValue;
            }
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        return v;
    }

    private <V> void setFieldValue(String fieldName, V value) {
        try {
            Reflects.setAnyFieldValue(target, fieldName, value, true, false);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Object get(String field) {
        return getFieldValue(field, null);
    }

    @Override
    public String getString(String field) {
        return getString(field, null);
    }


    @Override
    public String getString(String field, String defaultValue) {
        return getFieldValue(field, defaultValue);
    }

    @Override
    public Character getCharacter(String field) {
        return getCharacter(field, null);
    }

    @Override
    public Character getCharacter(String field, Character defaultValue) {
        return getFieldValue(field, defaultValue);
    }

    @Override
    public Byte getByte(String field) {
        return getByte(field, null);
    }

    @Override
    public Byte getByte(String field, Byte defaultValue) {
        return getFieldValue(field, defaultValue);
    }

    @Override
    public Integer getInteger(String field) {
        return getInteger(field, null);
    }

    @Override
    public Integer getInteger(String field, Integer defaultValue) {
        return getFieldValue(field, defaultValue);
    }

    @Override
    public Short getShort(String field) {
        return getShort(field, null);
    }

    @Override
    public Short getShort(String field, Short defaultValue) {
        return getFieldValue(field, defaultValue);
    }

    @Override
    public Double getDouble(String field) {
        return getDouble(field, null);
    }

    @Override
    public Double getDouble(String field, Double defaultValue) {
        return getFieldValue(field, defaultValue);
    }

    @Override
    public Float getFloat(String field) {
        return getFloat(field, null);
    }

    @Override
    public Float getFloat(String field, Float defaultValue) {
        return getFieldValue(field, defaultValue);
    }

    @Override
    public Long getLong(String field) {
        return getLong(field, null);
    }

    @Override
    public Long getLong(String field, Long defaultValue) {
        return getFieldValue(field, defaultValue);
    }

    @Override
    public Boolean getBoolean(String field) {
        return getBoolean(field, null);
    }

    @Override
    public Boolean getBoolean(String field, Boolean defaultValue) {
        return getFieldValue(field, defaultValue);
    }


    @Override
    public void set(String field, Object value) {
        setFieldValue(field, value);
    }

    @Override
    public void setString(String field, String value) {
        set(field, value);
    }

    @Override
    public void setChar(String field, char value) {
        set(field, value);
    }

    @Override
    public void setByte(String field, byte value) {
        set(field, value);
    }

    @Override
    public void setShort(String field, short value) {
        set(field, value);
    }

    @Override
    public void setInteger(String field, int value) {
        set(field, value);
    }

    @Override
    public void setLong(String field, long value) {
        set(field, value);
    }

    @Override
    public void setFloat(String field, float value) {
        set(field, value);
    }

    @Override
    public void setDouble(String field, double value) {
        set(field, value);
    }

    @Override
    public void setBoolean(String field, boolean value) {
        set(field, value);
    }

}
