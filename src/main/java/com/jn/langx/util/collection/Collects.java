package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.diff.CollectionDiffer;
import com.jn.langx.util.collection.diff.KeyBuilder;
import com.jn.langx.util.collection.diff.MapDiffer;
import com.jn.langx.util.collection.iter.EnumerationIterable;
import com.jn.langx.util.collection.iter.IteratorIterable;
import com.jn.langx.util.collection.iter.WrappedIterable;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.function.*;
import com.jn.langx.util.reflect.type.Primitives;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.struct.Pair;

import java.util.*;

/**
 * Collection utilities
 */
@SuppressWarnings({"unchecked", "unused"})
public class Collects {
    /**
     * Get a empty, mutable java.util.Hashtable
     *
     * @param <K> Key
     * @param <V> Value
     * @return An empty, mutable java.util.Hashtable
     */
    public static <K, V> Hashtable emptyHashtable() {
        return new Hashtable<K, V>();
    }

    /**
     * Get a empty, mutable java.util.TreeMap
     *
     * @param <K> Key
     * @param <V> Value
     * @return An empty, mutable java.util.TreeMap
     */
    public static <K, V> Map<K, V> emptyTreeMap() {
        return new TreeMap<K, V>();
    }

    /**
     * Get a empty, mutable java.util.TreeMap
     *
     * @param <K> Key
     * @param <V> Value
     * @return An empty, mutable java.util.TreeMap
     */
    public static <K, V> Map<K, V> emptyTreeMap(@Nullable Comparator<K> comparator) {
        if (comparator == null) {
            return emptyTreeMap();
        }
        return new TreeMap<K, V>(comparator);
    }

    /**
     * Get a empty, mutable java.util.HashMap
     *
     * @param <K> Key
     * @param <V> Value
     * @return An empty, mutable java.util.HashMap
     */
    public static <K, V> Map<K, V> emptyHashMap() {
        return new HashMap<K, V>();
    }

    /**
     * Get a empty, mutable java.util.HashMap or java.util.LinkedHashMap
     *
     * @param <K> Key
     * @param <V> Value
     * @return An empty, mutable java.util.HashMap if is not sequential, else an empty, mutable java.util.LinkedHashMap
     */
    public static <K, V> Map<K, V> emptyHashMap(boolean sequential) {
        return sequential ? new LinkedHashMap<K, V>() : new HashMap<K, V>();
    }

    /**
     * Get a empty, mutable java.util.HashSet
     *
     * @param <E> Element
     * @return An empty, mutable java.util.HashSet
     */
    public static <E> HashSet<E> emptyHashSet() {
        return emptyHashSet(false);
    }

    /**
     * Get a empty, mutable java.util.HashSet or java.util.LinkedHashSet
     *
     * @param <E> Element
     * @return An empty, mutable java.util.HashSet if is not sequential, else an empty, mutable java.util.LinkedHashSet
     */
    public static <E> HashSet<E> emptyHashSet(boolean sequential) {
        return sequential ? new LinkedHashSet<E>() : new HashSet<E>();
    }


    /**
     * Get a empty, mutable java.util.TreeSet
     *
     * @param <E> Element
     * @return An empty, mutable java.util.TreeSet
     */
    public static <E> Set<E> emptyTreeSet() {
        return new TreeSet<E>();
    }

    public static <E> Set<E> emptyTreeSet(Comparator<E> comparator) {
        if (comparator == null) {
            return emptyTreeSet();
        }
        return new TreeSet<E>(comparator);
    }


    /**
     * Get a empty, mutable java.util.ArrayList
     *
     * @param <E> Element
     * @return An empty, mutable java.util.ArrayList
     */
    public static <E> List<E> emptyArrayList() {
        return new ArrayList<E>();
    }

    /**
     * Get a empty, mutable java.util.LinkedList
     *
     * @param <E> Element
     * @return An empty, mutable java.util.LinkedList
     */
    public static <E> List<E> emptyLinkedList() {
        return new LinkedList<E>();
    }

    public static <E> E[] emptyArray(@Nullable Class<E> componentType) {
        return Arrs.createArray(Primitives.wrap(componentType), 0);
    }


