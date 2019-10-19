package com.jn.langx.cache;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Supplier;

import java.util.*;

public class LRUCache<K, V> extends AbstractCache<K, V> {

    /**
     * key: lastUsedTime
     * Value: entry.key
     */
    private Map<Long, Set<Entry<K, V>>> lastUsedTimeIndex = WrappedNonAbsentMap.wrap(new TreeMap<Long, Set<Entry<K, V>>>(new ComparableComparator<Long>()), new Supplier<Long, Set<Entry<K, V>>>() {
        @Override
        public Set<Entry<K, V>> get(Long lastUsedTime) {
            return new TreeSet<Entry<K, V>>(new Comparator<Entry<K, V>>() {
                @Override
                public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                    return o1.getExpireTime() == o2.getExpireTime() ? 0 : (o1.getExpireTime() > o2.getExpireTime() ? 1 : -1);
                }
            });
        }
    });

    public LRUCache() {
        super(Integer.MAX_VALUE, 60 * 1000);
    }

    public LRUCache(int maxCapacity, long evictExpiredInterval) {
        super(maxCapacity, evictExpiredInterval);
    }

    @Override
    protected void addToCache(Entry<K, V> entry) {
        lastUsedTimeIndex.get(entry.getLastUsedTime()).add(entry);
    }

    @Override
    protected void removeFromCache(Entry<K, V> entry, RemoveCause removeCause) {
        lastUsedTimeIndex.get(entry.getLastUsedTime()).remove(entry);
    }

    @Override
    protected void beforeRecomputeExpireTimeOnRead(Entry<K, V> entry) {
    }

    @Override
    protected void afterRecomputeExpireTimeOnRead(Entry<K, V> entry) {
    }

    @Override
    protected List<Entry<K, V>> forceEvict(final int count) {

        final List<Entry<K, V>> evicted = new ArrayList<Entry<K, V>>();

        Collects.forEach(new ArrayList<Long>(lastUsedTimeIndex.keySet()), new Consumer<Long>() {
            @Override
            public void accept(Long lastUsedTime) {
                Set<Entry<K, V>> set = lastUsedTimeIndex.get(lastUsedTime);
                if (Emptys.isNotEmpty(set)) {
                    List<Entry<K, V>> list = new LinkedList<Entry<K, V>>(set);
                    while (evicted.size() < count && !list.isEmpty()) {
                        Entry<K, V> entry = list.remove(0);
                        if (entry != null) {
                            evicted.add(entry);
                        }
                    }
                }
            }
        });
        return evicted;
    }
}
