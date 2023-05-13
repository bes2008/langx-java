package com.jn.langx.util.collection.iter;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.PrimitiveArrays;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author jinuo.fang
 */
@SuppressWarnings("unchecked")
public class ArrayIterator<E> extends UnmodifiableIterator<E> implements Iterable<E> {
    private final E[] array;
    private int index = 0;
    private final int length;
    private boolean reversed;

    public ArrayIterator(Object array) {
        this(array, false);
    }

    public ArrayIterator(Object array, boolean reversed) {
        if (array != null) {
            Preconditions.checkArgument(Arrs.isArray(array));
            if (PrimitiveArrays.isPrimitiveArray(array.getClass())) {
                this.array = PrimitiveArrays.wrap(array);
            } else {
                this.array = Arrs.copy((E[]) array);
            }
        } else {
            this.array = null;
        }
        this.length = this.array == null ? 0 : this.array.length;
        this.reversed = reversed;
        this.index = reversed ? (this.length - 1) : 0;
    }

    public ArrayIterator(E[] array) {
        this(array, false);
    }

    public ArrayIterator(E[] array, boolean reversed) {
        this.array = Arrs.copy(array);
        this.length = array == null ? 0 : array.length;
        this.reversed = reversed;
        this.index = reversed ? (this.length - 1) : 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<E>(array, reversed);
    }

    @Override
    public boolean hasNext() {
        return reversed ? index >= 0 : index < length;
    }

    @Override
    public E next() {
        if (hasNext()) {
            return array[reversed ? index-- : index++];
        } else {
            throw new NoSuchElementException();
        }
    }
}
