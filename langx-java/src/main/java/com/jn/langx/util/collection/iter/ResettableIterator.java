package com.jn.langx.util.collection.iter;

import java.util.Iterator;

public interface ResettableIterator<E> extends Iterator<E> {
    void reset();
}
