package com.jn.langx.util.valuegetter;

import com.jn.langx.util.Emptys;

import java.util.Map;

public class MapValueGetter<V> implements ValueGetter<Map<String, V>, V> {
    private String key;

    public MapValueGetter(String key) {
        this.key = key;
    }

    @Override
    public V get(Map<String, V> map) {
        if (Emptys.isEmpty(map)) {
            return null;
        }
        return map.get(key);
    }
}
