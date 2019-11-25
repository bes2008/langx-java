package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.diff.*;
import com.jn.langx.util.collection.iter.ArrayIterator;
import com.jn.langx.util.collection.iter.EnumerationIterable;
import com.jn.langx.util.collection.iter.IteratorIterable;
import com.jn.langx.util.collection.iter.WrappedIterable;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.comparator.Comparators;
import com.jn.langx.util.function.*;
import com.jn.langx.util.reflect.type.Primitives;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.struct.Pair;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jn.langx.util.function.Functions.emptyHashSetSupplier0;
import static com.jn.langx.util.function.Functions.emptyTreeSetSupplier0;

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

    public static <K, V> NonAbsentHashMap<K, V> emptyNonAbsentHashMap(@NonNull Supplier<K, V> supplier) {
        Preconditions.checkNotNull(supplier);
        return new NonAbsentHashMap<K, V>(supplier);
    }

    public static <K, V> WrappedNonAbsentMap<K, V> wrapAsNonAbsentMap(@NonNull Map<K, V> map, @NonNull Supplier<K, V> supplier) {
        Preconditions.checkNotNull(map);
        Preconditions.checkNotNull(supplier);
        return new WrappedNonAbsentMap<K, V>(map, supplier);
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
    public static <E> TreeSet<E> emptyTreeSet() {
        return new TreeSet<E>();
    }

    public static <E> TreeSet<E> emptyTreeSet(@Nullable Comparator<E> comparator) {
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
    public static <E> LinkedList<E> emptyLinkedList() {
        return new LinkedList<E>();
    }

    public static <E> E[] emptyArray(@Nullable Class<E> componentType) {
        return Arrs.createArray(Primitives.wrap(componentType), 0);
    }

    public static <E> ArrayList<E> newArrayList(@Nullable Iterable<E> elements) {
        return new ArrayList<E>(asList(elements));
    }

    public static <E> ArrayList<E> newArrayList(@Nullable E... elements) {
        return new ArrayList<E>(asList(elements));
    }

    public static <E> LinkedList<E> newLinkedList(@Nullable Iterable<E> elements) {
        return new LinkedList<E>(asList(elements));
    }

    public static <E> LinkedList<E> newLinkedList(@Nullable E... elements) {
        return new LinkedList<E>(asList(elements));
    }

    public static <E> HashSet<E> newHashSet(@Nullable Iterable<E> elements) {
        return new HashSet<E>(asSet(elements));
    }

    public static <E> HashSet<E> newHashSet(@Nullable E... elements) {
        return new HashSet<E>(asList(elements));
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(@Nullable E... elements) {
        return new LinkedHashSet<E>(asList(elements));
    }

    public static <E> TreeSet<E> newTreeSet(@Nullable E... elements) {
        return new TreeSet<E>(asList(elements));
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
        TreeSet,
        NonDistinctTreeSet;

        public static SetType ofSet(@Nullable Set set) {
            if (set == null) {
                return HashSet;
            }
            return inferSetType(set);
        }
    }


    private static SetType inferSetType(@NonNull Set set) {
        Preconditions.checkNotNull(set);
        if (set instanceof SortedSet) {
            return SetType.TreeSet;
        }
        if (set instanceof HashSet) {
            if (set instanceof LinkedHashSet) {
                return SetType.LinkedHashSet;
            }
            return SetType.HashSet;
        }
        return SetType.HashSet;
    }


    /**
     * Avoid NPE, create an empty, new set when the specified set is null
     */
    public static <E> Set<E> getEmptySetIfNull(@Nullable Set<E> set) {
        return getEmptySetIfNull(set, null);
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
        LinkedList,
        CopyOnWrite,
        VECTOR,
        STACK;

        public static ListType ofList(@Nullable List list) {
            if (list == null) {
                return ArrayList;
            }
            return inferListType(list);
        }
    }


    private static ListType inferListType(@NonNull List list) {
        if (list instanceof CopyOnWriteArrayList) {
            return ListType.CopyOnWrite;
        }
        if (list instanceof LinkedList) {
            return ListType.LinkedList;
        }
        if (list instanceof Stack) {
            return ListType.STACK;
        }
        if (list instanceof Vector) {
            return ListType.VECTOR;
        }
        if (list instanceof ArrayList) {
            return ListType.ArrayList;
        }
        return ListType.ArrayList;
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
                case CopyOnWrite:
                    list = new CopyOnWriteArrayList<E>();
                    break;
                case STACK:
                    list = new Stack<E>();
                    break;
                case VECTOR:
                    list = new Vector<E>();
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

    private static Collection emptyCollectionByInfer(Collection prototype) {
        if (prototype == null) {
            return emptyArrayList();
        }
        if (prototype instanceof Set) {
            SetType setType = SetType.ofSet((Set) prototype);
            return getEmptySetIfNull(null, setType);
        }
        if (prototype instanceof Queue) {
            return emptyArrayList();
        }
        if (prototype instanceof List) {
            ListType listType = ListType.ofList((List) prototype);
            return getEmptyListIfNull(null, listType);
        }
        return emptyArrayList();
    }

    private static <E> Collection<E> emptyCollection(@Nullable Iterable<E> iterable) {
        if (iterable == null) {
            return emptyArrayList();
        }
        if (iterable instanceof Collection) {
            return emptyCollectionByInfer((Collection) iterable);
        }
        return asList(iterable);
    }

    /**
     * Convert an array to a ArrayList
     */
    public static <E> Set<E> asSet(@Nullable E... array) {
        return asSet(array, true, SetType.HashSet);
    }

    /**
     * Convert an array to a ArrayList or a LinkedList
     */
    public static <E> Set<E> asSet(@Nullable E[] array, @Nullable SetType setType) {
        return asSet(array, true, setType);
    }

    public static <E> Set<E> asSet(@Nullable Iterable<E> iterable) {
        return asSet(iterable, true);
    }

    public static <E> Set<E> asSet(@Nullable Iterable<E> iterable, boolean mutable) {
        if (Emptys.isNull(iterable)) {
            Set set = emptyHashSet();
            return mutable ? set : Collections.unmodifiableSet(set);
        }

        Collection<E> c = (iterable instanceof Collection) ? (Collection) iterable : asList(iterable);
        Set set = new HashSet(c);
        return mutable ? set : Collections.unmodifiableSet(set);
    }

    /**
     * Convert an array to a List, if the 'mutable' argument is true, will return an unmodifiable List
     */
    public static <E> Set<E> asSet(@Nullable E[] array, boolean mutable, @Nullable SetType setType) {
        List<E> immutableList = Emptys.isEmpty(array) ? Collects.<E>emptyArrayList() : Arrays.asList(array);
        if (setType == null) {
            setType = SetType.HashSet;
        }
        Set<E> set = null;
        switch (setType) {
            case HashSet:
                set = new HashSet<E>(immutableList);
                if (!mutable) {
                    set = Collections.unmodifiableSet(set);
                }
                break;
            case LinkedHashSet:
                set = new LinkedHashSet<E>(immutableList);
                if (!mutable) {
                    set = Collections.unmodifiableSet(set);
                }
                break;
            case TreeSet:
                TreeSet tset = new TreeSet<E>(immutableList);
                if (!mutable) {
                    set = Collections.unmodifiableSortedSet(tset);
                }
                break;
            case NonDistinctTreeSet:
                NonDistinctTreeSet tset2 = new NonDistinctTreeSet<E>(immutableList);
                if (!mutable) {
                    set = Collections.unmodifiableSortedSet(tset2);
                }
                break;
            default:
                set = new HashSet<E>(immutableList);
                if (!mutable) {
                    set = Collections.unmodifiableSet(set);
                }
                break;
        }

        return set;
    }

    /**
     * Convert an array to a ArrayList
     */
    public static <E> List<E> asList(@Nullable E... array) {
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
            case STACK:
                list = new Stack<E>();
                list.addAll(immutableList);
                break;
            case VECTOR:
                list = new Vector<E>(immutableList);
                break;
            case CopyOnWrite:
                list = new CopyOnWriteArrayList<E>(immutableList);
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

    public static <E> List<E> asList(@Nullable Iterable<E> iterable) {
        return asList(iterable, true);
    }

    public static <E> List<E> asList(@Nullable Iterable<E> iterable, boolean mutable) {
        if (Emptys.isNull(iterable)) {
            return emptyArrayList();
        }
        if (!(iterable instanceof List)) {
            return (List<E>) asList(collect(iterable, toList()), mutable);
        }
        List<E> list = (List<E>) iterable;
        if (!mutable) {
            return Collections.unmodifiableList(list);
        }
        return list;
    }

    public static <E> Collection<E> asCollection(@Nullable Iterable<E> iterable) {
        if (Emptys.isNull(iterable)) {
            return emptyArrayList();
        }
        if (!(iterable instanceof Collection)) {
            return (Collection<E>) asList(collect(iterable, toList()));
        }
        return (Collection<E>) iterable;
    }

    public static <E, C extends Collection<E>> Object[] toArray(@Nullable C collection) {
        if (Emptys.isEmpty(collection)) {
            C c = (C) Collections.emptyList();
            return c.toArray();
        }
        return collection.toArray();
    }

    /**
     * Convert a list to an array
     */
    public static <E, C extends Collection<E>> E[] toArray(@Nullable C list, @Nullable Class<E[]> clazz) {
        Preconditions.checkNotNull(clazz);
        if (Emptys.isEmpty(list)) {
            C c = (C) Collections.emptyList();
            return Arrays.copyOf(c.toArray(), list.size(), clazz);
        }
        // Make a new array of the specified class
        return Arrays.copyOf(list.toArray(), list.size(), clazz);
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
            if (mutable) {
                return asList((E[]) object);
            } else {
                return new ArrayIterator<E>((E[]) object);
            }
        }

        if (object instanceof Collection) {
            if (!mutable) {
                return Collections.unmodifiableCollection((Collection) object);
            }
            return (Collection) object;
        }

        if (object instanceof Iterable) {
            if (!mutable) {
                return new WrappedIterable<E>((Iterable) object, false);
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
    public static <E> Collection<E> filter(@Nullable Object anyObject, @NonNull final Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        Iterable<E> iterable = asIterable(anyObject);
        final Collection<E> result = emptyCollection(iterable);
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
    public static <E, R> Collection<R> map(@Nullable Object anyObject, @NonNull final Function<E, R> mapper) {
        Preconditions.checkNotNull(mapper);
        Iterable<E> iterable = asIterable(anyObject);
        final Collection result = emptyCollection(iterable);
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
    public static <E, K, V, M extends Map<K, V>> M map(@Nullable Object anyObject, @NonNull final Mapper<E, Pair<K, V>> mapper) {
        Preconditions.checkNotNull(mapper);
        final M result = (M) emptyHashMap(true);
        Iterable<E> iterable = asIterable(anyObject);
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
    public static <K, V, R, M extends Map<K, V>> List<R> map(@Nullable M map, @NonNull final Function2<K, V, R> mapper) {
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
    public static <K, V, K1, V1, M extends Map<K, V>> Map<K1, V1> map(@Nullable M map, @NonNull final Mapper2<K, V, Pair<K1, V1>> mapper) {
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
    public static <E, R> Collection<R> flatMap(@NonNull final Function<E, R> mapper, @Nullable Collection<E[]> collection) {
        if (Emptys.isEmpty(collection)) {
            return emptyArrayList();
        }
        Preconditions.checkNotNull(mapper);
        final Collection<R> list = (Collection<R>) emptyCollectionByInfer(collection);
        forEach(collection, new Consumer<E[]>() {
            @Override
            public void accept(E[] c) {
                Collection<R> rs = Collects.map(c, mapper);
                list.addAll(rs);
            }
        });
        return list;
    }

    /**
     * map a collection to another, flat it
     */
    public static <E, R, C extends Collection<? extends Iterable<E>>> Collection<R> flatMap(@Nullable C collection, @NonNull final Function<E, R> mapper) {
        if (Emptys.isEmpty(collection)) {
            return emptyArrayList();
        }
        Preconditions.checkNotNull(mapper);
        final Collection<R> list = emptyCollectionByInfer(collection);
        forEach(collection, new Consumer<Collection<E>>() {
            @Override
            public void accept(Collection<E> c) {
                Collection<R> rs = Collects.map(c, mapper);
                list.addAll(rs);
            }
        });
        return list;
    }

    /**
     * Iterate every element
     */
    public static <E> void forEach(@Nullable Object anyObject, @NonNull Consumer<E> consumer) {
        forEach(anyObject, consumer, null);
    }

    /**
     * Iterate every element
     */
    public static <E> void forEach(@Nullable Object anyObject, @NonNull Consumer<E> consumer, @Nullable Predicate<E> breakPredicate) {
        if (anyObject == null) {
            return;
        }
        Preconditions.checkNotNull(consumer);
        Iterable<E> iterable = asIterable(anyObject);
        for (E e : iterable) {
            if (breakPredicate != null && breakPredicate.test(e)) {
                break;
            }
            consumer.accept(e);
        }
    }

    /**
     * Iterate every element
     */
    public static <E, C extends Collection<E>> void forEach(@Nullable C collection, @NonNull final Consumer2<Integer, E> consumer) {
        forEach(collection, consumer, null);
    }


    /**
     * Iterate every element
     */
    public static <E, C extends Collection<E>> void forEach(@Nullable C collection, @NonNull final Consumer2<Integer, E> consumer, @Nullable final Predicate2<Integer, E> breakPredicate) {
        Preconditions.checkNotNull(consumer);
        if (Emptys.isNotEmpty(collection)) {
            final Holder<Integer> indexHolder = new Holder<Integer>(0);
            forEach(collection, new Consumer<E>() {
                @Override
                public void accept(E e) {
                    consumer.accept(indexHolder.get(), e);
                    indexHolder.set(indexHolder.get() + 1);
                }
            }, breakPredicate != null ? new Predicate<E>() {
                @Override
                public boolean test(E value) {
                    return breakPredicate.test(indexHolder.get(), value);
                }
            } : null);
        }
    }

    /**
     * Iterate every element
     */
    public static <E> void forEach(@Nullable E[] array, @NonNull Consumer2<Integer, E> consumer) {
        forEach(array, consumer, null);
    }

    /**
     * Iterate every element
     */
    public static <E> void forEach(@Nullable E[] array, @NonNull Consumer2<Integer, E> consumer, @Nullable final Predicate2<Integer, E> breakPredicate) {
        Preconditions.checkNotNull(consumer);
        if (Emptys.isNotEmpty(array)) {
            for (int i = 0; i < array.length; i++) {
                if (breakPredicate != null && breakPredicate.test(i, array[i])) {
                    break;
                }
                consumer.accept(i, array[i]);
            }
        }
    }

    /**
     * Iterate every element
     */
    public static <K, V, M extends Map<? extends K, ? extends V>> void forEach(@Nullable M map, @NonNull Consumer2<K, V> consumer) {
        forEach(map, consumer, null);
    }

    /**
     * Iterate every element
     */
    public static <K, V, M extends Map<? extends K, ? extends V>> void forEach(@Nullable M map, @NonNull Consumer2<K, V> consumer, @Nullable final Predicate2<K, V> breakPredicate) {
        Preconditions.checkNotNull(consumer);
        if (Emptys.isNotEmpty(map)) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                if (breakPredicate != null && breakPredicate.test(entry.getKey(), entry.getValue())) {
                    break;
                }

                consumer.accept(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * find the first matched element, null if not found
     */
    public static <E, C extends Collection<E>> E findFirst(@Nullable C collection, @Nullable Predicate<E> predicate) {
        List<E> list = findN(collection, predicate, 1);
        if (Emptys.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * find the first matched element, null if not found
     */
    public static <K, V> Map.Entry<? extends K, ? extends V> findFirst(@Nullable Map<? extends K, ? extends V> map, @Nullable Predicate2<K, V> predicate) {
        if (map == null) {
            return null;
        }
        if (Emptys.isNotEmpty(map)) {
            if (predicate != null) {
                for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                    if (predicate.test(entry.getKey(), entry.getValue())) {
                        return entry;
                    }
                }
            } else {
                Set set = map.entrySet();
                List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(set);
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * find the first matched element, null if not found
     */
    public static <E, C extends Collection<E>> List<E> findN(@Nullable C collection, @Nullable Predicate<E> predicate, int n) {
        List<E> ret = Collects.emptyArrayList();
        if (n <= 0 || Emptys.isEmpty(collection)) {
            return ret;
        }

        for (E e : collection) {
            if (predicate == null || predicate.test(e)) {
                ret.add(e);
                if (ret.size() == n) {
                    break;
                }
            }
        }

        return ret;
    }

    /**
     * find the first matched element, null if not found
     */
    public static <K, V> Map<? extends K, ? extends V> findN(@Nullable Map<? extends K, ? extends V> map, @Nullable Predicate2<K, V> predicate, int n) {
        Map<K, V> ret = emptyHashMap(true);
        if (n <= 0 || map == null || map.isEmpty()) {
            return ret;
        }
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            if (predicate == null || predicate.test(entry.getKey(), entry.getValue())) {
                K key = entry.getKey();
                V value = entry.getValue();
                ret.put(key, value);
                if (map.size() == n) {
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * remove all elements that match the condition
     *
     * @return whether has any element removed
     * @throws UnsupportedOperationException, NullPointException
     */
    public static <E, C extends Collection<E>> boolean removeIf(@Nullable C collection, @NonNull Predicate<E> predicate) {
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
                try {
                    iterator.remove();
                } catch (UnsupportedOperationException ex) {
                    break;
                }
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
    public static <E, C extends Collection<E>> boolean anyMatch(@Nullable C collection, @NonNull Predicate<E> predicate) {
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
    public static <K, V, M extends Map<? extends K, ? extends V>> boolean anyMatch(@Nullable M map, @NonNull Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(map)) {
            Map.Entry<? extends K, ? extends V> entry = findFirst(map, predicate);
            return entry != null;
        }
        return false;
    }

    /**
     * whether all elements match the specified condition or not
     *
     * @return whether has any element removed
     */
    public static <E, C extends Collection<E>> boolean allMatch(@Nullable C collection, @NonNull Predicate<E> predicate) {
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
    public static <K, V, M extends Map<? extends K, ? extends V>> boolean allMatch(@Nullable M map, @NonNull Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(map)) {
            for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
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
    public static <E, C extends Collection<E>> boolean noneMatch(@Nullable C collection, @NonNull Predicate<E> predicate) {
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
    public static <K, V, M extends Map<? extends K, ? extends V>> boolean noneMatch(@Nullable M map, @NonNull Predicate2<K, V> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(map)) {
            for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
                if (predicate.test(e.getKey(), e.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <E, C extends Collection<E>> Set<E> distinct(@Nullable C collection) {
        return new LinkedHashSet<E>(Emptys.isEmpty(collection) ? Collections.EMPTY_LIST : collection);
    }

    /**
     * truncate a collection using subList(0, maxSize)
     */
    public static <E, C extends Collection<E>> List<E> limit(@Nullable C collection, int maxSize) {
        if (Emptys.isEmpty(collection)) {
            return emptyLinkedList();
        }

        Preconditions.checkTrue(maxSize >= 0);

        List<E> list = (collection instanceof List) ? (List<E>) collection : new LinkedList<E>(collection);
        if (list.size() <= maxSize) {
            return list;
        }
        return list.subList(0, maxSize);
    }

    /**
     * skip n elements, get a collection using subList(n, size)
     */
    public static <E, C extends Collection<E>> List<E> skip(@Nullable C collection, int n) {
        if (Emptys.isEmpty(collection)) {
            return emptyLinkedList();
        }
        Preconditions.checkTrue(n >= 0);
        List<E> list = (collection instanceof List) ? (List<E>) collection : new LinkedList<E>(collection);
        if (list.size() <= n) {
            return emptyArrayList();
        }
        return list.subList(n, list.size());
    }


    /**
     * sort a collection, return an new list. it is different to
     * Collections.sort(list) is that : Collections.sort() return void
     */
    public static <E extends Comparable<E>, C extends Collection<E>> TreeSet<E> sort(@Nullable C collection, boolean reverse) {
        return sort(collection, new ComparableComparator<E>(), reverse);
    }

    /**
     * sort a collection, return an new list. it is different to
     * Collections.sort(list) is that : Collections.sort() return void
     */
    public static <E, C extends Collection<E>> TreeSet<E> sort(@Nullable C collection, @NonNull Comparator<E> comparator) {
        return sort(collection, comparator, false);
    }

    /**
     * sort a collection, return an new list. it is different to
     * Collections.sort(list) is that : Collections.sort() return void
     */
    public static <E, C extends Collection<E>> TreeSet<E> sort(@Nullable C collection, @NonNull Comparator<E> comparator, boolean reverse) {
        Preconditions.checkNotNull(comparator);
        if (Emptys.isEmpty(collection)) {
            return new TreeSet<E>(comparator);
        } else {
            TreeSet<E> set = new NonDistinctTreeSet<E>(reverse ? Collections.reverseOrder(comparator) : comparator);
            set.addAll(filter(collection, Functions.<E>nonNullPredicate()));
            return set;
        }
    }

    /**
     * sort a collection, return an new list. it is different to
     * Collections.sort(list) is that : Collections.sort() return void
     */
    public static <K, V, M extends Map<K, V>> M sort(@Nullable M map, @NonNull Comparator<K> comparator) {
        Preconditions.checkNotNull(comparator);
        M result = (M) emptyTreeMap(comparator);
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

    public static <K, V> int count(@Nullable Map<K, V> map) {
        return Emptys.isEmpty(map) ? 0 : map.size();
    }

    public static <E, C extends Collection<E>> int count(@Nullable C collection) {
        return Emptys.isEmpty(collection) ? 0 : collection.size();
    }

    public static int count(@Nullable Object anyObject) {
        final Holder<Integer> count = new Holder<Integer>(0);
        forEach(anyObject, new Consumer<Object>() {
            @Override
            public void accept(Object object) {
                count.set(count.get() + 1);
            }
        });
        return count.get();
    }

    public static <E> E max(@NonNull Object object, @NonNull final Comparator<E> comparator) {
        Preconditions.checkNotNull(comparator);
        Iterable<E> iterable = asIterable(object);
        return reduce(iterable, new Operator2<E>() {
            @Override
            public E apply(E input1, E input2) {
                return comparator.compare(input1, input2) >= 0 ? input1 : input2;
            }
        });
    }

    public static <E> E min(@Nullable Object object, @NonNull final Comparator<E> comparator) {
        Preconditions.checkNotNull(comparator);
        Iterable<E> iterable = asIterable(object);
        final Holder<E> min = new Holder<E>();
        return reduce(iterable, new Operator2<E>() {
            @Override
            public E apply(E input1, E input2) {
                return comparator.compare(input1, input2) <= 0 ? input1 : input2;
            }
        });
    }

    public static <E, R> R collect(@Nullable Object anyObject, @NonNull Collector<E, R> collector) {
        Preconditions.checkNotNull(collector);
        return collect(anyObject, collector.supplier(), collector.accumulator());
    }

    public static <E, R> R collect(@Nullable Object anyObject, @NonNull Supplier0<R> containerFactory, @NonNull final Consumer2<R, E> consumer) {
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

    public static <E> Collection<E> collect(@Nullable Object anyObject, @NonNull final Collection<E> container) {
        Preconditions.checkNotNull(container);
        Supplier0<Collection<E>> containerFactory = new Supplier0<Collection<E>>() {
            @Override
            public Collection<E> get() {
                return container;
            }
        };
        Consumer2<Collection<E>, E> consumer = new Consumer2<Collection<E>, E>() {
            @Override
            public void accept(Collection<E> list, E value) {
                list.add(value);
            }
        };
        return collect(anyObject, containerFactory, consumer);
    }

    public static <X, Y, E, C extends Collection<E>> CollectionDiffResult<E> diff(@Nullable X oldObject, @NonNull final Function<X, E> oldMapper, @Nullable Y newObject, @NonNull final Function<Y, E> newMapper) {
        return diff(oldObject, oldMapper, newObject, newMapper, null);
    }

    public static <X, Y, E, C extends Collection<E>> CollectionDiffResult<E> diff(@Nullable X oldObject, @NonNull final Function<X, E> oldMapper, @Nullable Y newObject, @NonNull final Function<Y, E> newMapper, @Nullable Comparator<E> elementComparator) {
        return diff(oldObject, oldMapper, newObject, newMapper, elementComparator, null);
    }

    public static <X, Y, E, C extends Collection<E>> CollectionDiffResult<E> diff(@Nullable X oldObject, @NonNull final Function<X, E> oldMapper, @Nullable Y newObject, @NonNull final Function<Y, E> newMapper, @Nullable Comparator<E> elementComparator, @Nullable KeyBuilder<String, E> keyBuilder) {
        Collection<E> oldCollection = map(oldObject, oldMapper);
        Collection<E> newCollection = map(oldObject, oldMapper);
        return diff(oldCollection, newCollection, elementComparator, keyBuilder);
    }


    public static <E, C extends Collection<E>> CollectionDiffResult<E> diff(@Nullable C oldCollection, @Nullable C newCollection) {
        return diff(oldCollection, newCollection, null);
    }

    public static <E, C extends Collection<E>> CollectionDiffResult<E> diff(@Nullable C oldCollection, @Nullable C newCollection, @Nullable Comparator<E> elementComparator) {
        return diff(oldCollection, newCollection, elementComparator, null);
    }

    public static <E, C extends Collection<E>> CollectionDiffResult<E> diff(@Nullable C oldCollection, @Nullable C newCollection, @Nullable Comparator<E> elementComparator, @Nullable KeyBuilder<String, E> keyBuilder) {
        CollectionDiffer<E> differ = new CollectionDiffer<E>();
        differ.setComparator(elementComparator);
        if (keyBuilder != null) {
            differ.diffUsingMap(keyBuilder);
        }
        return differ.diff(oldCollection, newCollection);
    }

    public static <K, V, M extends Map<K, V>> MapDiffResult<K, V> diff(@Nullable M oldMap, @Nullable M newMap) {
        return diff(oldMap, newMap, null);
    }

    public static <K, V, M extends Map<K, V>> MapDiffResult<K, V> diff(@Nullable M oldMap, @Nullable M newMap, @Nullable Comparator<V> valueComparator) {
        return diff(oldMap, newMap, valueComparator, null);
    }

    public static <K, V, M extends Map<K, V>> MapDiffResult<K, V> diff(@Nullable M oldMap, @Nullable M newMap, @Nullable Comparator<V> valueComparator, @Nullable Comparator<K> keyComparator) {
        MapDiffer<K, V> differ = new MapDiffer<K, V>();
        differ.setValueComparator(valueComparator);
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

    public static <E, C extends Collection<E>> void addAll(@NonNull final C collection, @Nullable Iterable<E> iterable) {
        Preconditions.checkNotNull(collection);
        if (Emptys.isNotEmpty(iterable)) {
            forEach(iterable, new Consumer<E>() {
                @Override
                public void accept(E e) {
                    collection.add(e);
                }
            });
        }
    }

    public static <E, C extends Collection<E>> void addAll(@NonNull C collection, @Nullable E... iterator) {
        addAll(collection, Collects.<E>asIterable(iterator));
    }

    public static <E, C extends Collection<E>> void addAll(@NonNull C collection, @Nullable Iterator<E> iterator) {
        addAll(collection, Collects.<E>asIterable(iterator));
    }

    /**
     * Concat two collection to one
     */
    public static <E, C extends Collection<E>> C concat(@Nullable C c1, @Nullable C c2) {
        return concat(c1, c2, true);
    }

    /**
     * Concat two collection to one
     */
    public static <E, C extends Collection<E>> C concat(@Nullable C c1, @Nullable C c2, boolean newOne) {
        if (newOne) {
            List<E> l = emptyArrayList();
            if (Emptys.isNotEmpty(c1)) {
                l.addAll(c1);
            }
            if (Emptys.isNotEmpty(c2)) {
                l.addAll(c2);
            }
            return (C) l;
        } else {
            Preconditions.checkNotNull(c1);
            if (Emptys.isNotEmpty(c2)) {
                c1.addAll(c2);
            }
            return c1;
        }
    }

    public static <E, C extends Collection<E>> C merge(@Nullable C c1, @Nullable C c2) {
        return merge(c1, c2, true);
    }

    public static <E, C extends Collection<E>> C merge(@Nullable final C c1, @Nullable C c2, boolean newOne) {
        if (newOne) {
            Set<E> set = emptyHashSet(true);
            if (Emptys.isNotEmpty(c1)) {
                set.addAll(c1);
            }
            if (Emptys.isNotEmpty(c2)) {
                set.addAll(c2);
            }
            return (C) set;
        } else {
            Preconditions.checkNotNull(c1);
            if (Emptys.isNotEmpty(c2)) {
                Set set = emptyHashSet(true);
                set.addAll(c2);
                forEach(c2, new Consumer<E>() {
                    @Override
                    public void accept(E e) {
                        if (!c1.contains(e)) {
                            c1.add(e);
                        }
                    }
                });
            }
            return c1;
        }
    }

    public static <K, V, M extends Map<K, V>> M merge(@Nullable M map1, @Nullable M map2) {
        return merge(map1, map2, true);
    }

    public static <K, V, M extends Map<K, V>> M merge(@Nullable M map1, @Nullable M map2, boolean newOne) {
        if (newOne) {
            Map<K, V> map = emptyHashMap();
            if (Emptys.isNotEmpty(map1)) {
                map.putAll(map1);
            }
            if (Emptys.isNotEmpty(map2)) {
                map.putAll(map2);
            }
            return (M) map;
        } else {
            Preconditions.checkNotNull(map1);
            if (Emptys.isNotEmpty(map2)) {
                map1.putAll(map2);
            }
            return map1;
        }
    }

    public static <E> E reduce(@Nullable E[] iterable, Operator2<E> operator) {
        return reduce(Collects.<E>asIterable(iterable), operator);
    }

    public static <E, C extends Iterable<E>> E reduce(@Nullable Iterable<E> iterable, Operator2<E> operator) {
        if (Emptys.isEmpty(iterable)) {
            return null;
        }
        Preconditions.checkNotNull(operator);
        List<E> list = asList(iterable);
        E result = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            result = operator.apply(result, list.get(i));
        }
        return result;
    }

    public static <K, E> Map<K, List<E>> groupBy(@Nullable E[] iterable, @NonNull final Function<E, K> classifier) {
        return groupBy(Collects.<E>asIterable(iterable), classifier, null);
    }

    public static <K, E> Map<K, List<E>> groupBy(@Nullable E[] iterable, @NonNull final Function<E, K> classifier, @Nullable Supplier0<Map<K, List<E>>> mapFactory) {
        return groupBy(Collects.<E>asIterable(iterable), classifier, mapFactory);
    }

    public static <K, E, C extends Collection<E>> Map<K, List<E>> groupBy(@Nullable C iterable, @NonNull final Function<E, K> classifier) {
        return groupBy(iterable, classifier, null);
    }

    public static <K, E, C extends Collection<E>> Map<K, List<E>> groupBy(@Nullable Iterable<E> iterable, @NonNull final Function<E, K> classifier, @Nullable Supplier0<Map<K, List<E>>> mapFactory) {
        Preconditions.checkNotNull(classifier);
        iterable = asIterable(iterable);
        if (mapFactory == null) {
            mapFactory = new Supplier0<Map<K, List<E>>>() {
                @Override
                public Map<K, List<E>> get() {
                    return new HashMap<K, List<E>>();
                }
            };
        }
        Map<K, List<E>> map = mapFactory.get();
        Preconditions.checkNotNull(map);
        final WrappedNonAbsentMap<K, List<E>> wrappedNonAbsentMap = WrappedNonAbsentMap.wrap(map, new Supplier<K, List<E>>() {
            @Override
            public List<E> get(K key) {
                return new ArrayList<E>();
            }
        });
        Collects.forEach(iterable, new Consumer<E>() {
            @Override
            public void accept(E e) {
                K group = classifier.apply(e);
                wrappedNonAbsentMap.get(group).add(e);
            }
        });
        return map;
    }


    /*****************************************************************
     *      Collector Factory:
     *****************************************************************/


    public static <E> Collector<E, TreeSet<E>> toTreeSet(@Nullable final Comparator<E> comparator) {
        return new Collector<E, TreeSet<E>>() {
            @Override
            public Supplier0<TreeSet<E>> supplier() {
                return emptyTreeSetSupplier0(comparator);
            }

            @Override
            public Consumer2<TreeSet<E>, E> accumulator() {
                return new Consumer2<TreeSet<E>, E>() {
                    @Override
                    public void accept(TreeSet<E> set, E value) {
                        set.add(value);
                    }
                };
            }
        };
    }

    public static <E> Collector<E, HashSet<E>> toHashSet(final boolean sequential) {
        return new Collector<E, HashSet<E>>() {
            @Override
            public Supplier0<HashSet<E>> supplier() {
                return emptyHashSetSupplier0();
            }

            @Override
            public Consumer2<HashSet<E>, E> accumulator() {
                return new Consumer2<HashSet<E>, E>() {
                    @Override
                    public void accept(HashSet<E> set, E value) {
                        set.add(value);
                    }
                };
            }
        };
    }

    public static <E> Collector<E, List<E>> toList() {
        return new Collector<E, List<E>>() {
            @Override
            public Supplier0<List<E>> supplier() {
                return new Supplier0<List<E>>() {
                    @Override
                    public List<E> get() {
                        return Collects.emptyArrayList();
                    }
                };
            }

            @Override
            public Consumer2<List<E>, E> accumulator() {
                return new Consumer2<List<E>, E>() {
                    @Override
                    public void accept(List<E> list, E value) {
                        list.add(value);
                    }
                };
            }
        };
    }

    public static <E, K, V> Collector<E, Map<K, V>> toHashMap(@NonNull final Function<E, K> keyMapper, @NonNull final Function<E, V> valueMapper, final boolean sequential) {
        Preconditions.checkNotNull(keyMapper);
        Preconditions.checkNotNull(valueMapper);
        return toMap(new Supplier0<Map<K, V>>() {
            @Override
            public Map<K, V> get() {
                return Collects.emptyHashMap(sequential);
            }
        }, keyMapper, valueMapper);
    }

    public static <E, K, V> Collector<E, Map<K, V>> toTreeMap(@NonNull final Function<E, K> keyMapper, @NonNull final Function<E, V> valueMapper, @Nullable final Comparator<K> comparator) {
        Preconditions.checkNotNull(keyMapper);
        Preconditions.checkNotNull(valueMapper);
        return toMap(new Supplier0<Map<K, V>>() {
            @Override
            public Map<K, V> get() {
                return Collects.emptyTreeMap(comparator);
            }
        }, keyMapper, valueMapper);
    }

    public static <E, K, V> Collector<E, Map<K, V>> toMap(@NonNull final Supplier0<Map<K, V>> mapFactory, @NonNull final Function<E, K> keyMapper, @NonNull final Function<E, V> valueMapper) {
        Preconditions.checkNotNull(keyMapper);
        Preconditions.checkNotNull(valueMapper);
        return new Collector<E, Map<K, V>>() {
            @Override
            public Supplier0<Map<K, V>> supplier() {
                return mapFactory;
            }

            @Override
            public Consumer2<Map<K, V>, E> accumulator() {
                return new Consumer2<Map<K, V>, E>() {
                    @Override
                    public void accept(Map<K, V> map, E e) {
                        K key = keyMapper.apply(e);
                        V value = valueMapper.apply(e);
                        map.put(key, value);
                    }
                };
            }
        };
    }

    public static <E, K> Collector<E, Map<K, List<E>>> groupingBy(@NonNull final Function<E, K> classifier, @NonNull final Supplier0<Map<K, List<E>>> mapFactory) {
        Preconditions.checkNotNull(classifier);
        Preconditions.checkNotNull(mapFactory);
        return new Collector<E, Map<K, List<E>>>() {
            @Override
            public Supplier0<Map<K, List<E>>> supplier() {
                return mapFactory;
            }

            @Override
            public Consumer2<Map<K, List<E>>, E> accumulator() {
                return new Consumer2<Map<K, List<E>>, E>() {
                    @Override
                    public void accept(Map<K, List<E>> map, E e) {
                        K group = classifier.apply(e);
                        List<E> list = map.get(group);
                        if (list == null) {
                            list = new ArrayList<E>();
                            map.put(group, list);
                        }
                        list.add(e);
                    }
                };
            }
        };
    }

    public static <E, C extends Collection<E>> C clearNulls(@Nullable C collection) {
        final Collection<E> c = Collects.emptyCollectionByInfer(collection);
        return (C) filter(c, Functions.nonNullPredicate());
    }

}
