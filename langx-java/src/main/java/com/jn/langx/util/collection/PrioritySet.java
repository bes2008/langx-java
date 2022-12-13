package com.jn.langx.util.collection;

import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.iter.MappingIterator;
import com.jn.langx.util.comparator.IntegerComparator;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.struct.Pair;
import com.jn.langx.util.struct.counter.AtomicIntegerCounter;

import java.util.*;

public class PrioritySet<E> extends AbstractSet<E> {

    private transient Map<E, AtomicIntegerCounter> elementToCounterMap;
    /**
     * 计数器最大值
     */
    private int counterMaxValue;
    /**
     * 计数器更新步长
     */
    private int counterUpdateStep;

    private int counterInitialValue;

    private Set<Map.Entry<E, AtomicIntegerCounter>> elements;

    public PrioritySet() {
        this(0, 100, 0);
    }

    public PrioritySet(int counterUpdateStep) {
        this(counterUpdateStep, 0);
    }

    public PrioritySet(int counterUpdateStep, int counterMaxValue) {
        this(0, counterUpdateStep, counterMaxValue);
    }

    public PrioritySet(int counterInitialValue, int counterUpdateStep, int counterMaxValue) {
        this(new HashMap<E, AtomicIntegerCounter>(), null, counterInitialValue, counterUpdateStep, counterMaxValue);
    }

    public PrioritySet(Collection<E> source, int counterInitialValue, int counterUpdateStep, int counterMaxValue) {
        this(new HashMap<E, AtomicIntegerCounter>(), source, counterInitialValue, counterUpdateStep, counterMaxValue);
    }

    public PrioritySet(Map<E, AtomicIntegerCounter> elementToCounterMap, Collection<E> source, int counterInitialValue, int counterUpdateStep, int counterMaxValue) {
        this.elementToCounterMap = elementToCounterMap;
        this.counterUpdateStep = counterUpdateStep > 1 ? counterUpdateStep : 1000;
        this.counterMaxValue = counterMaxValue > this.counterUpdateStep ? counterMaxValue : this.counterUpdateStep * 10;

        this.counterInitialValue = Maths.max(counterInitialValue, 0);
        this.elements = new TreeSet<Map.Entry<E, AtomicIntegerCounter>>(new Comparator<Map.Entry<E, AtomicIntegerCounter>>() {
            private IntegerComparator comparator = new IntegerComparator(false);

            @Override
            public int compare(Map.Entry<E, AtomicIntegerCounter> o1, Map.Entry<E, AtomicIntegerCounter> o2) {
                return comparator.compare(o1.getValue().get(), o2.getValue().get());
            }
        });
        if (Objs.isNotEmpty(source)) {
            addAll(source);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new MappingIterator<Map.Entry<E, AtomicIntegerCounter>, E>(this.elements, new Function<Map.Entry<E, AtomicIntegerCounter>, E>() {
            @Override
            public E apply(Map.Entry<E, AtomicIntegerCounter> entry) {
                return entry.getKey();
            }
        });
    }

    @Override
    public boolean add(E e) {
        if (!this.elementToCounterMap.containsKey(e)) {
            AtomicIntegerCounter counter = new AtomicIntegerCounter(counterInitialValue);
            this.elementToCounterMap.put(e, counter);
            this.elements.add(new Pair<E, AtomicIntegerCounter>(e, counter));
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return super.remove(o);
    }

    @Override
    public void clear() {
        elementToCounterMap.clear();
    }

    @Override
    public int size() {
        return this.elementToCounterMap.size();
    }

    public void increment(E e) {
        AtomicIntegerCounter counter = elementToCounterMap.get(e);
        if (counter == null) {
            return;
        }

        int count = counter.get();
        if (count + 1 >= counterMaxValue) {
            counter.increment(1);
            // 根据步长，进行计数器更新
            synchronized (this) {
                count = counter.get();
                if (count + 1 >= counterMaxValue) {
                    for (Map.Entry<E, AtomicIntegerCounter> entry : this.elementToCounterMap.entrySet()) {
                        AtomicIntegerCounter c = entry.getValue();
                        c.set(Maths.max(c.get() / counterUpdateStep, counterInitialValue));
                    }
                }
            }
        } else {
            counter.increment(1);
        }
    }
}
