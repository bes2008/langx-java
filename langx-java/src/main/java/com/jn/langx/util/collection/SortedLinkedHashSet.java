package com.jn.langx.util.collection;

import com.jn.langx.Ordered;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.comparator.OrderedComparator;
import com.jn.langx.util.function.Supplier;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SortedLinkedHashSet<E> extends LinkedHashSet<E> {
    private OrderedComparator comparator;

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    private final ReentrantReadWriteLock.ReadLock readLock = rwl.readLock();

    private final ReentrantReadWriteLock.WriteLock writeLock = rwl.writeLock();

    public SortedLinkedHashSet() {
        this(new OrderedComparator());
    }

    public SortedLinkedHashSet(Supplier<E, Integer> defaultOrderSupplier) {
        this(new OrderedComparator(defaultOrderSupplier));
    }

    public SortedLinkedHashSet(OrderedComparator comparator) {
        this.comparator = comparator;
    }

    /**
     * Every time an Ordered element is added via this method this
     * Set will be re-sorted, otherwise the element is simply added
     * to the end. Added element must not be null.
     */
    @Override
    public boolean add(E o) {
        Preconditions.checkNotNull(o, "Can not add NULL object");
        writeLock.lock();
        try {
            boolean present = false;
            if (o instanceof Ordered) {
                present = this.addOrderedElement((Ordered) o);
            } else {
                present = super.add(o);
            }
            return present;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Adds all elements in this Collection.
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        Preconditions.checkNotNull(c, "Can not merge with NULL set");
        writeLock.lock();
        try {
            for (E object : c) {
                this.add(object);
            }
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        writeLock.lock();
        try {
            return super.remove(o);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        if (Objs.isEmpty(c)) {
            return false;
        }
        writeLock.lock();
        try {
            return super.removeAll(c);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        readLock.lock();
        try {
            return super.toArray(a);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String toString() {
        readLock.lock();
        try {
            return Strings.join(",", this);
        } finally {
            readLock.unlock();
        }
    }

    @SuppressWarnings("rawtypes")
    private boolean addOrderedElement(Ordered adding) {
        boolean added = false;
        E[] tempUnorderedElements = (E[]) this.toArray();
        if (super.contains(adding)) {
            return false;
        }
        super.clear();

        if (tempUnorderedElements.length == 0) {
            added = super.add((E) adding);
        } else {
            Set tempSet = new LinkedHashSet();
            for (E current : tempUnorderedElements) {
                if (current instanceof Ordered) {
                    if (this.comparator.compare(adding, current) < 0) {
                        added = super.add((E) adding);
                        super.add(current);
                    } else {
                        super.add(current);
                    }
                } else {
                    tempSet.add(current);
                }
            }
            if (!added) {
                added = super.add((E) adding);
            }
            for (Object object : tempSet) {
                super.add((E) object);
            }
        }
        return added;
    }

}
