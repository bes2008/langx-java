package com.jn.langx.util.reflect;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.BasedStringAccessor;

/**
 * A field accessor based on reflect
 *
 * @author jinuo.fang
 */
public class FieldAccessor extends BasedStringAccessor<String, Object> {

    public FieldAccessor(@NonNull Object target) {
        setTarget(target);
    }


    private <V> V getFieldValue(String fieldName, V defaultValue) {
        V v;
        v = Reflects.<V>getAnyFieldValue(getTarget(), fieldName, true, true);
        if (v == null) {
            v = defaultValue;
        }
        return v;
    }

    private <V> void setFieldValue(String fieldName, V value) {
        Reflects.setAnyFieldValue(getTarget(), fieldName, value, true, false);
    }

    @Override
    public Object get(String field) {
        return getFieldValue(field, null);
    }

    @Override
    public boolean has(String key) {
        return Reflects.getAnyField(getTarget().getClass(), key) != null;
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
