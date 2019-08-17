package com.jn.langx.util.collection.diff;

public interface KeyBuilder<K, O> {
    K getKey(O object);
}
