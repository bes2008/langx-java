package com.jn.langx.util.collection;

import com.jn.langx.util.Objs;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Implements a combination of WeakHashMap and IdentityHashMap.
 * Useful for caches that need to key off of a == comparison
 * instead of a .equals.
 * <p>
 * <b>
 * This class is not a general-purpose Map implementation! While
 * this class implements the Map interface, it intentionally violates
 * Map's general contract, which mandates the use of the equals method
 * when comparing objects. This class is designed for use only in the
 * rare cases wherein reference-equality semantics are required.
 * <p>
 * Note that this implementation is not synchronized.
 * </b>
 */
public class WeakIdentityHashMap<K, V> implements Map<K, V> {
    private final ReferenceQueue<K> queue = new ReferenceQueue<K>();
    private Map<IdentityWeakReference, V> backingStore = new HashMap<IdentityWeakReference, V>();

    public WeakIdentityHashMap() {
    }

    public void clear() {
        backingStore.clear();
        reap();
    }

    public boolean containsKey(Object key) {
        reap();
        return backingStore.containsKey(new IdentityWeakReference(key));
    }

    public boolean containsValue(Object value) {
        reap();
        return backingStore.containsValue(value);
    }

    public Set<Map.Entry<K, V>> entrySet() {
        reap();
        Set<Map.Entry<K, V>> ret = new HashSet<Map.Entry<K, V>>();
        for (Map.Entry<IdentityWeakReference, V> ref : backingStore.entrySet()) {
            final K key = ref.getKey().get();
            final V value = ref.getValue();
            Map.Entry<K, V> entry = new Map.Entry<K, V>() {
                public K getKey() {
                    return key;
                }

                public V getValue() {
                    return value;
                }

                public V setValue(V value) {
                    throw new UnsupportedOperationException();
                }
            };
            ret.add(entry);
        }
        return Collections.unmodifiableSet(ret);
    }

    public Set<K> keySet() {
        reap();
        Set<K> ret = new HashSet<K>();
        for (IdentityWeakReference ref : backingStore.keySet()) {
            ret.add(ref.get());
        }
        return Collections.unmodifiableSet(ret);
    }

    public boolean equals(Object o) {
        if (o instanceof WeakIdentityHashMap) {
            //noinspection rawtypes
            return backingStore.equals(((WeakIdentityHashMap) o).backingStore);
        }
        return false;
    }

    public V get(Object key) {
        reap();
        return backingStore.get(new IdentityWeakReference(key));
    }

    public V put(K key, V value) {
        reap();
        return backingStore.put(new IdentityWeakReference(key), value);
    }

    public int hashCode() {
        reap();
        return backingStore.hashCode();
    }

    public boolean isEmpty() {
        reap();
        return backingStore.isEmpty();
    }

    public void putAll(Map t) {
        throw new UnsupportedOperationException();
    }

    public V remove(Object key) {
        reap();
        return backingStore.remove(new IdentityWeakReference(key));
    }

    public int size() {
        reap();
        return backingStore.size();
    }

    public Collection<V> values() {
        reap();
        return backingStore.values();
    }

    private synchronized void reap() {
        Object zombie = queue.poll();

        while (zombie != null) {
            IdentityWeakReference victim = (IdentityWeakReference) zombie;
            backingStore.remove(victim);
            zombie = queue.poll();
        }
    }

    class IdentityWeakReference extends WeakReference<K> {
        int hash;

        @SuppressWarnings("unchecked")
        IdentityWeakReference(Object obj) {
            super((K) obj, queue);
            hash = Objs.id(obj);
        }

        public int hashCode() {
            return hash;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof WeakIdentityHashMap.IdentityWeakReference) {
                IdentityWeakReference ref = (IdentityWeakReference) o;
                return this.get() == ref.get();
            }
            return false;
        }
    }
}
