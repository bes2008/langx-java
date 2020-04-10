package com.jn.langx.util.collection.iter;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ReverseListIterator<E> implements Iterator<E> {
    /**
     * The list to get elements from
     */
    protected final List<E> list;

    /**
     * The current index of the list
     */
    protected int current;

    /**
     * Construct a ReverseListIterator for the given list.
     *
     * @param list List to iterate over.
     */
    public ReverseListIterator(final List<E> list) {
        this.list = list;
        current = list.size() - 1;
    }

    /**
     * Check if there are more elements.
     *
     * @return True if there are more elements.
     */
    public boolean hasNext() {
        return current > 0;
    }

    /**
     * Get the next element.
     *
     * @return The next element.
     * @throws NoSuchElementException
     */
    public E next() {
        if (current <= 0) {
            throw new NoSuchElementException();
        }

        return list.get(current--);
    }

    /**
     * Remove the current element.
     */
    public void remove() {
        list.remove(current);
    }
}