    public enum MapType {
        StringMap,
        HashMap,
        TreeMap,
        LinkedHashMap,
        IdentityHashMap,
        Hashtable,
        Properties;

        public static MapType ofMap(@Nullable Map map) {
            if (map == null) {
                return HashMap;
            }
            if (map instanceof com.jn.langx.util.collection.StringMap) {
                return StringMap;
            }
            if (map instanceof Properties) {
                return Properties;
            }
            if (map instanceof Hashtable) {
                return Hashtable;
            }
            if (map instanceof IdentityHashMap) {
                return IdentityHashMap;
            }
            if (map instanceof LinkedHashMap) {
                return LinkedHashMap;
            }
            if (map instanceof TreeMap) {
                return TreeMap;
            }
            return HashMap;
        }
    }

    /**
     * Create an empty map when the specified map is null.
     * using it, you can avoid NPE
     *
     * @return a new, empty map when the specified is null, others, return the argument
     */
    public static <K, V> Map<K, V> getEmptyMapIfNull(@Nullable Map<K, V> map) {
        return getEmptyMapIfNull(map, null);
    }

    /**
     * @see #getEmptyMapIfNull(Map, MapType)
     */
    public static <K, V> Map<K, V> getEmptyMapIfNull(@Nullable Map<K, V> map, @Nullable MapType mapType) {
        if (map == null) {
            if (mapType == null) {
                return emptyHashMap();
            }
            Map map1;
            switch (mapType) {
                case StringMap:
                    map1 = new StringMap();
                    break;
                case HashMap:
                    map1 = emptyHashMap();
                    break;
                case TreeMap:
                    map1 = emptyTreeMap();
                    break;
                case LinkedHashMap:
                    map1 = emptyHashMap(true);
                    break;
                case IdentityHashMap:
                    map1 = new IdentityHashMap();
                    break;
                case Hashtable:
                    map1 = emptyHashtable();
                    break;
                case Properties:
                    map1 = new Properties();
                    break;
                default:
                    map1 = emptyHashMap();
                    break;
            }
            return map1;
        }
        return map;
    }

    public enum SetType {
        HashSet,
        LinkedHashSet,
        TreeSet;

        public static SetType ofSet(@Nullable Set set) {
            if (set == null) {
                return HashSet;
            }
            if (set instanceof TreeSet) {
                return TreeSet;
            }
            if (set instanceof LinkedHashSet) {
                return LinkedHashSet;
            }
            return HashSet;
        }
    }

    /**
     * Avoid NPE, create an empty, new set when the specified set is null
     */
    public static <E> Set<E> getEmptySetIfNull(@Nullable Set<E> set) {
        return getEmptySetIfNull(set);
    }

    /**
     * @see #getEmptySetIfNull(Set)
     */
    public static <E> Set<E> getEmptySetIfNull(@Nullable Set<E> set, @Nullable SetType setType) {
        if (set == null) {
            if (setType == null) {
                return emptyHashSet();
            }
            switch (setType) {
                case HashSet:
                    set = emptyHashSet();
                    break;
                case TreeSet:
                    set = emptyTreeSet();
                    break;
                case LinkedHashSet:
                    set = emptyHashSet(true);
                    break;
                default:
                    set = emptyHashSet();
                    break;
            }
        }
        return set;
    }

    public enum ListType {
        ArrayList,
        LinkedList;

        public static ListType ofList(@Nullable List list) {
            if (list == null) {
                return ArrayList;
            }
            if (list instanceof LinkedList) {
                return LinkedList;
            }
            return ArrayList;
        }
    }

    /**
     * Avoid NPE, create an empty, new list when the specified list is null
     */
    public static <E> List<E> getEmptyListIfNull(@Nullable List<E> list) {
        return getEmptyListIfNull(list, null);
    }

    /**
     * @see #getEmptyListIfNull(List)
     */
    public static <E> List<E> getEmptyListIfNull(@Nullable List<E> list, @Nullable ListType listType) {
        if (list == null) {
            if (listType == null) {
                return emptyArrayList();
            }
            switch (listType) {
                case LinkedList:
                    list = emptyLinkedList();
                    break;
                case ArrayList:
                    list = emptyArrayList();
                    break;
                default:
                    list = emptyArrayList();
                    break;
            }
        }
        return list;
    }

