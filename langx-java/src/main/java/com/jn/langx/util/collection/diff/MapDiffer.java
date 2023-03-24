package com.jn.langx.util.collection.diff;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Differ;
import com.jn.langx.util.function.Consumer;

import java.util.*;

/**
 * @param <K>
 * @param <V>
 * @author jinuo.fang
 */
public class MapDiffer<K, V> implements Differ<Map<K, V>, MapDiffResult<K, V>> {
    @Nullable
    private Comparator<K> keyComparator;
    @Nullable
    private Comparator<V> valueComparator;



    public void setKeyComparator(@Nullable Comparator<K> comparator) {
        this.keyComparator = comparator;
    }

    public void setValueComparator(@Nullable Comparator<V> comparator) {
        this.valueComparator = comparator;
    }

    @Override
    public MapDiffResult<K, V> diff(@Nullable final Map<K, V> oldMap, @Nullable final Map<K, V> newMap) {
        MapDiffResult<K, V> result = new MapDiffResult<K, V>();

        if (oldMap == null && newMap == null) {
            return result;
        }

        if (newMap == null) {
            result.setRemoves(oldMap);
            return result;
        }

        if (oldMap == null) {
            result.setAdds(newMap);
            return result;
        }

        Set<K> oldKeys = oldMap.keySet();
        Set<K> newKeys = newMap.keySet();
        CollectionDiffer<K> keyDiffer = new CollectionDiffer<K>();
        keyDiffer.setComparator(keyComparator);
        CollectionDiffResult<K> keyDiffResult = keyDiffer.diff(oldKeys, newKeys);

        Collection<K> addsKeys = keyDiffResult.getAdds();
        final Map<K, V> addsMap = new HashMap<K, V>();
        Collects.forEach(addsKeys, new Consumer<K>() {
            @Override
            public void accept(K key) {
                addsMap.put(key, newMap.get(key));
            }
        });

        final Map<K, V> removesMap = new HashMap<K, V>();
        Collection<K> removesKeys = keyDiffResult.getRemoves();
        Collects.forEach(removesKeys, new Consumer<K>() {
            @Override
            public void accept(K key) {
                removesMap.put(key, oldMap.get(key));
            }
        });

        Collection<K> keyEquals = keyDiffResult.getEquals();
        final Map<K, V> equalsMap = new HashMap<K, V>();
        final Map<K, V> updatesMap = new HashMap<K, V>();
        Collects.forEach(keyEquals, new Consumer<K>() {
            @Override
            public void accept(K key) {
                V oldValue = oldMap.get(key);
                V newValue = newMap.get(key);
                if (valueComparator == null) {
                    if (oldValue.equals(newValue)) {
                        equalsMap.put(key, newValue);
                    } else {
                        updatesMap.put(key, newValue);
                    }
                } else {
                    if (valueComparator.compare(oldValue, newValue) == 0) {
                        equalsMap.put(key, newValue);
                    } else {
                        updatesMap.put(key, newValue);
                    }
                }
            }
        });

        result.setAdds(addsMap);
        result.setRemoves(removesMap);
        result.setEquals(equalsMap);
        result.setUpdates(updatesMap);
        return result;
    }

}
