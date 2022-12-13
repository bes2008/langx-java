package com.jn.langx.util.collection;

import com.jn.langx.Ordered;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.iter.MappingIterator;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.struct.counter.AtomicIntegerCounter;

import java.util.*;

public class PrioritySet<E> extends AbstractSet<E> {
    private static final Object X = new Object();

    private final class Wrapper<E> implements Comparable<Wrapper<E>>, Ordered {
        private E e;
        private AtomicIntegerCounter counter;

        Wrapper(E e, AtomicIntegerCounter counter) {
            this.e = e;
            this.counter = counter;
        }

        @Override
        public int getOrder() {
            return counter.get();
        }

        @Override
        public int compareTo(Wrapper<E> o) {
            if (this.equals(o)) {
                return 0;
            }
            int r = this.getOrder() - o.getOrder();
            if (r == 0) {
                r = 1;
            }
            return r;
        }

        @Override
        public int hashCode() {
            return e.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj.getClass() != Wrapper.class) {
                return false;
            }
            Wrapper<E> that = (Wrapper<E>) obj;
            if (this.e == that.e) {
                return true;
            }
            return this.e.equals(that.e);
        }

        @Override
        public String toString() {
            return "{" +
                    "e=" + e +
                    ", count=" + getOrder() +
                    '}';
        }
    }

    /**
     * 计数器最大值
     */
    private int counterMaxValue;
    /**
     * 计数器更新步长, 当 step > 1 时，才会启用 达到最大上限时 更新所有计数器的逻辑
     */
    private int counterUpdateStep;

    private int counterInitialValue;

    private TreeMap<Wrapper<E>, Wrapper<E>> elements;


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
        this.counterUpdateStep = Maths.max(counterUpdateStep, 1);
        this.counterMaxValue = counterMaxValue > this.counterUpdateStep ? counterMaxValue : this.counterUpdateStep * 10;

        this.counterInitialValue = Maths.max(counterInitialValue, 0);
        this.elements = new TreeMap<Wrapper<E>, Wrapper<E>>();
        if (Objs.isNotEmpty(source)) {
            addAll(source);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new MappingIterator<Wrapper<E>, E>(this.elements.keySet(), new Function<Wrapper<E>, E>() {
            @Override
            public E apply(Wrapper<E> we) {
                return we.e;
            }
        });
    }

    @Override
    public boolean add(E e) {
        Wrapper<E> we = new Wrapper<E>(e, new AtomicIntegerCounter(counterInitialValue));
        if (!this.elements.containsKey(we)) {
            this.elements.put(we, we);
        }
        return true;
    }

    @Override
    public void clear() {
        this.elements.clear();
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    public void increment(E e) {
        Wrapper<E> key = new Wrapper<E>(e, new AtomicIntegerCounter(counterInitialValue));
        Wrapper<E> we = this.elements.get(key);
        if (we == null) {
            return;
        }

        AtomicIntegerCounter counter = we.counter;
        int count = counter.get();

        // 达到上限时， 更新所有的计数器值
        if (counterUpdateStep > 1 && count + 1 >= counterMaxValue) {
            counter.increment(1);
            // 根据步长，进行计数器更新
            synchronized (this) {
                count = counter.get();
                if (count + 1 >= counterMaxValue) {
                    for (Wrapper<E> wrapper : Collects.asList(this.elements.keySet())) {
                        AtomicIntegerCounter c = wrapper.counter;
                        int newValue = Maths.max(c.get() / counterUpdateStep, counterInitialValue);
                        c.set(newValue);
                    //    this.elements.remove(wrapper);
                    //    this.elements.put(wrapper, wrapper);
                    }
                }
            }
        } else {
            int sizeBeforeDelete = this.elements.size();
            this.elements.remove(we);
            int sizeAfterDelete = this.elements.size();
            if(sizeAfterDelete==sizeBeforeDelete){
                System.out.println(1);
            }
            counter.increment(1);
            this.elements.put(we, we);
        }
    }

    public Map<E, Integer> showPriority() {
        Map<E, Integer> m = new LinkedHashMap<E, Integer>();
        for (Wrapper<E> we : this.elements.keySet()) {
            m.put(we.e, we.counter.get());
        }
        return m;
    }
}
