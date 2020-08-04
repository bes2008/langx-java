package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.BasedStringAccessor;

/**
 * A accessor for a StringMap
 *
 * @author jinuo.fang
 */
public class StringMapAccessor extends BasedStringAccessor<String, StringMap> {

    public StringMapAccessor() {
    }

    public StringMapAccessor(@NonNull StringMap target) {
        this();
        setTarget(target);
    }

    @Override
    public Object get(String key) {
        return getTarget().get(key);
    }

    @Override
    public boolean has(String key) {
        return getTarget().containsKey(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getTarget().get(key);
    }

    @Override
    public void set(String key, Object value) {
        if (value == null) {
            getTarget().remove(key);
        } else {
            getTarget().put(key, value.toString());
        }
    }

}
