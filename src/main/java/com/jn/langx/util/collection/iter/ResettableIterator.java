package com.jn.langx.util.collection.iter;

public interface ResettableIterator<E> extends Iterable<E> {
    void reset();
}
