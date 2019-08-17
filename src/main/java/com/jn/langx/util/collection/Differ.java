package com.jn.langx.util.collection;

import java.util.Comparator;

public interface Differ<C,E> {
    void setComparator(Comparator<E> comparator);

    DiffResult<C> diff(C oldCollection, C newCollection);
}
