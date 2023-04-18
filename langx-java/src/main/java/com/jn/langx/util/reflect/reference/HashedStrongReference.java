package com.jn.langx.util.reflect.reference;

import com.jn.langx.util.struct.Reference;

public class HashedStrongReference<V> implements Reference<V> {
    private V referent;
    private int hash;

    public HashedStrongReference(V referent) {
        this(referent, null);
    }

    public HashedStrongReference(V referent, Integer hash) {
        this.referent = referent;
        setHash(hash == null ? (referent == null ? 0 : referent.hashCode()) : hash);
    }

    public int getHash() {
        return hash;
    }

    @Override
    public void setHash(int hash) {
        this.hash = hash;
    }

    @Override
    public V get() {
        return referent;
    }

    @Override
    public boolean isNull() {
        return referent == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashedStrongReference<?> that = (HashedStrongReference<?>) o;

        if (hash != that.hash) return false;
        return referent != null ? referent.equals(that.referent) : that.referent == null;
    }

    @Override
    public int hashCode() {
        int result = referent != null ? referent.hashCode() : 0;
        result = 31 * result + hash;
        return result;
    }
}
