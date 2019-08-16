package com.jn.langx.util.collection;

import com.jn.langx.util.BasedStringAccessor;

import java.util.Properties;

/**
 * @author jinuo.fang
 */
public class PropertiesAccessor extends BasedStringAccessor<String, Properties> {
    private Properties target;

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
        return target.getProperty(key, defaultValue);
    }

    @Override
    public void set(String key, Object value) {
        if (value == null) {
            target.remove(key);
            return;
        }
        target.setProperty(key, value.toString());
    }

}
