package com.jn.langx.util.reflect;

import com.jn.langx.util.BasedStringAccessor;

/**
 * A field accessor based on reflect
 *
 * @author jinuo.fang
 */
public class FieldAccessor extends BasedStringAccessor<String, Object> {
    private Object target;

    public FieldAccessor(Object target) {
        setTarget(target);
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
    public String getString(String key, String defaultValue) {
        Object value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    @Override
    public void set(String field, Object value) {
        setFieldValue(field, value);
    }

    @Override
    public void setString(String field, String value) {
        set(field, value);
    }


}
