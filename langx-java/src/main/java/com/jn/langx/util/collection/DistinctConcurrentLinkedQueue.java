package com.jn.langx.util.collection;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @param <E> the element in queue
 * @since 4.0.5
 */
public class DistinctConcurrentLinkedQueue<E> extends ConcurrentLinkedQueue<E> {
    private ConcurrentHashMap<E, Integer> elementsHolder = new ConcurrentHashMap<E, Integer>();

    public DistinctConcurrentLinkedQueue() {

    }

    public DistinctConcurrentLinkedQueue(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public boolean offer(E e) {
        Preconditions.checkNotNull(e);
        if (!elementsHolder.containsKey(e)) {
            boolean success = super.offer(e);
            if (success) {
                elementsHolder.put(e, 1);
            }
            return success;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        List<E> elements = Pipeline.<E>of(c).filter(new Predicate<E>() {
            @Override
            public boolean test(E e) {
                return !elementsHolder.containsKey(e);
            }
        }).asList();
        if (elements.isEmpty()) {
            return false;
        }
        Collects.forEach(elements, new Consumer<E>() {
            @Override
            public void accept(E e) {
                offer(e);
            }
        });
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return elementsHolder.containsKey(o);
    }

    @Override
    public boolean remove(Object o) {
        boolean success = super.remove(o);
        if (success) {
            elementsHolder.remove(o);
        }
        return success;
    }

    @Override
    public E poll() {
        E e = super.poll();
        if (e != null) {
            elementsHolder.remove(e);
        }
        return e;
    }

}
