package com.jn.langx.cache;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.reference.ReferenceEntry;
import com.jn.langx.util.reflect.reference.ReferenceType;
import com.jn.langx.util.struct.Reference;

import java.lang.ref.ReferenceQueue;

public class Entry<K, V> extends ReferenceEntry<K, V> {

    // required, for evict
    private long expireTime = Long.MAX_VALUE;

    Entry(K key, ReferenceType keyReferenceType, V value, ReferenceType valueReferenceType, ReferenceQueue referenceQueue, boolean wrappedWhenStrong, long expireTime) {
        super(key, keyReferenceType, value, valueReferenceType, referenceQueue, wrappedWhenStrong);
        setExpireTime(expireTime);
    }

    Entry(K key, V value, boolean wrappedWhenStrong, long expireTime) {
        this(key, ReferenceType.STRONG, value, ReferenceType.STRONG, null, wrappedWhenStrong, expireTime);
    }


    private long lastReadTime;

    private long lastWriteTime;

    // for LRU
    private long lastUsedTime;

    private int usedCountFromLastEvict;

    private int age = 0;

    @Override
    public V getValue() {
        return getValue(true);
    }

    public V getValue(boolean updateTime) {
        if(updateTime) {
            lastReadTime = System.currentTimeMillis();
            lastUsedTime = lastReadTime;
        }
        return super.getValue();
    }

    public void setValue(V value) {
        Preconditions.checkNotNull(value);
        lastWriteTime = System.currentTimeMillis();
        lastUsedTime = lastWriteTime;
        super.setValue(value);
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

    public void setLastReadTime(long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public void setLastWriteTime(long lastWriteTime) {
        this.lastWriteTime = lastWriteTime;
    }

    public void setLastUsedTime(long lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Entry<?, ?> entry = (Entry<?, ?>) object;

        return keyRef.equals(entry.keyRef);
    }

    @Override
    public int hashCode() {
        if (keyRef != null) {
            if (keyRef instanceof Reference) {
                return ((Reference<K>) keyRef).getHash();
            }
            return keyRef.hashCode();
        }
        return 0;
    }


}
