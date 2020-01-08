package com.jn.langx.util.collection;

import com.jn.langx.util.comparator.NonZeroComparator;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class NonDistinctTreeMap<K, V> extends TreeMap<K, V> {
    public NonDistinctTreeMap() {
        super();
    }

    public NonDistinctTreeMap(Comparator<? super K> comparator) {
        super(new NonZeroComparator(comparator));
    }

    public NonDistinctTreeMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public NonDistinctTreeMap(SortedMap<K, ? extends V> m) {
        super(m);
    }
}
