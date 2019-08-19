package com.jn.langx.util.collection;

import com.jn.langx.util.comparator.CharComparator;
import com.jn.langx.util.comparator.StringComparator;

public class Comparators {
    public static final StringComparator STRING_COMPARATOR = new StringComparator();
    public static final StringComparator STRING_COMPARATOR_IGNORE_CASE = new StringComparator(true);
    public static final CharComparator CHAR_COMPARATOR = new CharComparator();

}
