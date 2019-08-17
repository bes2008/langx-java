package com.jn.langx.util;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.collection.DiffResult;
import com.jn.langx.util.collection.StringMap;
import com.jn.langx.util.collection.diff.CollectionDiffer;
import com.jn.langx.util.collection.diff.KeyBuilder;
import com.jn.langx.util.collection.diff.MapDiffer;
import com.jn.langx.util.collection.iter.EnumerationIterable;
import com.jn.langx.util.collection.iter.IteratorIterable;
import com.jn.langx.util.collection.iter.WrappedIterable;
import com.jn.langx.util.function.*;
import com.jn.langx.util.reflect.type.Primitives;
import com.jn.langx.util.struct.Pair;

import java.util.*;

/**
 * Collection utilities
 */
@SuppressWarnings({"unchecked","unused"})
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
     * @return An empty, mutable java.util.HashMap if is not orderable, else an empty, mutable java.util.LinkedHashMap
     */
    public static <K, V> Map<K, V> emptyHashMap(boolean orderable) {
        return orderable ? new LinkedHashMap<K, V>() : new HashMap<K, V>();
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
     * @return An empty, mutable java.util.HashSet if is not orderable, else an empty, mutable java.util.LinkedHashSet
     */
    public static <E> HashSet<E> emptyHashSet(boolean orderable) {
        return orderable ? new LinkedHashSet<E>() : new HashSet<E>();
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

    public static <E> E[] emptyArray(Class<E> clazz) {
        return Arrs.createArray(Primitives.wrap(clazz), 0);
    }


    public enum MapType {
        StringMap,
        HashMap,
        TreeMap,
        LinkedHashMap,
        IdentityHashMap,
        Hashtable,
        Properties;

        public static MapType ofMap(Map map) {
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
    public static <K, V> Map<K, V> getEmptyMapIfNull(Map<K, V> map) {
        return getEmptyMapIfNull(map, null);
    }

    /**
     * @see #getEmptyMapIfNull(Map, MapType)
     */
    public static <K, V> Map<K, V> getEmptyMapIfNull(Map<K, V> map, MapType mapType) {
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

        public static SetType ofSet(Set set) {
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
    public static <E> Set<E> getEmptySetIfNull(Set<E> set) {
        return getEmptySetIfNull(set);
    }

    /**
     * @see #getEmptySetIfNull(Set)
     */
    public static <E> Set<E> getEmptySetIfNull(Set<E> set, SetType setType) {
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

        public static ListType ofList(List list) {
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
    public static <E> List<E> getEmptyListIfNull(List<E> list) {
        return getEmptyListIfNull(list, null);
    }

    /**
     * @see #getEmptyListIfNull(List)
     */
    public static <E> List<E> getEmptyListIfNull(List<E> list, ListType listType) {
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
    public static <E> List<E> asList(E[] array) {
        return asList(array, true, ListType.ArrayList);
    }

    /**
     * Convert an array to a ArrayList or a LinkedList
     */
    public static <E> List<E> asList(E[] array, ListType listType) {
        return asList(array, true, listType);
    }

    /**
     * Convert an array to a List, if the 'mutable' argument is true, will return an unmodifiable List
     */
    public static <E> List<E> asList(E[] array, boolean mutable, ListType listType) {
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

    public static <E> E[] toArray(Collection<E> list) {
        return toArray(list, null);
    }

    /**
     * Convert a list to an array
     */
    public static <E> E[] toArray(Collection<E> list, @Nullable Class<E[]> clazz) {
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
    public static <E> Iterable<E> asIterable(Object object) {
        return asIterable(object, false);
    }

    /**
     * Convert any object to Iterable
     */
    public static <E> Iterable<E> asIterable(Object object, boolean mutable) {
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
    public static <E> List<E> filter(Object anyObject, @NonNull Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        List<E> result = new ArrayList<E>();
        for (E e : iterable) {
            if (predicate.test(e)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Filter a map with the specified predicate
     */
    public static <K, V> Map<K, V> filter(Map<K, V> map, @NonNull Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        Map<K, V> result = getEmptyMapIfNull(null, MapType.ofMap(map));
        if (Emptys.isNotEmpty(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                if (predicate.test(entry.getKey(), entry.getValue())) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    /**
     * mapping an iterable to a list
     */
    public static <E, R> List<R> map(Object anyObject, @NonNull Function<E, R> mapper) {
        Preconditions.checkNotNull(mapper);
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        List<R> result = new ArrayList<R>();
        for (E e : iterable) {
            result.add(mapper.apply(e));
        }
        return result;
    }


    /**
     * mapping an iterable to a map
     */
    public static <E, K, V> Map<K, V> map(Object anyObject, @NonNull Function<E, Pair<K, V>> mapper) {
        Preconditions.checkNotNull(mapper);
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        Map<K, V> result = new HashMap<K, V>();
        for (E e : iterable) {
            Pair<K, V> pair = mapper.apply(e);
            result.put(pair.getKey(), pair.getValue());
        }
        return result;
    }

    /**
     * mapping aMap to a list
     */
    public static <K, V, R> List<R> map(Map<K, V> map, @NonNull Function<Map.Entry<K, V>, R> mapper) {
        Preconditions.checkNotNull(mapper);
        List<R> result = new ArrayList<R>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            result.add(mapper.apply(entry));
        }
        return result;
    }

    /**
     * mapping aMap to bMap
     */
    public static <K, V, K1, V1> Map<K1, V1> map(Map<K, V> map, @NonNull Function2<K, V, Pair<K1, V1>> mapper) {
        Preconditions.checkNotNull(mapper);
        Map<K1, V1> result = getEmptyMapIfNull(null, MapType.ofMap(map));
        if (Emptys.isNotEmpty(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                Pair<K1, V1> e = mapper.apply(entry.getKey(), entry.getValue());
                result.put(e.getKey(), e.getValue());
            }
        }
        return result;
    }

    /**
     * Iterate every element
     */
    public static <E> void forEach(Object anyObject, @NonNull Consumer<E> consumer) {
        Preconditions.checkNotNull(consumer);
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        for (E e : iterable) {
            consumer.accept(e);
        }
    }

    /**
     * Iterate every element
     */
    public static <E> void forEach(E[] array, @NonNull Consumer2<Integer, E> consumer) {
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
    public static <K, V> void forEach(Map<K, V> map, @NonNull Consumer2<K, V> consumer) {
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
    public static <E> E findFirst(Object anyObject, @NonNull Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        for (E e : iterable) {
            if (predicate.test(e)) {
                return e;
            }
        }
        return null;
    }

    /**
     * find the first matched element, null if not found
     */
    public static <K, V> Map.Entry<K, V> findFirst(Map<K, V> map, @NonNull Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                if (predicate.test(entry.getKey(), entry.getValue())) {
                    return entry;
                }
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
    public static <E> boolean removeIf(Collection<E> collection, Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        boolean hasRemoved = false;
        if (Emptys.isNotEmpty(collection)) {
            Iterator<E> iterator = collection.iterator();
            while (iterator.hasNext()) {
                E e = iterator.next();
                if (predicate.test(e)) {
                    iterator.remove();
                    hasRemoved = true;
                }
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
    public static <K, V> boolean removeIf(Map<K, V> map, Predicate2<K, V> predicate) {
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
    public static <E> boolean anyMatch(Collection<E> collection, Predicate<E> predicate) {
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
    public static <K, V> boolean anyMatch(Map<K, V> map, Predicate2<K, V> predicate) {
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
    public static <E> boolean allMatch(Collection<E> collection, Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(collection)) {
            for(E e : collection){
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
    public static <K, V> boolean allMatch(Map<K, V> map, Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(map)) {
            for(Map.Entry<K, V> e: map.entrySet()){
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
    public static <E> boolean noneMatch(Collection<E> collection, Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(collection)) {
            Iterator<E> iterator = collection.iterator();
            for (E e: collection) {
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
    public static <K, V> boolean noneMatch(Map<K, V> map, Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(map)) {
            for(Map.Entry<K, V> e: map.entrySet()){
                if (predicate.test(e.getKey(), e.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <E> Set<E> distinct(Collection<E> collection) {
        return new LinkedHashSet<E>(Emptys.isEmpty(collection) ? Collections.EMPTY_LIST : collection);
    }

    /**
     * truncate a collection using subList(0, length)
     */
    public static <E> List<E> limit(Collection<E> collection, int length) {
        if (Emptys.isEmpty(collection)) {
            return emptyLinkedList();
        }
        List<E> list = (collection instanceof List) ? (List<E>) collection : new LinkedList<E>(collection);
        if (list.size() <= length) {
            return list;
        }
        return list.subList(0, length);
    }

    public static <E> Collection<E> concat(Collection<E> c1, Collection<E> c2, boolean newOne) {
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

    /**
     * sort a collection, return an new list. it is different to
     * Collections.sort(list) is that : Collections.sort() return void
     */
    public static <E> List<E> sort(Collection<E> collection, Comparator<E> comparator) {
        return sort(collection, comparator, false);
    }

    public static <E> List<E> sort(Collection<E> collection, Comparator<E> comparator, boolean reverse) {
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

    /**
     * Reverse a list, return an new list when the argument 'newOne' is true
     */
    public static <E> List<E> reverse(List<E> list, boolean newOne) {
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

    public static <E> DiffResult<Collection<E>> diff(Collection<E> oldCollection, Collection<E> newCollection) {
        return diff(oldCollection, newCollection, null);
    }

    public static <E> DiffResult<Collection<E>> diff(Collection<E> oldCollection, Collection<E> newCollection, Comparator<E> elementComparator) {
        return diff(oldCollection, newCollection, elementComparator);
    }

    public static <E> DiffResult<Collection<E>> diff(Collection<E> oldCollection, Collection<E> newCollection, Comparator<E> elementComparator, KeyBuilder<String, E> keyBuilder) {
        CollectionDiffer<E> differ = new CollectionDiffer<E>();
        differ.setComparator(elementComparator);
        if (keyBuilder != null) {
            differ.diffUsingMap(keyBuilder);
        }
        return differ.diff(oldCollection, newCollection);
    }

    public static <K, V> DiffResult<Map<K, V>> diff(Map<K, V> oldMap, Map<K, V> newMap) {
        return diff(oldMap, newMap, null);
    }

    public static <K, V> DiffResult<Map<K, V>> diff(Map<K, V> oldMap, Map<K, V> newMap, Comparator<V> valueComparator) {
        return diff(oldMap, newMap, valueComparator, null);
    }

    public static <K, V> DiffResult<Map<K, V>> diff(Map<K, V> oldMap, Map<K, V> newMap, Comparator<V> valueComparator, Comparator<K> keyComparator) {
        MapDiffer<K, V> differ = new MapDiffer<K, V>();
        differ.setComparator(valueComparator);
        differ.setKeyComparator(keyComparator);
        return differ.diff(oldMap, newMap);
    }
}