    /**
     * Convert an array to a ArrayList
     */
    public static <E> List<E> asList(@Nullable E[] array) {
        return asList(array, true, ListType.ArrayList);
    }

    /**
     * Convert an array to a ArrayList or a LinkedList
     */
    public static <E> List<E> asList(@Nullable E[] array, @Nullable ListType listType) {
        return asList(array, true, listType);
    }

    /**
     * Convert an array to a List, if the 'mutable' argument is true, will return an unmodifiable List
     */
    public static <E> List<E> asList(@Nullable E[] array, boolean mutable, @Nullable ListType listType) {
        List<E> immutableList = Emptys.isEmpty(array) ? Collections.<E>emptyList() : Arrays.asList(array);
        if (listType == null) {
            listType = ListType.ArrayList;
        }
        List<E> list;
        switch (listType) {
            case LinkedList:
                list = new LinkedList<E>(immutableList);
                break;
            case ArrayList:
                list = new ArrayList<E>(immutableList);
                break;
            default:
                list = new ArrayList<E>(immutableList);
                break;
        }
        if (!mutable) {
            list = Collections.unmodifiableList(list);
        }
        return list;
    }

    public static <E> E[] toArray(@Nullable Collection<E> list) {
        return toArray(list, null);
    }

    public static <E> List<E> asList(Iterable<E> iterable) {
        return asList(iterable, true);
    }

    public static <E> List<E> asList(Iterable<E> iterable, boolean mutable) {
        Preconditions.checkNotNull(iterable);
        if (!(iterable instanceof List)) {
            asList(collect(iterable, Collectors.toList()), mutable);
        }
        List<E> list = (List<E>) iterable;
        if (!mutable) {
            return Collections.unmodifiableList(list);
        }
        return list;
    }

    /**
     * Convert a list to an array
     */
    public static <E> E[] toArray(@Nullable Collection<E> list, @Nullable Class<E[]> clazz) {
        if (Emptys.isEmpty(list)) {
            list = Collections.emptyList();
        }
        if (clazz == null) {
            return (E[]) list.toArray();
        }
        // Make a new array of the specified class
        return (E[]) Arrays.copyOf(list.toArray(), list.size(), clazz);
    }

    /**
     * Convert any object to an immutable Iterable
     */
    public static <E> Iterable<E> asIterable(@Nullable Object object) {
        return asIterable(object, false);
    }

    /**
     * Convert any object to Iterable
     */
    public static <E> Iterable<E> asIterable(@Nullable Object object, boolean mutable) {
        if (Emptys.isNull(object)) {
            return asList(null, mutable, null);
        }

        if (Arrs.isArray(object)) {
            return asList((E[]) object, mutable, null);
        }

        if (object instanceof Iterable) {
            if (!mutable) {
                return new WrappedIterable<E>((Iterable) object, mutable);
            }
            return (Iterable) object;
        }

        if (object instanceof Map) {
            return (Iterable<E>) asList(Arrs.wrapAsArray(object), mutable, null);
        }

        if (object instanceof Iterator) {
            return new IteratorIterable<E>((Iterator<E>) object, mutable);
        }

        if (object instanceof Enumeration) {
            return new EnumerationIterable<E>((Enumeration<E>) object);
        }

        return (Iterable<E>) asList(Arrs.wrapAsArray(object), mutable, null);
    }

    /**
     * Filter any object with the specified predicate
     */
    public static <E> List<E> filter(@Nullable Object anyObject, @NonNull final Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        final List<E> result = emptyArrayList();
        forEach(iterable, new Consumer<E>() {
            @Override
            public void accept(E e) {
                if (predicate.test(e)) {
                    result.add(e);
                }
            }
        });
        return result;
    }

    /**
     * Filter a map with the specified predicate
     */
    public static <K, V> Map<K, V> filter(@Nullable Map<K, V> map, @NonNull final Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        final Map<K, V> result = getEmptyMapIfNull(null, MapType.ofMap(map));
        if (Emptys.isNotEmpty(map)) {
            forEach(map, new Consumer2<K, V>() {
                @Override
                public void accept(K key, V value) {
                    if (predicate.test(key, value)) {
                        result.put(key, value);
                    }
                }
            });
        }
        return result;
    }

