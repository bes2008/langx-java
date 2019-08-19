package com.jn.langx.util;

import com.jn.langx.util.comparator.StringComparator;

public class Comparators {
    public static final StringComparator STRING_COMPARATOR = new StringComparator();
    public static final StringComparator STRING_COMPARATOR_IGNORE_CASE = new StringComparator(true);

}
