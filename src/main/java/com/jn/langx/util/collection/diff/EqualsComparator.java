package com.jn.langx.util.collection.diff;

import java.util.Comparator;

public final class EqualsComparator<V> implements Comparator<V> {
    @Override
    public int compare(V o1, V o2) {
        return o1.equals(o2) ? 0 : 1;
    }
}
