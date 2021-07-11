package com.jn.langx.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

public class IntegerComparator implements Comparator<Integer>, Serializable {
    private static final long serialVersionUID = 1L;
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
