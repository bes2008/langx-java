package com.jn.langx.util.reflect.reference;


import java.lang.ref.ReferenceQueue;

public class References {
    private References(){

    }
    public static <V> Object newReference(V value, ReferenceType referenceType) {
        return newReference(null, value, referenceType, null, false);
    }

    public static <V> Object newReference(V value, ReferenceType referenceType, boolean wrappedWhenStrong) {
        return newReference(null, value, referenceType, null, wrappedWhenStrong);
    }

    public static <V> Object newReference(V value, ReferenceType referenceType, ReferenceQueue<Object> refQueue, boolean wrappedWhenStrong) {
        return newReference(null, value, referenceType, refQueue, wrappedWhenStrong);
    }

    public static <V> Object newReference(Integer hash, V value, ReferenceType referenceType, ReferenceQueue<Object> refQueue, boolean wrappedWhenStrong) {
        if (referenceType == ReferenceType.WEAK) {
            return new HashedWeakReference<V>(value, refQueue, hash);
        }
        if (referenceType == ReferenceType.SOFT) {
            return new HashedSoftwareReference<V>(value, refQueue, hash);
        }
        if (wrappedWhenStrong) {
            return new HashedStrongReference<V>(value, hash);
        }
        return value;
    }
}
