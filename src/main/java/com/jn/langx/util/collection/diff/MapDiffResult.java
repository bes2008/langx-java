package com.jn.langx.util.collection.diff;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objects;

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

    public void setAdds(@Nullable Map<K, V> adds) {
        if (Objects.nonNull(adds)) {
            this.adds = adds;
        }
    }

    @Override
    public Map<K, V> getRemoves() {
        return removes;
    }

    public void setRemoves(@Nullable Map<K, V> removes) {
        if (Objects.nonNull(removes)) {
            this.removes = removes;
        }
    }

    @Override
    public Map<K, V> getUpdates() {
        return updates;
    }

    public void setUpdates(@Nullable Map<K, V> updates) {
        if (Objects.nonNull(updates)) {
            this.updates = updates;
        }
    }

    @Override
    public Map<K, V> getEquals() {
        return equals;
    }

    public void setEquals(@Nullable Map<K, V> equals) {
        if (Objects.nonNull(equals)) {
            this.equals = equals;
        }
    }

    @Override
    public boolean hasDifference() {
        return Emptys.isNotEmpty(adds) || Emptys.isNotEmpty(updates) || Emptys.isNotEmpty(removes);
    }
}
