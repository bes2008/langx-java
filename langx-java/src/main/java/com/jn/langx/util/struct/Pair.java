package com.jn.langx.util.struct;

import com.jn.langx.util.Objs;
import com.jn.langx.util.hash.HashCodeBuilder;

import java.util.Map;

/**
 * Key value pair
 */
public class Pair<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    public Pair() {
    }

    public Pair(K k, V v) {
        this.setKey(k);
        this.setValue(v);
    }

    public K getKey() {
        return key;
    }

    public static <K,V> Pair<K,V> of(K k, V v){
        return new Pair<K,V>(k, v);
    }

    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Pair that = (Pair) object;
        if (!Objs.equals(key, that.key)) {
            return false;
        }
        return Objs.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().with(key).with(value).build();
    }
}
