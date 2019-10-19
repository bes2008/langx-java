package com.jn.langx.cache;

import com.jn.langx.util.Preconditions;

public class Entry<K, V> {
    // required
    private K key;
    // required
    private V value;
    // required, for evict
    private long expireTime = Long.MAX_VALUE;

    Entry(K key, V value, long expireTime) {
        setKey(key);
        setValue(value);
        setExpireTime(expireTime);
    }


    private long lastReadTime;

    private long lastWriteTime;

    // for LRU
    private long lastUsedTime;

    private int usedCountFromLastEvict;

    private int age = 0;


    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        Preconditions.checkNotNull(key);
        this.key = key;
    }

    public V getValue() {
        lastReadTime = System.currentTimeMillis();
        lastUsedTime = lastReadTime;
        return value;
    }

    public void setValue(V value) {
        Preconditions.checkNotNull(value);
        lastWriteTime = System.currentTimeMillis();
        lastUsedTime = lastWriteTime;
        this.value = value;
        age = 0;
    }

    public int getAge() {
        return age;
    }

    public void incrementAge() {
        age = age + 1;
    }

    public void incrementUseCount() {
        usedCountFromLastEvict = usedCountFromLastEvict + 1;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expireTime;
    }

    public long getLastUsedTime() {
        return lastUsedTime;
    }

    public long getLastReadTime() {
        return lastReadTime;
    }

    public long getLastWriteTime() {
        return lastWriteTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()){
            return false;
        }

        Entry<?, ?> entry = (Entry<?, ?>) object;

        return key.equals(entry.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
