package com.jn.langx.util.comparator;

import java.util.Comparator;

public class HashCodeComparator<T> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        return o1.hashCode() - o2.hashCode();
    }
}
