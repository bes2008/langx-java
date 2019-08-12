package com.jn.langx.util.collect;

import com.jn.langx.Accessor;

public class StringMapAccessor implements Accessor<StringMap> {
    private StringMap map;

    @Override
    public void setTarget(StringMap target) {
        this.map = target;
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
}
