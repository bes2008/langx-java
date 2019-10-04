package com.jn.langx.util.comparator;

import java.util.Comparator;

public class ComparableComparator<E extends Comparable<E>> implements Comparator<E> {
    @Override
    public int compare(E o1, E o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        return o1.compareTo(o2);
    }
}
