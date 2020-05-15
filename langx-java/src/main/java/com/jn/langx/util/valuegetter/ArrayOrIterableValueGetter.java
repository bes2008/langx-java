package com.jn.langx.util.valuegetter;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;

import java.util.List;

public class ArrayOrIterableValueGetter<V> implements ValueGetter<Object, V> {
    private int index;

    public ArrayOrIterableValueGetter(int index) {
        this.index = index;
    }

    @Override
    public V get(Object input) {
        List<V> list = Collects.asList(Collects.<V>asIterable(input));
        if (Emptys.isEmpty(list)) {
            return null;
        }
        return list.get(index);
    }
}
