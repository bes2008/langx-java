package com.jn.langx.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

public class ReverseComparator<V> implements Comparator<V>, Serializable {
    private static final long serialVersionUID = 1L;
    private transient Comparator<V> delegate;

    public ReverseComparator(Comparator<V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int compare(V o1, V o2) {
        return delegate.compare(o2, o1);
    }
}
