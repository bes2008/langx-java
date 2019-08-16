package com.jn.langx.util.collection;

import com.jn.langx.util.BasedStringAccessor;

import java.util.Properties;

/**
 * @author jinuo.fang
 */
public class PropertiesAccessor extends BasedStringAccessor<String, Properties> {

    public PropertiesAccessor() {
    }

    public PropertiesAccessor(Properties properties) {
        setTarget(properties);
    }

    @Override
    public Object get(String key) {
        return getString(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getTarget().getProperty(key, defaultValue);
    }

    @Override
    public void set(String key, Object value) {
        if (value == null) {
            getTarget().remove(key);
            return;
        }
        getTarget().setProperty(key, value.toString());
    }

}
