package com.jn.langx.util.hash;

import com.jn.langx.Builder;

public class HashCodeBuilder implements Builder<Integer> {
    private int hash = 0;

    public HashCodeBuilder() {
        this(0);
    }

    public HashCodeBuilder(int hash) {
        if (hash < 0) {
            hash = 0;
        }
        this.hash = hash;
    }

    public HashCodeBuilder with(Object object) {
        compute(object == null ? 0 : object.hashCode());
        return this;
    }

    private void compute(int hash) {
        this.hash = this.hash * 31 + hash;
    }

    public Integer build() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashCodeBuilder that = (HashCodeBuilder) o;

        return hash == that.hash;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
