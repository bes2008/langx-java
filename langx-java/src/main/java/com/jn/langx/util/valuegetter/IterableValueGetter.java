package com.jn.langx.util.valuegetter;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;

import java.util.List;

public class IterableValueGetter<V> implements ValueGetter<Iterable<V>, V> {
    private int index;

    public IterableValueGetter(int index) {
        this.index = index;
    }

    @Override
    public V get(Iterable<V> input) {
        List<V> list = Collects.asList(input);
        if (Emptys.isEmpty(list)) {
            return null;
        }
        return list.get(index);
    }
}
