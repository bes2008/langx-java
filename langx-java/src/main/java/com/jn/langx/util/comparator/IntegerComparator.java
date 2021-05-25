package com.jn.langx.util.comparator;

import java.util.Comparator;

public class IntegerComparator implements Comparator<Integer> {
    private boolean asc;

    public IntegerComparator() {
        this(true);
    }

    public IntegerComparator(boolean asc) {
        this.asc = asc;
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        return asc ? o1.compareTo(o2) : o2.compareTo(o1);
    }
}
