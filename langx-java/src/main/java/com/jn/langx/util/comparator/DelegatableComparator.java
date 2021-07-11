package com.jn.langx.util.comparator;

import com.jn.langx.Delegatable;

import java.util.Comparator;

public interface DelegatableComparator extends Comparator, Delegatable<Comparator> {

    @Override
    Comparator getDelegate();

    @Override
    void setDelegate(final Comparator delegate);

    @Override
    int compare(Object o1, Object o2);
}
