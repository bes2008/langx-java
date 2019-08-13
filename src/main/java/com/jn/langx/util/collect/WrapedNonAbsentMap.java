package com.jn.langx.util.collect;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.collect.function.Supplier;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Avoid the follower code:
 * <pre>
 *     Map<K,List<E>> map = new HashMap<K,List<E>>();
 *     List<E> list = map.get(key);
 *     if(list==null){
 *         list = new ArrayList<E>();
 *         map.put(key, list);
 *     }
 *     list.add(e);
 * </pre>
 * <p>
 * using this class, your code will be:
 * <pre>
 *     Supplier<K,List<E>> supplier = new Supplier<K,List<E>>(){
 *         public List<E> get(K key){
 *             return new ArrayList<E>();
 *         }
 *     };
 *     Map<K,List<E>> map0 = new HashMap<K,List<E>>();
 *     Map<K,List<E>> map = WrapedNonAbsentMap.wrap(map0, supplier);
 *     map.get(key).add(e);
 * </pre>
 *
 * @param <K> key
 * @param <V> value
 * @see java.util.Map
 * @see java.util.HashMap
 * @see WrapedNonAbsentMap
 */
public class WrapedNonAbsentMap<K, V> implements Map<K, V> {
    private Map<K, V> delegate;
    private Supplier<K, V> supplier;

    public WrapedNonAbsentMap(Map<K, V> map, Supplier<K, V> supplier) {
        this.delegate = map;
        this.supplier = supplier;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return Emptys.isEmpty(delegate);
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    public V get(Object key, Supplier<K, V> supplier) {
        K key0 = (K) key;
        V v = getIfPresent(key0);
        if (v == null) {
            supplier = supplier != null ? supplier : this.supplier;
            return putIfAbsent(key0, supplier.get(key0));
        }
        return v;
    }

    @Override
    public V get(Object key) {
        return get(key, null);
    }

    public V getIfPresent(Object key) {
        return delegate.get(key);
    }

    public V putIfAbsent(K key, V value) {
        V v = delegate.get(key);
        if (v == null) {
            delegate.put(key, value);
        }
        return v;
    }

    @Override
    public V put(K key, V value) {
        return delegate.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (Emptys.isNotEmpty(m)) {
            delegate.putAll(m);
        }
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<K> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return delegate.entrySet();
    }

    public static <K,V> WrapedNonAbsentMap<K,V> wrap(Map<K,V> map, Supplier<K,V> supplier){
        return new WrapedNonAbsentMap<K,V>(map, supplier);
    }
}
