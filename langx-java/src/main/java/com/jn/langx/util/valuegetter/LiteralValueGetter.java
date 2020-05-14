package com.jn.langx.util.valuegetter;

public class LiteralValueGetter<V> implements ValueGetter<V, V> {
    @Override
    public V get(V input) {
        return input;
    }
}
