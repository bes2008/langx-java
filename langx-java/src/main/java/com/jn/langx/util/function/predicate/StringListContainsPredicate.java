package com.jn.langx.util.function.predicate;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;

import java.util.Collection;
import java.util.Comparator;

public class StringListContainsPredicate extends ContainsPredicate<String> {
    public StringListContainsPredicate(String... array) {
        this(true, array);
    }

    public StringListContainsPredicate(boolean ignoreCase, String... array) {
        this(ignoreCase, Collects.asList(array));
    }

    public StringListContainsPredicate(Collection<String> collection) {
        this(true, collection);
    }

    public StringListContainsPredicate(final boolean ignoreCase, Collection<String> collection) {
        super(collection, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Strings.equals(o1, o2, ignoreCase) ? o1.compareTo(o2) : 0;
            }
        });
    }

}
