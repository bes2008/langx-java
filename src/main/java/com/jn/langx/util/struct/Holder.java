
package com.jn.langx.util.struct;

public class Holder<V> {
    private V v;

    public Holder() {
    }

    public Holder(final V value) {
        this.set(value);
    }

    public void set(final V value) {
        this.v = value;
    }

    public V get() {
        return this.v;
    }

}
