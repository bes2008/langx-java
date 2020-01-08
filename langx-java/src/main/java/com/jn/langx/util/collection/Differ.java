package com.jn.langx.util.collection;

public interface Differ<V, R extends DiffResult> {
    R diff(V oldValue, V newValue);
}
