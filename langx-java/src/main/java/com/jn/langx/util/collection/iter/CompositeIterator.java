package com.jn.langx.util.collection.iter;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Composite iterator that combines multiple other iterators,
 * as registered via {@link #add(Iterator)}.
 * <p>
 * <p>This implementation maintains a linked set of iterators
 * which are invoked in sequence until all iterators are exhausted.
 *
 * @param <E> the element type
 */
public class CompositeIterator<E> extends UnmodifiableIterator {

    private final List<Iterator<E>> iterators = Collects.emptyArrayList();

    private int currentIteratorIndex = -1;

    /**
     * Add given iterator to this composite.
     */
    public void add(Iterator<E> iterator) {
        Preconditions.checkNotNull(iterator);
        if (this.currentIteratorIndex <= -1) {
            if (this.iterators.contains(iterator)) {
                throw new IllegalArgumentException("You cannot add the same iterator twice");
            }
            this.iterators.add(iterator);
        }

    }

    @Override
    public boolean hasNext() {
        if (iterators.isEmpty() || currentIteratorIndex >= iterators.size()) {
            return false;
        }
        if (currentIteratorIndex == -1) {
            currentIteratorIndex = 0;
        }
        Iterator<E> currentIterator = iterators.get(currentIteratorIndex);
        if (currentIterator.hasNext()) {
            return true;
        } else {
            currentIteratorIndex++;
            return hasNext();
        }
    }

    @Override
    public E next() {
        if (hasNext()) {
            return iterators.get(currentIteratorIndex).next();
        }
        throw new NoSuchElementException("All iterators exhausted");
    }

}
