package com.jn.langx.util.collection.sequence;

import com.jn.langx.util.collection.Collects;

public class ArraySequence<E> extends ListSequence<E> {
    public ArraySequence(E[] array) {
        super(Collects.asList(array));
    }
}
