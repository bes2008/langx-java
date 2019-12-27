package com.jn.langx.util.collection.iter;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.PrimitiveArrays;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author jinuo.fang
 */
public class ArrayIterator<E> extends UnmodifiableIterator<E> implements Iterable<E> {
    private final E[] array;
    private int index = 0;
    private final int length;

    public ArrayIterator(Object array) {
        if (array != null) {
            Preconditions.checkArgument(Arrs.isArray(array));
            if (PrimitiveArrays.isPrimitiveArray(array.getClass())) {
                this.array = PrimitiveArrays.wrap(array);
            } else {
                this.array = (E[]) array;
            }
        } else {
            this.array = null;
        }
        this.length = this.array == null ? 0 : this.array.length;
    }

    public ArrayIterator(E[] array) {
        this.array = array;
        this.length = array == null ? 0 : array.length;
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return index < length;
    }

    @Override
    public E next() {
        if (hasNext()) {
            return array[index++];
        } else {
            throw new NoSuchElementException();
        }
    }
}
