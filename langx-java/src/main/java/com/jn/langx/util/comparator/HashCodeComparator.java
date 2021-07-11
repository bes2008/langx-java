package com.jn.langx.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

public class HashCodeComparator<T> implements Comparator<T>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(T o1, T o2) {
        return o1.hashCode() - o2.hashCode();
    }
}
