package com.jn.langx.util;

import com.jn.langx.Accessor;

import java.util.Properties;

public class PropertiesAccessor implements Accessor<Properties> {
    private Properties props;

    public PropertiesAccessor(Properties properties) {
        setTarget(properties);
    }

    @Override
    public Object get(String key) {
        return getString(key);
    }

    public String getString(String key) {
        return props.getProperty(key, "0");
    }

    public String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public Integer getInteger(String key) {
        return getInteger(key, 0);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return Integer.parseInt(getString(key, "" + defaultValue));
    }

    public Short getShort(String key) {
        return getShort(key, Short.valueOf("" + 0));
    }

    public Short getShort(String key, Short defaultValue) {
        return Short.parseShort(getString(key, "" + defaultValue));
    }

    public Double getDouble(String key) {
        return getDouble(key, 0.0d);
    }

    public Double getDouble(String key, Double defaultValue) {
        return Double.parseDouble(getString(key, "" + defaultValue));
    }

    public Float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public Float getFloat(String key, Float defaultValue) {
        return Float.parseFloat(getString(key, "" + defaultValue));
    }

    public Long getLong(String key) {
        return getLong(key, 0L);
    }

    public Long getLong(String key, Long defaultValue) {
        return Long.parseLong(getString(key, "" + defaultValue));
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return Boolean.parseBoolean(getString(key, "" + defaultValue));
    }

    @Override
    public void setTarget(Properties target) {
        this.props = target;
    }
}
