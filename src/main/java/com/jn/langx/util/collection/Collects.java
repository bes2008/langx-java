package com.jn.langx.util.collection;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.*;

import java.util.*;

/**
 * Collection utilities
 */
@SuppressWarnings({"unchecked"})
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
            if (map instanceof StringMap) {
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
                    map1 = new Hashtable<K, V>();
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
     * Reverse a list, return an new list when the argument 'newOne' is true
     */
    public static <E> List<E> reverse(List<E> list, boolean newOne) {
        if (Emptys.isEmpty(list)) {
            return (list == null || newOne) ? new ArrayList<E>() : list;
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

    /**
     * Convert a list to an array
     */
    public static <E> E[] toArray(List<E> list) {
        if (Emptys.isEmpty(list)) {
            list = Collections.emptyList();
        }
        return (E[]) list.toArray();
    }

    /**
     * Convert a set to an array
     */
    public static <E> E[] toArray(Set<E> set) {
        if (Emptys.isEmpty(set)) {
            set = Collections.emptySet();
        }
        return (E[]) set.toArray();
    }


    /**
     * Convert any object to Iterable
     *
     * @param object
     * @param <E>
     * @return
     */
    private static <E> Iterable<E> asIterable(Object object) {
        if (Emptys.isNull(object)) {
            return asList(null);
        }

        if (Arrs.isArray(object)) {
            return asList((E[]) object);
        }

        if (object instanceof Iterable) {
            return (Iterable) object;
        }

        if (object instanceof Map) {
            return (Iterable<E>) asList(Arrs.wrapAsArray(object));
        }

        if (object instanceof Iterator) {
            return new IteratorIterable<E>((Iterator<E>) object, false);
        }

        if (object instanceof Enumeration) {
            return new EnumerationIterable<E>((Enumeration<E>) object);
        }

        if (object instanceof Number) {
            return (Iterable<E>) asList(Arrs.wrapAsArray((Number) object));
        }

        if (object instanceof String) {
            return (Iterable<E>) asList(Arrs.wrapAsArray((String) object));
        }

        return (Iterable<E>) asList(Arrs.wrapAsArray(object));
    }

    /**
     * Filter any object with the specified predicate
     */
    public static <E> List<E> filter(Object anyObject, Predicate<E> predicate) {
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
    public static <K, V> Map<K, V> filter(Map<K, V> map, Predicate2<K, V> predicate) {
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
     * mapping a to b
     */
    public static <E, R> List<R> map(Object anyObject, Function<E, R> mapper) {
        Preconditions.checkNotNull(mapper);
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        List<R> result = new ArrayList<R>();
        for (E e : iterable) {
            result.add(mapper.apply(e));
        }
        return result;
    }

    /**
     * mapping aMap to bMap
     */
    public static <R, K, V> List<R> map(Map<K, V> map, Function<Map.Entry<K, V>, R> mapper) {
        Preconditions.checkNotNull(mapper);
        List<R> result = new ArrayList<R>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            result.add(mapper.apply(entry));
        }
        return result;
    }

    public static <K, V, K1, V1> Map<K1, V1> map(Map<K, V> map, Function2<K, V, Map.Entry<K1, V1>> mapper) {
        Preconditions.checkNotNull(mapper);
        Map<K1, V1> result = getEmptyMapIfNull(null, MapType.ofMap(map));
        if (Emptys.isNotEmpty(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                Map.Entry<K1, V1> e = mapper.apply(entry.getKey(), entry.getValue());
                result.put(e.getKey(), e.getValue());
            }
        }
        return result;
    }

    /**
     * Iterate every element
     */
    public static <E> void forEach(Object anyObject, Consumer<E> consumer) {
        Preconditions.checkNotNull(consumer);
        Iterable<E> iterable = (Iterable<E>) asIterable(anyObject);
        for (E e : iterable) {
            consumer.accept(e);
        }
    }

    /**
     * Iterate every element
     */
    public static <K, V> void forEach(Map<K, V> map, Consumer2<K, V> consumer) {
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
    public static <E> E findFirst(Object anyObject, Predicate<E> predicate) {
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
    public static <K, V> Map.Entry<K, V> findFirst(Map<K, V> map, Predicate2<K, V> predicate) {
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
}
