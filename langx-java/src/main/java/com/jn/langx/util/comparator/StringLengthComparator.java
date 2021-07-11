package com.jn.langx.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

public class StringLengthComparator implements Comparator<String>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length();
    }
}
