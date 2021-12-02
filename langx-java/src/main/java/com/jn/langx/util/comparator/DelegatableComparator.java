package com.jn.langx.util.comparator;

import com.jn.langx.Delegatable;

import java.util.Comparator;

public interface DelegatableComparator<V> extends Comparator<V>, Delegatable<Comparator<V>> {

    @Override
    Comparator<V> getDelegate();

    @Override
    void setDelegate(final Comparator<V> delegate);

    @Override
    int compare(V o1, V o2);
}
