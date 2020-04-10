package com.jn.langx.util.reflect.reference;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.struct.Reference;

import java.lang.ref.ReferenceQueue;

public class ReferenceEntry<K, V> {
    // required
    protected Object keyRef;
    private ReferenceType keyReferenceType = ReferenceType.STRONG;
    // required
    protected Object valueRef;
    private ReferenceType valueReferenceType = ReferenceType.STRONG;
    private ReferenceQueue refQueue;
    private boolean wrappedWhenStrong = false;

    public ReferenceEntry(K key, ReferenceType keyReferenceType, V value, ReferenceType valueReferenceType, ReferenceQueue referenceQueue, boolean wrappedWhenStrong) {
        setKeyReferenceType(keyReferenceType);
        setValueReferenceType(valueReferenceType);
        setKey(key);
        setValue(value);
        this.refQueue = referenceQueue;
        this.wrappedWhenStrong = wrappedWhenStrong;
    }

    public ReferenceEntry(K key, V value, ReferenceQueue referenceQueue, boolean wrappedWhenStrong) {
        this(key, ReferenceType.STRONG, value, ReferenceType.STRONG, referenceQueue, wrappedWhenStrong);
    }

    public K getKey() {
        if (keyRef instanceof Reference) {
            return ((Reference<K>) keyRef).get();
        }
        return (K) keyRef;
    }

    public void setKey(K key) {
        Preconditions.checkNotNull(key);
        this.keyRef = References.newReference(key, keyReferenceType, refQueue, wrappedWhenStrong);
    }

    public V getValue() {
        if (valueRef instanceof Reference) {
            return ((Reference<V>) valueRef).get();
        }
        return (V) valueRef;
    }

    public void setValue(V value) {
        Preconditions.checkNotNull(value);

        this.valueRef = References.newReference(value, valueReferenceType, refQueue, wrappedWhenStrong);
    }

    private void setKeyReferenceType(ReferenceType keyReferenceType) {
        this.keyReferenceType = keyReferenceType;
    }

    private void setValueReferenceType(ReferenceType valueReferenceType) {
        this.valueReferenceType = valueReferenceType;
    }
}
