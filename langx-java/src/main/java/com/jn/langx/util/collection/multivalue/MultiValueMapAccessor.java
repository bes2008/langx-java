package com.jn.langx.util.collection.multivalue;

import com.jn.langx.util.BasedStringAccessor;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;

import java.util.Collection;
import java.util.List;

public class MultiValueMapAccessor<V> extends BasedStringAccessor<String, MultiValueMap<String, V>> {

    @Override
    public Object get(String key) {
        return getValueList(key);
    }

    private V getFirst(String key) {
        List<V> values = getValueList(key);
        return Emptys.isEmpty(values) ? null : values.get(0);
    }

    public List<V> getValueList(String key) {
        Collection<V> vs = this.getTarget().get(key);
        if (vs == null) {
            return null;
        }
        return Collects.asList(vs);
    }

    @Override
    public String getString(String key, String defaultValue) {
        V value = getFirst(key);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    @Override
    public void set(String key, Object value) {
        getTarget().add(key, (V) value);
    }

}