    /**
     * mapping an iterable to a list
     */
    public static <E, R> List<R> map(@Nullable Object anyObject, @NonNull final Function<E, R> mapper) {
        Preconditions.checkNotNull(mapper);
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        final List<R> result = emptyArrayList();
        forEach(iterable, new Consumer<E>() {
            @Override
            public void accept(E e) {
                result.add(mapper.apply(e));
            }
        });
        return result;
    }


    /**
     * mapping an iterable to a map
     */
    public static <E, K, V> Map<K, V> map(@Nullable Object anyObject, @NonNull final Mapper<E, Pair<K, V>> mapper) {
        Preconditions.checkNotNull(mapper);
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        final Map<K, V> result = emptyHashMap();
        forEach(iterable, new Consumer<E>() {
            @Override
            public void accept(E e) {
                Pair<K, V> pair = mapper.apply(e);
                result.put(pair.getKey(), pair.getValue());
            }
        });
        return result;
    }

    /**
     * mapping aMap to a list
     */
    public static <K, V, R> List<R> map(@Nullable Map<K, V> map, @NonNull final Function2<K, V, R> mapper) {
        Preconditions.checkNotNull(mapper);
        final List<R> result = emptyArrayList();
        forEach(map, new Consumer2<K, V>() {
            @Override
            public void accept(K key, V value) {
                result.add(mapper.apply(key, value));
            }
        });
        return result;
    }

    /**
     * mapping aMap to bMap
     */
    public static <K, V, K1, V1> Map<K1, V1> map(@Nullable Map<K, V> map, @NonNull final Mapper2<K, V, Pair<K1, V1>> mapper) {
        Preconditions.checkNotNull(mapper);
        final Map<K1, V1> result = getEmptyMapIfNull(null, MapType.ofMap(map));
        if (Emptys.isNotEmpty(map)) {
            forEach(map.entrySet(), new Consumer<Map.Entry<K, V>>() {
                @Override
                public void accept(Map.Entry<K, V> entry) {
                    Pair<K1, V1> e = mapper.apply(entry.getKey(), entry.getValue());
                    result.put(e.getKey(), e.getValue());
                }
            });
        }
        return result;
    }

    /**
     * map a collection to another, flat it
     */
    public static <E, R0, R> List<R> flatMap(@Nullable Object anyObject, @NonNull Function<E, R0> mapper) {
        List<R0> mapped = map(anyObject, mapper);
        final List<R> list = emptyArrayList();
        forEach(mapped, new Consumer<R0>() {
            @Override
            public void accept(R0 r) {
                forEach(r, new Consumer<R>() {
                    @Override
                    public void accept(R o) {
                        list.add(o);
                    }
                });
            }
        });
        return list;
    }

    /**
     * Iterate every element
     */
    public static <E> void forEach(@Nullable Object anyObject, @NonNull Consumer<E> consumer) {
        Preconditions.checkNotNull(consumer);
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        for (E e : iterable) {
            consumer.accept(e);
        }
    }

    /**
     * Iterate every element
     */
    public static <E> void forEach(@Nullable E[] array, @NonNull Consumer2<Integer, E> consumer) {
        Preconditions.checkNotNull(consumer);
        if (Emptys.isNotEmpty(array)) {
            for (int i = 0; i < array.length; i++) {
                consumer.accept(i, array[i]);
            }
        }
    }

