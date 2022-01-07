package com.jn.langx.util.function.predicate;

import com.jn.langx.util.collection.Collects;

import java.util.Collection;
import java.util.Comparator;

public class StringListContainsPredicate extends ContainsPredicate<String>{
    public StringListContainsPredicate(String... array){
        this(Collects.asList(array));
    }
    public StringListContainsPredicate(Collection<String> collection) {
        super(collection);
    }

    public StringListContainsPredicate(Collection<String> collection, Comparator<String> comparator) {
        super(collection, comparator);
    }
}
