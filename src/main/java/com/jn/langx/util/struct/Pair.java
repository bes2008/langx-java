package com.jn.langx.util.struct;

/**
 * Key value pair
 */
public abstract class Pair<K, V> {
    private K key;
    private V value;

    public K getKey() {
        return key;
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
        return key.equals(that.key) && value.equals(that.key);
    }

}