    /**
     * Iterate every element
     */
    public static <K, V> void forEach(@Nullable Map<K, V> map, @NonNull Consumer2<K, V> consumer) {
        Preconditions.checkNotNull(consumer);
        if (Emptys.isNotEmpty(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                consumer.accept(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * find the first matched element, null if not found
     */
    public static <E> E findFirst(@Nullable Object anyObject, @Nullable Predicate<E> predicate) {
        if (Emptys.isNull(anyObject)) {
            return null;
        }
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        if (predicate != null) {
            for (E e : iterable) {
                if (predicate.test(e)) {
                    return e;
                }
            }
        } else {
            Iterator<E> iterator = iterable.iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
            return null;
        }

        return null;
    }

    /**
     * find the first matched element, null if not found
     */
    public static <K, V> Map.Entry<K, V> findFirst(@Nullable Map<K, V> map, @Nullable Predicate2<K, V> predicate) {
        if (map == null) {
            return null;
        }
        if (Emptys.isNotEmpty(map)) {
            if (predicate != null) {
                for (Map.Entry<K, V> entry : map.entrySet()) {
                    if (predicate.test(entry.getKey(), entry.getValue())) {
                        return entry;
                    }
                }
            } else {
                Set<Map.Entry<K, V>> set = map.entrySet();
                List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(set);
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * remove all elements that match the condition
     *
     * @return whether has any element removed
     * @throws UnsupportedOperationException, NullPointException
     */
    public static <E> boolean removeIf(@Nullable Collection<E> collection, @NonNull Predicate<E> predicate) {
        boolean hasRemoved = false;
        if (Emptys.isNotEmpty(collection)) {
            hasRemoved = removeIf(collection.iterator(), predicate);
        }
        return hasRemoved;
    }

    public static <E> boolean removeIf(@Nullable Iterator<E> iterator, @NonNull Predicate<E> predicate) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkNotNull(predicate);
        boolean hasRemoved = false;
        while (iterator.hasNext()) {
            E e = iterator.next();
            if (predicate.test(e)) {
                iterator.remove();
                hasRemoved = true;
            }
        }
        return hasRemoved;
    }

    /**
     * remove all elements that match the map
     *
     * @return whether has any element removed
     * @throws UnsupportedOperationException, NullPointException
     */
    public static <K, V> boolean removeIf(@Nullable Map<K, V> map, @NonNull Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        boolean hasRemoved = false;
        if (Emptys.isNotEmpty(map)) {
            Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<K, V> e = iterator.next();
                if (predicate.test(e.getKey(), e.getValue())) {
                    iterator.remove();
                    hasRemoved = true;
                }
            }
        }
        return hasRemoved;
    }


    /**
     * has any element match the specified condition
     *
     * @return whether has any element removed
     */
    public static <E> boolean anyMatch(@Nullable Collection<E> collection, @NonNull Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(collection)) {
            E e = findFirst(collection, predicate);
            return e != null;
        }
        return false;
    }

    /**
     * has any element match the specified condition
     *
     * @return whether has any element removed
     */
    public static <K, V> boolean anyMatch(@Nullable Map<K, V> map, @NonNull Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(map)) {
            Map.Entry<K, V> entry = findFirst(map, predicate);
            return entry != null;
        }
        return false;
    }

    /**
     * whether all elements match the specified condition or not
     *
     * @return whether has any element removed
     */
    public static <E> boolean allMatch(@Nullable Collection<E> collection, @NonNull Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(collection)) {
            for (E e : collection) {
                if (!predicate.test(e)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * whether all elements match the specified condition or not
     *
     * @return whether has any element removed
     */
    public static <K, V> boolean allMatch(@Nullable Map<K, V> map, @NonNull Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(map)) {
            for (Map.Entry<K, V> e : map.entrySet()) {
                if (!predicate.test(e.getKey(), e.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * has no any element match the specified condition ?
     *
     * @return whether has any element removed
     */
    public static <E> boolean noneMatch(@Nullable Collection<E> collection, @NonNull Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(collection)) {
            Iterator<E> iterator = collection.iterator();
            for (E e : collection) {
                if (predicate.test(e)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * has no any element match the specified condition ?
     *
     * @return whether has any element removed
     */
    public static <K, V> boolean noneMatch(@Nullable Map<K, V> map, @NonNull Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(map)) {
            for (Map.Entry<K, V> e : map.entrySet()) {
                if (predicate.test(e.getKey(), e.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <E> Set<E> distinct(@Nullable Collection<E> collection) {
        return new LinkedHashSet<E>(Emptys.isEmpty(collection) ? Collections.EMPTY_LIST : collection);
    }

    /**
     * truncate a collection using subList(0, maxSize)
     */
    public static <E> List<E> limit(@Nullable Collection<E> collection, int maxSize) {
        if (Emptys.isEmpty(collection)) {
            return emptyLinkedList();
        }

        Preconditions.checkArgument(maxSize >= 0);

        List<E> list = (collection instanceof List) ? (List<E>) collection : new LinkedList<E>(collection);
        if (list.size() <= maxSize) {
            return list;
        }
        return list.subList(0, maxSize);
    }

    /**
     * skip n elements, get a collection using subList(n, size)
     */
    public static <E> List<E> skip(@Nullable Collection<E> collection, int n) {
        if (Emptys.isEmpty(collection)) {
            return emptyLinkedList();
        }

        Preconditions.checkArgument(n >= 0);

        List<E> list = (collection instanceof List) ? (List<E>) collection : new LinkedList<E>(collection);
        if (list.size() <= n) {
            return emptyArrayList();
        }
        return list.subList(n, list.size());
    }

    /**
     * Concat two collection to one
     */
    public static <E> Collection<E> concat(@Nullable Collection<E> c1, @Nullable Collection<E> c2, boolean newOne) {
        if (newOne) {
            List<E> l = emptyArrayList();
            if (Emptys.isNotEmpty(c1)) {
                l.addAll(c1);
            }
            if (Emptys.isNotEmpty(c2)) {
                l.addAll(c2);
            }
            return l;
        } else {
            Preconditions.checkNotNull(c1);
            if (Emptys.isNotEmpty(c2)) {
                c1.addAll(c2);
            }
            return c1;
        }
    }

    public static <E extends Comparable<E>> List<E> sort(@Nullable Collection<E> collection, boolean reverse) {
        return sort(collection, new ComparableComparator<E>(), reverse);
    }

    /**
     * sort a collection, return an new list. it is different to
     * Collections.sort(list) is that : Collections.sort() return void
     */
    public static <E> List<E> sort(@Nullable Collection<E> collection, @NonNull Comparator<E> comparator) {
        return sort(collection, comparator, false);
    }

    public static <E> List<E> sort(@Nullable Collection<E> collection, @NonNull Comparator<E> comparator, boolean reverse) {
        Preconditions.checkNotNull(comparator);
        List<E> newList = new LinkedList<E>();
        if (Emptys.isEmpty(collection)) {
            return newList;
        } else {
            newList.addAll(collection);
            Collections.sort(newList, reverse ? Collections.reverseOrder(comparator) : comparator);
            return newList;
        }
    }

    public static <K, V> Map<K, V> sort(@Nullable Map<K, V> map, @NonNull Comparator<K> comparator) {
        Preconditions.checkNotNull(comparator);
        Map<K, V> result = emptyTreeMap(comparator);
        if (Emptys.isNotEmpty(map)) {
            result.putAll(map);
        }
        return result;
    }

    /**
     * Reverse a list, return an new list when the argument 'newOne' is true
     */
    public static <E> List<E> reverse(@Nullable List<E> list, boolean newOne) {
        if (Emptys.isEmpty(list)) {
            return (list == null || newOne) ? Collects.<E>emptyArrayList() : list;
        }
        if (!newOne) {
            Collections.reverse(list);
            return list;
        } else {
            List<E> newList = new ArrayList<E>();
            int i = list.size() - 1;
            while (i >= 0) {
                newList.add(list.get(i));
                i--;
            }
            return newList;
        }
    }

    public static <K, V> int count(Map<K, V> map) {
        return Emptys.isEmpty(map) ? 0 : map.size();
    }

    public static <E> int count(Collection<E> collection) {
        return Emptys.isEmpty(collection) ? 0 : collection.size();
    }

    public static int count(Object anyObject) {
        final Holder<Integer> count = new Holder<Integer>(0);
        forEach(anyObject, new Consumer<Object>() {
            @Override
            public void accept(Object object) {
                count.set(count.get() + 1);
            }
        });
        return count.get();
    }

    public static <E> E max(Object object, final Comparator<E> comparator) {
        Iterable<E> iterable = (Iterable<E>) object;
        int count = count(iterable);
        if (count == 0) {
            return null;
        }
        if (count == 1) {
            return iterable.iterator().next();
        }
        final Holder<E> max = new Holder<E>();

        forEach(iterable, new Consumer<E>() {
            @Override
            public void accept(E e) {
                if (max.get() == null) {
                    max.set(e);
                } else {
                    int delta = comparator.compare(e, max.get());
                    if (delta > 0) {
                        max.set(e);
                    }
                }
            }
        });
        return max.get();
    }

    public static <E> E min(Object object, final Comparator<E> comparator) {
        Iterable<E> iterable = (Iterable<E>) object;
        int count = count(iterable);
        if (count == 0) {
            return null;
        }
        if (count == 1) {
            return iterable.iterator().next();
        }
        final Holder<E> min = new Holder<E>();

        forEach(iterable, new Consumer<E>() {
            @Override
            public void accept(E e) {
                if (min.get() == null) {
                    min.set(e);
                } else {
                    int delta = comparator.compare(e, min.get());
                    if (delta < 0) {
                        min.set(e);
                    }
                }
            }
        });
        return min.get();
    }

    public static <E, R> R collect(Object anyObject, Collector<E, R> collector) {
        Preconditions.checkNotNull(collector);
        return collect(anyObject, collector.supplier(), collector.accumulator());
    }

    public static <E, R> R collect(Object anyObject, Supplier0<R> containerFactory, final Consumer2<R, E> consumer) {
        Preconditions.checkNotNull(containerFactory);
        Preconditions.checkNotNull(consumer);
        final R container = containerFactory.get();
        forEach(anyObject, new Consumer<E>() {
            @Override
            public void accept(E e) {
                consumer.accept(container, e);
            }
        });
        return container;
    }

    public static <E> DiffResult<Collection<E>> diff(@Nullable Collection<E> oldCollection, @Nullable Collection<E> newCollection) {
        return diff(oldCollection, newCollection, null);
    }

    public static <E> DiffResult<Collection<E>> diff(@Nullable Collection<E> oldCollection, @Nullable Collection<E> newCollection, @Nullable Comparator<E> elementComparator) {
        return diff(oldCollection, newCollection, elementComparator);
    }

    public static <E> DiffResult<Collection<E>> diff(@Nullable Collection<E> oldCollection, @Nullable Collection<E> newCollection, @Nullable Comparator<E> elementComparator, @Nullable KeyBuilder<String, E> keyBuilder) {
        CollectionDiffer<E> differ = new CollectionDiffer<E>();
        differ.setComparator(elementComparator);
        if (keyBuilder != null) {
            differ.diffUsingMap(keyBuilder);
        }
        return differ.diff(oldCollection, newCollection);
    }

    public static <K, V> DiffResult<Map<K, V>> diff(@Nullable Map<K, V> oldMap, @Nullable Map<K, V> newMap) {
        return diff(oldMap, newMap, null);
    }

    public static <K, V> DiffResult<Map<K, V>> diff(@Nullable Map<K, V> oldMap, @Nullable Map<K, V> newMap, @Nullable Comparator<V> valueComparator) {
        return diff(oldMap, newMap, valueComparator, null);
    }

    public static <K, V> DiffResult<Map<K, V>> diff(@Nullable Map<K, V> oldMap, @Nullable Map<K, V> newMap, @Nullable Comparator<V> valueComparator, @Nullable Comparator<K> keyComparator) {
        MapDiffer<K, V> differ = new MapDiffer<K, V>();
        differ.setComparator(valueComparator);
        differ.setKeyComparator(keyComparator);
        return differ.diff(oldMap, newMap);
    }

    public static Map<String, String> propertiesToStringMap(@Nullable Properties properties) {
        return propertiesToStringMap(properties, false);
    }

    public static Map<String, String> propertiesToStringMap(@Nullable Properties properties, boolean sort) {
        return propertiesToStringMap(properties, Comparators.STRING_COMPARATOR_IGNORE_CASE);
    }

    public static Map<String, String> propertiesToStringMap(@Nullable Properties properties, @Nullable Comparator<String> keyComparator) {
        final Map<String, String> map = keyComparator != null ? new TreeMap<String, String>(keyComparator) : new StringMap();
        if (Emptys.isNotEmpty(properties)) {
            Collects.forEach(properties, new Consumer2<Object, Object>() {
                @Override
                public void accept(Object key, Object value) {
                    map.put(key.toString(), value.toString());
                }
            });
        }
        return map;
    }
}
