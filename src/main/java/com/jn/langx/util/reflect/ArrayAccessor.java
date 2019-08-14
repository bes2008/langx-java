package com.jn.langx.util.reflect;

import com.jn.langx.Accessor;
import com.jn.langx.util.Arrs;
import com.jn.langx.util.Preconditions;

import java.lang.reflect.Array;

public class ArrayAccessor<E> implements Accessor<Integer, E> {
    private E target;

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
    public Integer getInteger(Integer index) {
        return Array.getInt(target, index);
    }

    @Override
    public Integer getInteger(Integer index, Integer defaultValue) {
        String str = getString(index, "" + defaultValue);
        return Integer.parseInt(str);
    }

    @Override
    public Short getShort(Integer index) {
        return Array.getShort(target, index);
    }

    @Override
    public Short getShort(Integer index, Short defaultValue) {
        String str = getString(index, "" + defaultValue);
        return Short.parseShort(str);
    }

    @Override
    public Double getDouble(Integer index) {
        return Array.getDouble(target, index);
    }

    @Override
    public Double getDouble(Integer index, Double defaultValue) {
        String str = getString(index, "" + defaultValue);
        return Double.parseDouble(str);
    }

    @Override
    public Float getFloat(Integer index) {
        return Array.getFloat(target, index);
    }

    @Override
    public Float getFloat(Integer index, Float defaultValue) {
        String str = getString(index, "" + defaultValue);
        return Float.parseFloat(str);
    }

    @Override
    public Long getLong(Integer index) {
        return Array.getLong(target, index);
    }

    @Override
    public Long getLong(Integer index, Long defaultValue) {
        String str = getString(index, "" + defaultValue);
        return Long.parseLong(str);
    }

    @Override
    public Boolean getBoolean(Integer index) {
        return Array.getBoolean(target, index);
    }

    @Override
    public Boolean getBoolean(Integer index, Boolean defaultValue) {
        String str = getString(index, "" + defaultValue);
        return Boolean.parseBoolean(str);
    }
}
