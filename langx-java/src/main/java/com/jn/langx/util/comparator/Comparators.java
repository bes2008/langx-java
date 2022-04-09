package com.jn.langx.util.comparator;

import java.util.Comparator;

public class Comparators {
    public static final Comparator<String> STRING_COMPARATOR= new StringComparator();
    public static final Comparator<String> STRING_COMPARATOR_IGNORE_CASE = new StringComparator(true);
    public static final Comparator<Character> CHAR_COMPARATOR = new CharComparator();
}
