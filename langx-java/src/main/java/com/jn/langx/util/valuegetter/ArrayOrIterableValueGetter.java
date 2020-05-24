package com.jn.langx.util.valuegetter;

/**
 *
 * @param <V> 数组，或者 collection,或者 iterable
 * @deprecated 用 {@link LinerCollectionValueGetter} 替代
 * @see LinerCollectionValueGetter
 */
@Deprecated
public class ArrayOrIterableValueGetter<V> extends LinerCollectionValueGetter<V> {
    public ArrayOrIterableValueGetter(int index) {
        super(index);
    }
}