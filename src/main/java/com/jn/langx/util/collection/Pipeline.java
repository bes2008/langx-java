package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.*;

import java.util.*;

/**
 * @author jinuo.fang
 */
public class Pipeline<E> {
    private Collection<E> collection;

    public <C extends Collection<E>> Pipeline(C collection) {
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
        maxSize = maxSize < 0 ? 0 : maxSize;
        Collection<E> list = Collects.limit(this.collection, maxSize);
        return new Pipeline<E>(list);
    }

    public Pipeline<E> skip(int n) {
        n = n < 0 ? 0 : n;
        Collection<E> list = Collects.skip(this.collection, n);
        return new Pipeline<E>(list);
    }

    public Pipeline<E> sorted(@NonNull Comparator<E> comparator) {
        return new Pipeline<E>(Collects.sort(this.collection, comparator));
    }

    public Pipeline<E> sort(@NonNull Comparator<E> comparator) {
        return new Pipeline<E>(Collects.sort(this.collection, comparator));
    }

    public Pipeline<E> distinct() {
        return new Pipeline<E>(Collects.distinct(this.collection));
    }

    public void forEach(@NonNull Consumer<E> consumer) {
        Collects.forEach(this.collection, consumer);
    }

    public void forEach(@NonNull Consumer<E> consumer, @Nullable Predicate<E> breakPredicate) {
        Collects.forEach(this.collection, consumer, breakPredicate);
    }

    public void forEach(@NonNull Consumer2<Integer, E> consumer, @Nullable Predicate2<Integer, E> breakPredicate) {
        Collects.forEach(this.collection, consumer, breakPredicate);
    }

    public boolean anyMatch(@NonNull Predicate<E> predicate) {
        return Collects.anyMatch(this.collection, predicate);
    }

    public boolean allMatch(@NonNull Predicate<E> predicate) {
        return Collects.allMatch(this.collection, predicate);
    }

    public boolean noneMatch(@NonNull Predicate<E> predicate) {
        return Collects.noneMatch(this.collection, predicate);
    }

    public E findFirst() {
        return Collects.findFirst(this.collection, null);
    }

    public E findFirst(@NonNull Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        return Collects.findFirst(this.collection, predicate);
    }

    public Pipeline<E> findN(int n) {
        return of(Collects.findN(this.collection, null, n));
    }

    public Pipeline<E> findN(@NonNull Predicate<E> predicate, int n) {
        Preconditions.checkNotNull(predicate);
        return of(Collects.findN(this.collection, predicate, n));
    }

    public Pipeline<E> clearNulls() {
        return new Pipeline<E>(Collects.clearNulls(this.collection));
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

    public Double sum() {
        return map(new Function<E, Double>() {
            @Override
            public Double apply(E e) {
                if (e instanceof Number) {
                    return ((Number) e).doubleValue();
                }
                return 0d;
            }
        }).reduce(new Operator2<Double>() {
            @Override
            public Double apply(Double input1, Double input2) {
                return input1 + input2;
            }
        });
    }

    public Double average() {
        if (count() > 0) {
            return sum() / count();
        }
        return 0d;
    }

    public E max(Comparator<E> comparator) {
        return Collects.max(this.collection, comparator);
    }

    public E min(Comparator<E> comparator) {
        return Collects.min(this.collection, comparator);
    }

    public E reduce(Operator2<E> operator) {
        return Collects.reduce(collection, operator);
    }

    public <K> Map<K, List<E>> groupBy(Function<E, K> classifier, Supplier0<Map<K, List<E>>> mapFactory) {
        return Collects.groupBy(this.collection, classifier, mapFactory);
    }

    public <R> R collect(Collector<E, R> collector) {
        return Collects.collect(this.collection, collector);
    }

    public Pipeline<E> concat(Pipeline<E> another) {
        return concat(another.collection);
    }

    public <C extends Collection<E>> Pipeline<E> concat(C another) {
        if (another != null) {
            return new Pipeline<E>(Collects.concat(this.collection, another));
        }
        return this;
    }

    public Pipeline<E> listized() {
        return new Pipeline<E>(collect(Collects.<E>toList()));
    }

    public <C extends Collection<E>> void addTo(final C collection) {
        Preconditions.checkNotNull(collection);
        forEach(new Consumer<E>() {
            @Override
            public void accept(E e) {
                collection.add(e);
            }
        });
    }

    public Pipeline<E> add(E e) {
        List<E> list = asList();
        list.add(e);
        return new Pipeline<E>(list);
    }

    public <C extends Collection<E>> Pipeline<E> addAll(C collection) {
        if (collection == null) {
            return this;
        }
        this.collection.addAll(collection);
        return this;
    }

    public List<E> asList() {
        return new ArrayList<E>(getAll());
    }

    public static <T> Pipeline<T> of(Object anyObject) {
        Collection<T> list = Collects.<T>asCollection(Collects.<T>asIterable(anyObject));
        return new Pipeline<T>(list);
    }

    public static <T> Pipeline<T> of(Iterable<T> iterable) {
        Collection<T> list = Collects.<T>asCollection(iterable);
        return new Pipeline<T>(list);
    }

    public static <T> Pipeline<T> of(T... array) {
        Collection<T> list = Collects.<T>asCollection(Collects.<T>asIterable(array));
        return new Pipeline<T>(list);
    }

}
