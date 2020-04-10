package com.jn.langx.util.reflect.reference;

import com.jn.langx.util.Objects;
import com.jn.langx.util.struct.Reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class HashedSoftwareReference<V> extends SoftReference<V> implements Reference<V> {
    private int hash;

    public HashedSoftwareReference(V referent, Integer hash) {
        super(referent);
        setHash(hash == null ? (referent == null ? 0 : referent.hashCode()) : hash);
    }

    public HashedSoftwareReference(V referent, ReferenceQueue<? super V> q, Integer hash) {
        super(referent, q);
        setHash(hash == null ? (referent == null ? 0 : referent.hashCode()) : hash);
    }

    public int getHash() {
        return hash;
    }

    @Override
    public boolean isNull() {
        return get() == null;
    }

    @Override
    public void setHash(int hash) {
        this.hash = hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashedSoftwareReference<?> that = (HashedSoftwareReference<?>) o;

        if (hash != that.hash) {
            return false;
        }
        return Objects.equals(get(), that.get());
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
