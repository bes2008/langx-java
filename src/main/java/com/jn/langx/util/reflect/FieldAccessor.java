package com.jn.langx.util.reflect;

import com.jn.langx.util.Accessor;

public class FieldAccessor implements Accessor<Object> {
    private Object target;

    public FieldAccessor(Object target) {
        setTarget(target);
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    private <V> V getFieldValue(String fieldName, V defaultValue) {
        try {
            return (V) Reflects.getAnyFieldValue(target, fieldName, true, true);
        } catch (NoSuchFieldException ex) {
            return defaultValue;
        } catch (IllegalAccessException ex) {
            return defaultValue;
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


}
