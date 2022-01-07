package com.jn.langx.util.function.predicate;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Objs;
import com.jn.langx.util.comparator.EqualsComparator;
import com.jn.langx.util.function.Predicate;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

public class ContainsPredicate<V> implements Predicate<V> {
    private TreeSet<V> set;

    public ContainsPredicate(Collection<V> collection) {
        this(collection,null);
    }

    public ContainsPredicate(Collection<V> collection, @NonNull Comparator<V> comparator) {
        if (comparator == null) {
            comparator = new EqualsComparator<V>();
        }
        set = new TreeSet<V>(comparator);
        if (Objs.isNotEmpty(collection)) {
            set.addAll(collection);
        }
    }

    @Override
    public boolean test(V value) {
        return set.contains(value);
    }
}
