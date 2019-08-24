package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Collector;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.struct.Holder;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

/**
 * @author jinuo.fang
 */
public class Pipeline<E> {
    private Collection<E> collection;

    public Pipeline(Collection<E> collection) {
        Preconditions.checkNotNull(collection);
        this.collection = collection;
    }

    public <O> Pipeline<O> map(Function<E, O> mapper) {
        return new Pipeline<O>(Collects.map(this.collection, mapper));
    }

    public <I, O> Pipeline<O> flatMap(Function<I, O> mapper) {
        Collection<Collection<I>> c = (Collection<Collection<I>>) this.collection;
        Collection<O> list = Collects.flatMap(c, mapper);
        return new Pipeline<O>(list);
    }

    public Pipeline<E> filter(Predicate<E> predicate) {
        return new Pipeline<E>(Collects.filter(this.collection, predicate));
    }

    public Pipeline<E> limit(int maxSize) {
        maxSize = maxSize < 0 ? maxSize = 0 : maxSize;
        Collection<E> list = Collects.limit(this.collection, maxSize);
        return new Pipeline<E>(list);
    }

    public Pipeline skip(int n) {
        n = n < 0 ? 0 : n;
        Collection<E> list = Collects.skip(this.collection, n);
        return new Pipeline<E>(list);
    }

    public Pipeline<E> sorted(Comparator<E> comparator) {
        return new Pipeline<E>(Collects.sort(this.collection, comparator));
    }

    public Pipeline<E> distinct() {
        return new Pipeline<E>(Collects.distinct(this.collection));
    }

    public void forEach(Consumer<E> consumer) {
        Collects.forEach(this.collection, consumer);
    }

    public boolean anyMatch(Predicate<E> predicate) {
        return Collects.anyMatch(this.collection, predicate);
    }

    public boolean allMatch(Predicate<E> predicate) {
        return Collects.allMatch(this.collection, predicate);
    }

    public boolean noneMatch(Predicate<E> predicate) {
        return Collects.noneMatch(this.collection, predicate);
    }

    public E findFirst() {
        return Collects.findFirst(this.collection, null);
    }

    public E findFirst(@NonNull Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        return Collects.findFirst(this.collection, predicate);
    }

    public int count() {
        return this.collection.size();
    }

    public Object[] toArray() {
        return Collects.toArray(this.collection);
    }

    public E[] toArray(Class<E[]> clazz) {
        return Collects.toArray(this.collection, clazz);
    }

    public Iterator<E> iterator() {
        return this.collection.iterator();
    }

    public Collection<E> getAll() {
        return collection;
    }

    public Number sum() {
        final Holder<Double> sum = new Holder<Double>(0d);
        map(new Function<E, Double>() {
            @Override
            public Double apply(E e) {
                if (e instanceof Number) {
                    return ((Number) e).doubleValue();
                }
                return 0d;
            }
        }).forEach(new Consumer<Double>() {
            @Override
            public void accept(Double value) {
                sum.set(sum.get() + value);
            }
        });
        return sum.get();
    }

    public Number average() {
        if (count() > 0) {
            return sum().doubleValue() / count();
        }
        return 0;
    }

    public E max(Comparator<E> comparator) {
        return Collects.max(this.collection, comparator);
    }

    public E min(Comparator<E> comparator) {
        return Collects.min(this.collection, comparator);
    }

    public <R> R collect(Collector<E, R> collector) {
        return Collects.collect(this.collection, collector);
    }

    public Pipeline<E> concat(Pipeline<E> another){
        return concat(another.collection);
    }

    public Pipeline<E> concat(Collection<E> another){
        if(another!=null){
           return new Pipeline<E>(Collects.concat(this.collection, another)) ;
        }
        return this;
    }

    public Pipeline<E> listized() {
        return new Pipeline<E>(collect(Collects.<E>toList()));
    }

    public void addTo(final Collection<E> collection) {
        Preconditions.checkNotNull(collection);
        forEach(new Consumer<E>() {
            @Override
            public void accept(E e) {
                collection.add(e);
            }
        });
    }

    public static <T> Pipeline<T> of(Object anyObject) {
        Collection<T> list = Collects.<T>asCollection(Collects.<T>asIterable(anyObject));
        return new Pipeline<T>(list);
    }

    public static <T> Pipeline<T> of(Iterable<T> iterable) {
        Collection<T> list = Collects.<T>asCollection(iterable);
        return new Pipeline<T>(list);
    }

    public static <T> Pipeline<T> of(T[] array) {
        Collection<T> list = Collects.<T>asCollection(Collects.<T>asIterable(array));
        return new Pipeline<T>(list);
    }

}
