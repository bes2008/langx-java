package com.jn.langx.util.valuegetter;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;

public class ArrayValueGetter<E> implements ValueGetter<E[], E> {
    private int index;

    public ArrayValueGetter(int index) {
        this.index = index;
    }

    @Override
    public E get(E[] input) {
        if (Emptys.isEmpty(input)) {
            return null;
        }
        Preconditions.checkIndex(index, input.length);
        return input[index];
    }
}
