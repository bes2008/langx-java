package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;
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

    public <O> Pipeline<O> flat() {
        return flatMap(Functions.<O>noopFunction());
    }

    public <I, O> Pipeline<O> flatMap(Function<I, O> mapper) {
        Collection<Collection<I>> c = (Collection<Collection<I>>) this.collection;
        Collection<O> list = Collects.flatMap(c, mapper);
        return new Pipeline<O>(list);
    }

    public <O> O firstMap(@NonNull final Function2<Integer, E, O> mapper) {
        return Collects.firstMap(collection, mapper);
    }

    public <O> O firstMap(@NonNull final Function2<Integer, E, O> mapper, Predicate<O> breakPredicate) {
        return Collects.firstMap(collection, mapper, breakPredicate);
    }

    public <O> O firstMap(@NonNull final Function2<Integer, E, O> mapper, Predicate2<E,O> breakPredicate) {
        return Collects.firstMap(collection, mapper, breakPredicate);
    }

    public Pipeline<E> filter(Predicate<E> predicate) {
        return this.filter(predicate, null);
    }

    public Pipeline<E> filter(Predicate<E> predicate, @Nullable Predicate<E> breakPredicate) {
        return new Pipeline<E>(Collects.filter(this.collection, predicate, breakPredicate));
    }

    public Pipeline<E> filter(Predicate2<Integer, E> predicate) {
        return filter(predicate, null);
    }

    public Pipeline<E> filter(Predicate2<Integer, E> predicate, @Nullable Predicate2<Integer, E> breakPredicate) {
        return new Pipeline<E>(Collects.filter(this.collection, predicate, breakPredicate));
    }

    public Pipeline<E> limit(int maxSize) {
        maxSize = Maths.max(0, maxSize);
        Collection<E> list = Collects.limit(this.collection, maxSize);
        return new Pipeline<E>(list);
    }

    public Pipeline<E> skip(int n) {
        n = Maths.max(0, n);
        Collection<E> list = Collects.skip(this.collection, n);
        return new Pipeline<E>(list);
    }

    public <K> Pipeline<List<E>> partitionBy(Function<E, K> classifier) {
        return new Pipeline<List<E>>(Collects.partitionBy(this.collection, classifier));
    }

    public <K> Pipeline<List<E>> partitionBy(Function2<Integer, E, K> classifier) {
        return new Pipeline<List<E>>(Collects.partitionBy(this.collection, classifier));
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
        this.forEach(null, consumer, null);
    }

    public void forEach(@Nullable Predicate<E> consumePredicate, Consumer<E> consumer) {
        this.forEach(consumePredicate, consumer, null);
    }

    public void forEach(@NonNull Consumer<E> consumer, @Nullable Predicate<E> breakPredicate) {
        this.forEach(null, consumer, breakPredicate);
    }

    public void forEach(@Nullable Predicate<E> consumePredicate, @NonNull Consumer<E> consumer, @Nullable Predicate<E> breakPredicate) {
        Collects.forEach(this.collection, consumePredicate, consumer, breakPredicate);
    }

    public void forEach(@NonNull Consumer2<Integer, E> consumer) {
        this.forEach(consumer, null);
    }

    public void forEach(@NonNull Consumer2<Integer, E> consumer, @Nullable Predicate2<Integer, E> breakPredicate) {
        this.forEach(null, consumer, breakPredicate);
    }

    public void forEach(@Nullable Predicate2<Integer, E> consumePredicate, @NonNull Consumer2<Integer, E> consumer) {
        this.forEach(consumePredicate, consumer, null);
    }

    public void forEach(@Nullable Predicate2<Integer, E> consumePredicate, @NonNull Consumer2<Integer, E> consumer, @Nullable Predicate2<Integer, E> breakPredicate) {
        Collects.forEach(this.collection, consumePredicate, consumer, breakPredicate);
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

    public <K> Map<K, List<E>> groupBy(Function<E, K> classifier) {
        return Collects.groupBy(this.collection, classifier, null);
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

    public boolean contains(E e) {
        return Collects.contains(collection, e);
    }

    public Pipeline<E> subPipeline(int offset, int limit) {
        return new Pipeline<E>(Collects.skip(Collects.limit(collection, limit), offset));
    }

    public Pipeline<E> listized() {
        return new Pipeline<E>(collect(Collects.<E>toList()));
    }

    public <C extends Collection<E>> void addTo(@NonNull final C collection) {
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

    public Pipeline<E> remove(E e){
        List<E> list = asList();
        list.remove(e);
        return new Pipeline<E>(list);
    }

    public Pipeline<E> shuffle() {
        return shuffle(GlobalThreadLocalMap.getRandom());
    }

    public Pipeline<E> shuffle(Random random) {
        List<E> list = asList();
        Collects.shuffle(list, random);
        return new Pipeline<E>(list);
    }

    public Pipeline<E> reverse() {
        return reverse(false);
    }

    public Pipeline<E> reverse(boolean newOne) {
        if (this.collection instanceof List) {
            return of(Collects.reverse((List) this.collection, newOne));
        }
        return of(asList()).reverse(newOne);
    }

    public Pipeline<E> swap(int i, int j) {
        List<E> list = asList();
        Collects.swap(list, i, j);
        return new Pipeline<E>(list);
    }

    public <C extends Collection<E>> Pipeline<E> addAll(@Nullable C collection) {
        if (collection == null) {
            return this;
        }
        this.collection.addAll(collection);
        return this;
    }

    public Set<E> asSet(boolean sequential) {
        if (sequential && collection instanceof LinkedHashSet) {
            return (LinkedHashSet<E>) collection;
        }
        Set<E> set = Collects.emptyHashSet(sequential);
        set.addAll(getAll());
        return set;
    }


    public List<E> asList() {
        if (collection instanceof List) {
            return (List<E>) collection;
        }
        return new ArrayList<E>(getAll());
    }

    public static <T> Pipeline<T> of(@Nullable Object anyObject) {
        Collection<T> list = Collects.<T>asCollection(Collects.<T>asIterable(anyObject));
        return new Pipeline<T>(list);
    }

    public static <T> Pipeline<T> of(@Nullable Iterable<T> iterable) {
        Collection<T> list = Collects.<T>asCollection(iterable);
        return new Pipeline<T>(list);
    }

    public static <T> Pipeline<T> of(@Nullable T... array) {
        Collection<T> list = Collects.<T>asCollection(Collects.<T>asIterable(array));
        return new Pipeline<T>(list);
    }

}
