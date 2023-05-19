package com.jn.langx.text.lexer;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.reflect.reference.ReferenceType;

import java.util.concurrent.atomic.AtomicInteger;

public class Key<T> {
    private static final AtomicInteger ourKeysCounter = new AtomicInteger();

    private static final ConcurrentReferenceHashMap<Integer, Key<?>> allKeys = new ConcurrentReferenceHashMap<Integer, Key<?>>(10, ReferenceType.STRONG, ReferenceType.WEAK);

    private final int myIndex = ourKeysCounter.getAndIncrement();

    private final String myName;

    public Key(@NonNull String name) {
        this.myName = name;
        synchronized (allKeys) {
            allKeys.put(this.myIndex, this);
        }
    }

    public final int hashCode() {
        return this.myIndex;
    }

    public final boolean equals(Object obj) {
        return obj == this;
    }

    public String toString() {
        return this.myName;
    }

    @NonNull
    public static <T> Key<T> create(@NonNull String name) {
        Preconditions.checkNotNullArgument(name, "name");
        return new Key<T>(name);
    }


    public static <T> Key<T> getKeyByIndex(int index) {
        synchronized (allKeys) {
            return (Key<T>) allKeys.get(index);
        }
    }

    @Deprecated
    @Nullable
    public static Key<?> findKeyByName(@NonNull String name) {
        Preconditions.checkNotNullArgument(name, "name");
        synchronized (allKeys) {
            for (Key<?> key : allKeys.values()) {
                if (name.equals(key.myName))
                    return key;
            }
            return null;
        }
    }
}