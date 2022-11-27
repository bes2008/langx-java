package com.jn.langx.text.lexer;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

public abstract class ImmutableUserMap {
    public static final ImmutableUserMap EMPTY = new ImmutableUserMap() {
        public <T> T get(@NonNull Key<T> key) {
            Preconditions.checkNotNullArgument(key,"key");
            return null;
        }
    };

    private ImmutableUserMap() {}

    public final <T> ImmutableUserMap put(@NonNull Key<T> key, T value) {
        Preconditions.checkNotNullArgument(key,"key");
        return new ImmutableUserMapImpl(key, value, this);
    }

    public abstract <T> T get(@NonNull Key<T> paramKey);

    private static final class ImmutableUserMapImpl<V> extends ImmutableUserMap {
        private final Key<V> myKey;

        private final V myValue;

        private final ImmutableUserMap myNext;

        private ImmutableUserMapImpl(Key<V> key, V value, ImmutableUserMap next) {
            this.myKey = key;
            this.myNext = next;
            this.myValue = value;
        }

        public <T> T get(@NonNull Key<T> key) {
            Preconditions.checkNotNullArgument(key,"key");
            if (key.equals(this.myKey))
                return (T)this.myValue;
            return this.myNext.get(key);
        }
    }
}
