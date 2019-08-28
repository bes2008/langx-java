package com.jn.langx.util.collection.diff;

import java.util.HashMap;
import java.util.Map;

public class MapDiffResult<K, V> implements CollectionDifferResult<Map<K, V>> {
    private Map<K, V> adds = new HashMap<K, V>();
    private Map<K, V> removes = new HashMap<K, V>();
    private Map<K, V> updates = new HashMap<K, V>();
    private Map<K, V> equals = new HashMap<K, V>();

    @Override
    public Map<K, V> getAdds() {
        return adds;
    }

    public void setAdds(Map<K, V> adds) {
        this.adds = adds;
    }

    @Override
    public Map<K, V> getRemoves() {
        return removes;
    }

    public void setRemoves(Map<K, V> removes) {
        this.removes = removes;
    }

    @Override
    public Map<K, V> getUpdates() {
        return updates;
    }

    public void setUpdates(Map<K, V> updates) {
        this.updates = updates;
    }

    @Override
    public Map<K, V> getEquals() {
        return equals;
    }

    public void setEquals(Map<K, V> equals) {
        this.equals = equals;
    }
}
