package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.diff.*;
import com.jn.langx.util.collection.iter.EnumerationIterable;
import com.jn.langx.util.collection.iter.IteratorIterable;
import com.jn.langx.util.collection.iter.WrappedIterable;
import com.jn.langx.util.collection.sequence.IterableSequence;
import com.jn.langx.util.collection.sequence.ListSequence;
import com.jn.langx.util.collection.sequence.SortedSetSequence;
import com.jn.langx.util.collection.sort.TimSort;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.comparator.Comparators;
import com.jn.langx.util.concurrent.ConcurrentHashSet;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;
import com.jn.langx.util.function.*;
import com.jn.langx.util.random.IRandom;
import com.jn.langx.util.reflect.type.Primitives;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.struct.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;



/**
 * Collection utilities
 */
@SuppressWarnings({"all"})
public class Collects {
    public static <T> Enumeration<T> immutableEmptyEnumeration() {
        return (Enumeration<T>) ImmutableEmptyEnumeration.EMPTY_ENUMERATION;
    }

    private static class ImmutableEmptyEnumeration<E> implements Enumeration<E> {
        static final ImmutableEmptyEnumeration<Object> EMPTY_ENUMERATION
                = new ImmutableEmptyEnumeration<Object>();

        public boolean hasMoreElements() {
            return false;
        }

        public E nextElement() {
            throw new NoSuchElementException();
        }
    }


    public static <E> List<E> immutableList() {
        return immutableList((List)null);
    }

    /**
     * @since 5.2.4
     */
    public static <E> List<E> immutableList(E... elements){
        return immutableList(asList(elements));
    }

    /**
     * @since 5.2.4
     */
    public static <E> List<E> immutableList(Collection<E> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(asList(list));
    }


    public static <E> List<E> immutableArrayList(List<E> list) {
        return Collections.unmodifiableList(Objs.useValueIfNull(list, Collects.<E>emptyArrayList()));
    }

    public static <E> List<E> immutableArrayList(E... array) {
        return immutableList(Collects.newArrayList(array));
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
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }


    public static <E> ArrayList<E> newArrayList(Iterable<E> elements) {
        return new ArrayList<E>(asList(elements));
    }

    public static <E> ArrayList<E> newArrayList(Iterator<E> elements) {
        return newArrayList(Collects.<E>asIterable(elements));
    }


    public static <E> ArrayList<E> newArrayList(@Nullable E... elements) {
        return new ArrayList<E>(asList(elements));
    }

    public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
        return new ArrayList<E>(Maths.max(0, initialArraySize));
    }

    public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
        return new ArrayList<E>(Maths.max(0, estimatedSize));
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<E>();
    }

    public static <E> LinkedList<E> newLinkedList(@Nullable Iterable<E> elements) {
        return new LinkedList<E>(asList(elements));
    }

    public static <E> LinkedList<E> newLinkedList(@Nullable Iterator<E> elements) {
        return newLinkedList(Collects.<E>asIterable(elements));
    }

    public static <E> LinkedList<E> newLinkedList(@Nullable E... elements) {
        return new LinkedList<E>(asList(elements));
    }


    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<E>();
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<E> elements) {
        return new CopyOnWriteArrayList<E>(Collects.asList(elements));
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
                list = new java.util.Stack<E>();
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
                    list = new java.util.Stack<E>();
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

    public static <E> Set<E> immutableSet() {
        return immutableSet((Set) null);
    }

    public static <E> Set<E> immutableSet(Set<E> set) {
        if (set == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(set);
    }

    public static <E> Set<E> immutableSet(Collection<E> collection) {
        return Collections.unmodifiableSet(asSet(collection));
    }

    public static <E> Set<E> immutableSet(E... elements) {
        return Collections.unmodifiableSet(asSet(elements));
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

    public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
        EnumSet<E> set = EnumSet.noneOf(elementType);
        Collects.addAll(set, Collects.<E>asList(iterable));
        return set;
    }

    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }

    public static <E> HashSet<E> newHashSet(@Nullable E... elements) {
        return new HashSet<E>(asList(elements));
    }

    public static <E> HashSet<E> newHashSet(@Nullable Iterable<E> elements) {
        return new HashSet<E>(asList(elements));
    }

    public static <E> HashSet<E> newHashSet(Iterator<E> elements) {
        HashSet<E> set = newHashSet();
        Collects.addAll(set, Collects.<E>asIterable(elements));
        return set;
    }

    public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
        return new HashSet<E>(Maths.max(0, expectedSize));
    }


    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet<E>();
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(@Nullable Iterable<E> elements) {
        return new LinkedHashSet<E>(asList(elements));
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(@Nullable E... elements) {
        return new LinkedHashSet<E>(asList(elements));
    }
    public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
        return new LinkedHashSet<E>(Maths.max(0, expectedSize));
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet<E>();
    }


    public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
        return new TreeSet<E>(Preconditions.checkNotNull(comparator));
    }

    public static <E> TreeSet<E> newTreeSet(@Nullable Iterable<E> elements) {
        return new TreeSet<E>(asList(elements));
    }



    public static <E> TreeSet<E> newTreeSet(@Nullable E... elements) {
        return new TreeSet<E>(asList(elements));
    }


    public static <E> Set<E> newIdentityHashSet() {
        return Collections.newSetFromMap(Maps.<E, Boolean>newIdentityHashMap());
    }

    public static <E> Set<E> newConcurrentHashSet() {
        return new ConcurrentHashSet<E>();
    }

    public static <E> Set<E> newConcurrentHashSet(Iterable<E> elements) {
        Set<E> set = newConcurrentHashSet();
        Collects.addAll(set, Collects.asList(elements));
        return set;
    }


    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet<E>();
    }

    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<E> elements) {
        CopyOnWriteArraySet<E> set = new CopyOnWriteArraySet<E>();
        Collects.addAll(set, Collects.asList(elements));
        return set;
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


    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }

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
    public static <K, V> TreeMap<K, V> emptyTreeMap() {
        return new TreeMap<K, V>();
    }

    /**
     * Get a empty, mutable java.util.TreeMap
     *
     * @param <K> Key
     * @param <V> Value
     * @return An empty, mutable java.util.TreeMap
     */
    public static <K, V> TreeMap<K, V> emptyTreeMap(@Nullable Comparator<K> comparator) {
        if (comparator == null) {
            return emptyTreeMap();
        }
        return new TreeMap<K, V>(comparator);
    }

    public static <K, V> Map<K, V> immutableMap() {
        return immutableMap(null);
    }

    public static <K, V> Map<K, V> immutableMap(Map<K, V> m) {
        if (m == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(m);
    }

    /**
     * Get a empty, mutable java.util.HashMap
     *
     * @param <K> Key
     * @param <V> Value
     * @return An empty, mutable java.util.HashMap
     */
    public static <K, V> HashMap<K, V> emptyHashMap() {
        return new HashMap<K, V>();
    }

    /**
     * Get a empty, mutable java.util.HashMap or java.util.LinkedHashMap
     *
     * @param <K> Key
     * @param <V> Value
     * @return An empty, mutable java.util.HashMap if is not sequential, else an empty, mutable java.util.LinkedHashMap
     */
    public static <K, V> HashMap<K, V> emptyHashMap(boolean sequential) {
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

    public static HashMap ofMap(Object... keyValuePairs) {
        int size = keyValuePairs.length % 2 == 0 ? keyValuePairs.length / 2 : (keyValuePairs.length / 2 + 1);
        HashMap map = new HashMap(size);
        for (int i = 0; i < size; i++) {
            int j = 2 * i;
            Object key = keyValuePairs[j];
            Object value = (j + 1) >= keyValuePairs.length ? null : keyValuePairs[j + 1];
            map.put(key, value);
        }
        return map;
    }
    public static <K, V> Map<K, V> newMap(Pair<K, V>... pairs) {
        Map<K, V> map = new HashMap<K, V>(pairs.length);
        for (int i = 0; i < pairs.length; ++i) {
            Pair<K, V> pair = pairs[i];
            map.put(pair.getKey(), pair.getValue());
        }

        return map;
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return Collects.emptyHashMap();
    }

    public static <K, V> HashMap<K, V> newHashMap(@Nullable Map<K, V> map) {
        if (Emptys.isEmpty(map)) {
            return emptyHashMap();
        }
        return new HashMap<K, V>(map);
    }

    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
        return new HashMap<K, V>(Maths.max(expectedSize, 0));
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<K, V> map) {
        return new LinkedHashMap<K, V>(map);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
        return new LinkedHashMap<K, V>(Maths.max(expectedSize, 0));
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return new ConcurrentHashMap<K, V>();
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentMap(Map<K,V> map) {
        if(map==null){
            return newConcurrentMap();
        }
        return new ConcurrentHashMap<K, V>(map);
    }

    public static <K, V> TreeMap<K, V> newTreeMap() {
        return Collects.emptyTreeMap();
    }

    public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, V> map) {
        return new TreeMap<K, V>(map);
    }

    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(Comparator<C> comparator) {
        return new TreeMap<K, V>(comparator);
    }

    public static <K, V> TreeMap<K, V> newTreeMap(Map<K, V> map) {
        if (map == null) {
            return new TreeMap<K, V>();
        }
        Comparator<K> comparator = null;
        if (map instanceof TreeMap) {
            comparator = ((TreeMap) map).comparator();
        }
        TreeMap<K, V> treeMap = new TreeMap<K, V>(comparator);
        treeMap.putAll(map);
        return treeMap;
    }

    public static <K, V> TreeMap<K, V> newTreeMap(Map<K, V> map, Comparator<K> keyComparator) {
        TreeMap<K, V> treeMap = new TreeMap<K, V>(keyComparator);
        if (Emptys.isNotEmpty(map)) {
            treeMap.putAll(map);
        }
        return treeMap;
    }
    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
        return new EnumMap<K, V>(Preconditions.checkNotNull(type));
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, V> map) {
        return new EnumMap<K, V>(map);
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap<K, V>();
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
        if (list instanceof com.jn.langx.util.collection.stack.Stack) {
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

    private static <E> Collection<E> newEmptyCollection(@Nullable Iterable<E> iterable) {
        if (iterable == null) {
            return emptyArrayList();
        }
        if (iterable instanceof Collection) {
            return emptyCollectionByInfer((Collection) iterable);
        }
        return emptyArrayList();
    }


    public static <E> Collection<E> asCollection(@Nullable Iterable<E> iterable) {
        if (Emptys.isNull(iterable)) {
            return emptyArrayList();
        }
        if (!(iterable instanceof Collection)) {
            final List<E> list = newArrayList();
            forEach(iterable, new Consumer<E>() {
                @Override
                public void accept(E e) {
                    list.add(e);
                }
            });
            return list;
        }
        return (Collection<E>) iterable;
    }

    public static <E> E[] emptyArray(@Nullable Class<E> componentType) {
        return Arrs.createArray(Primitives.wrap(componentType), 0);
    }


    public static <E, C extends Collection<E>> Object[] toArray(@Nullable C collection) {
        if (Emptys.isEmpty(collection)) {
            C c = (C) Collections.emptyList();
            return c.toArray();
        }
        return collection.toArray();
    }

    public static <E, C extends Collection<E>> E[] asArray(@Nullable C list, @NonNull Class<E> componentClass) {
        Preconditions.checkNotNull(componentClass);
        E[] array = Arrs.createArray(componentClass, Emptys.isEmpty(list) ? 0 : list.size());
        if (Emptys.isNotEmpty(list)) {
            list.toArray(array);
        }
        return array;
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
            List<E> a = asList(PrimitiveArrays.<E>wrap(object));
            if (mutable) {
                return asList(a);
            } else {
                return Collections.unmodifiableList(a);
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
        return filter(anyObject, predicate, null);
    }


    /**
     * Filter any object with the specified predicate
     */
    public static <E> Collection<E> filter(@Nullable Object anyObject, @Nullable Predicate<E> consumePredicate, @Nullable final Predicate<E> breakPredicate) {
        Iterable<E> iterable = asIterable(anyObject);
        final Collection<E> result = newEmptyCollection(iterable);
        consumePredicate = consumePredicate == null ? Functions.<E>truePredicate() : consumePredicate;
        forEach((Collection<E>) asCollection(iterable), consumePredicate, new Consumer<E>() {
            @Override
            public void accept(E e) {
                result.add(e);
            }
        }, breakPredicate);
        return result;
    }

    /**
     * Filter any object with the specified predicate
     */
    public static <E> Collection<E> filter(@Nullable Object anyObject, @Nullable Predicate2<Integer, E> consumePredicate, @Nullable final Predicate2<Integer, E> breakPredicate) {
        Iterable<E> iterable = asIterable(anyObject);
        final Collection<E> result = newEmptyCollection(iterable);
        consumePredicate = consumePredicate == null ? Functions.<Integer, E>truePredicate2() : consumePredicate;
        forEach((Collection<E>) asCollection(iterable), consumePredicate, new Consumer2<Integer, E>() {
            @Override
            public void accept(Integer index, E e) {
                result.add(e);
            }
        }, breakPredicate);
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
        final Collection result = newEmptyCollection(iterable);
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
        forEach((Iterable<E>) iterable, new Consumer<E>() {
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

    public static <E> Collection<E> flat(Collection<Collection<E>> collections) {
        return flatMap(collections, Functions.<E>noopFunction());
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
        forEach((Collection) collection, new Consumer<Collection<E>>() {
            @Override
            public void accept(Collection<E> c) {
                Collection<R> rs = Collects.map(c, mapper);
                list.addAll(rs);
            }
        });
        return list;
    }

    public static <E> void forEach(Object obj, @NonNull final Consumer<E> consumer) {
        Iterable<E> iterable = asIterable(obj);
        forEach(iterable, null, consumer, null);
    }

    public static <E, C extends Iterable<E>> void forEach(@Nullable C collection, @NonNull final Consumer<E> consumer) {
        forEach(collection, null, consumer, null);
    }

    public static <E, C extends Iterable<E>> void forEach(@Nullable C collection, @Nullable Predicate<E> consumePredicate, @NonNull final Consumer<E> consumer) {
        forEach(collection, consumePredicate, consumer, null);
    }

    public static <E, C extends Iterable<E>> void forEach(@Nullable C collection, @NonNull final Consumer<E> consumer, @Nullable Predicate<E> breakPredicate) {
        forEach(collection, null, consumer, breakPredicate);
    }

    /**
     * Consume every element what matched the consumePredicate
     */
    public static <E, C extends Iterable<E>> void forEach(@Nullable C collection, @Nullable Predicate<E> consumePredicate, @NonNull final Consumer<E> consumer, @Nullable Predicate<E> breakPredicate) {
        if (Emptys.isNotEmpty(collection)) {
            consumePredicate = consumePredicate == null ? Functions.<E>truePredicate() : consumePredicate;
            Iterator<E> iterator = collection.iterator();
            while (iterator.hasNext()) {
                E element = iterator.next();
                if (consumePredicate.test(element)) {
                    consumer.accept(element);
                }
                if (breakPredicate != null && breakPredicate.test(element)) {
                    break;
                }
            }
        }
    }

    public static <E> void forEach(Object obj, @NonNull final Consumer2<Integer, E> consumer) {
        Iterable<E> iterable = asIterable(obj);
        forEach(iterable, null, consumer, null);
    }

    public static <E, C extends Collection<E>> void forEach(@Nullable C collection, @NonNull final Consumer2<Integer, E> consumer) {
        forEach(collection, null, consumer, null);
    }

    /**
     * Iterate every element
     */
    public static <E, C extends Iterable<E>> void forEach(@Nullable C collection, @NonNull final Consumer2<Integer, E> consumer) {
        forEach(collection, null, consumer, null);
    }


    /**
     * Iterate every element
     */
    public static <E, C extends Iterable<E>> void forEach(@Nullable C collection, @NonNull final Consumer2<Integer, E> consumer, @Nullable final Predicate2<Integer, E> breakPredicate) {
        forEach(collection, null, consumer, breakPredicate);
    }

    /**
     * Consume every element what matched the consumePredicate
     */
    public static <E, C extends Iterable<E>> void forEach(@Nullable C collection, @Nullable final Predicate2<Integer, E> consumePredicate, @NonNull final Consumer2<Integer, E> consumer) {
        forEach(collection, consumePredicate, consumer, null);
    }


    /**
     * Consume every element what matched the consumePredicate
     */
    public static <E, C extends Iterable<E>> void forEach(@Nullable C collection, @Nullable Predicate2<Integer, E> consumePredicate, @NonNull final Consumer2<Integer, E> consumer, @Nullable Predicate2<Integer, E> breakPredicate) {
        consumePredicate = consumePredicate == null ? Functions.<Integer, E>truePredicate2() : consumePredicate;

        if (Emptys.isNotEmpty(collection)) {
            Iterator<E> iterator = collection.iterator();
            for (int i = 0; iterator.hasNext(); i++) {
                E element = iterator.next();
                if (consumePredicate.test(i, element)) {
                    consumer.accept(i, element);
                }
                if (breakPredicate != null && breakPredicate.test(i, element)) {
                    break;
                }
            }
        }
    }

    public static <E> void forEach(@Nullable E[] array, @NonNull final Consumer<E> consumer) {
        forEach(array, new Consumer2<Integer, E>() {
            @Override
            public void accept(Integer key, E value) {
                consumer.accept(value);
            }
        }, null);
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
        forEach(array, null, consumer, breakPredicate);
    }

    /**
     * consume every element that matched the consumePredicate
     */
    public static <E> void forEach(@Nullable E[] array, @Nullable final Predicate2<Integer, E> consumePredicate, @NonNull Consumer2<Integer, E> consumer) {
        forEach(array, consumePredicate, consumer, null);
    }

    /**
     * consume every element that matched the consumePredicate
     */
    public static <E> void forEach(@Nullable E[] array, @Nullable Predicate<E> consumePredicate, @NonNull Consumer<E> consumer, @Nullable final Predicate<E> breakPredicate) {
        consumePredicate = consumePredicate == null ? Functions.<E>truePredicate() : consumePredicate;

        if (Emptys.isNotEmpty(array)) {
            for (int i = 0; i < array.length; i++) {
                E element = array[i];
                if (consumePredicate.test(element)) {
                    consumer.accept(element);
                }
                if (breakPredicate != null && breakPredicate.test(element)) {
                    break;
                }
            }
        }
    }

    /**
     * consume every element that matched the consumePredicate
     */
    public static <E> void forEach(@Nullable E[] array, @Nullable Predicate2<Integer, E> consumePredicate, @NonNull Consumer2<Integer, E> consumer, @Nullable final Predicate2<Integer, E> breakPredicate) {
        consumePredicate = consumePredicate == null ? Functions.<Integer, E>truePredicate2() : consumePredicate;

        if (Emptys.isNotEmpty(array)) {
            for (int i = 0; i < array.length; i++) {
                E element = array[i];
                if (consumePredicate.test(i, element)) {
                    consumer.accept(i, element);
                }
                if (breakPredicate != null && breakPredicate.test(i, element)) {
                    break;
                }
            }
        }
    }


    /**
     * Iterate every element
     */
    public static <K, V, M extends Map<? extends K, ? extends V>> void forEach(@Nullable M map, @NonNull Consumer2<K, V> consumer) {
        forEach(map, null, consumer, null);
    }

    /**
     * Iterate every element
     */
    public static <K, V, M extends Map<? extends K, ? extends V>> void forEach(@Nullable M map, @NonNull Consumer2<K, V> consumer, @Nullable final Predicate2<K, V> breakPredicate) {
        forEach(map, null, consumer, breakPredicate);
    }

    /**
     * consume every element what matched the consumePredicate
     */
    public static <K, V, M extends Map<? extends K, ? extends V>> void forEach(@Nullable M map, @Nullable final Predicate2<K, V> consumePredicate, @NonNull Consumer2<K, V> consumer) {
        forEach(map, consumePredicate, consumer, null);
    }

    /**
     * consume every element what matched the consumePredicate
     */
    public static <K, V, M extends Map<? extends K, ? extends V>> void forEach(@Nullable M map, @Nullable Predicate2<K, V> consumePredicate, @NonNull Consumer2<K, V> consumer, @Nullable final Predicate2<K, V> breakPredicate) {
        Preconditions.checkNotNull(consumer);
        consumePredicate = consumePredicate == null ? Functions.<K, V>truePredicate2() : consumePredicate;
        if (Emptys.isNotEmpty(map)) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                if (consumePredicate.test(entry.getKey(), entry.getValue())) {
                    consumer.accept(entry.getKey(), entry.getValue());
                }
                if (breakPredicate != null && breakPredicate.test(entry.getKey(), entry.getValue())) {
                    break;
                }
            }
        }
    }

    public static <E, C extends Collection<E>> Integer firstOccurrence(C c, final E item) {
        return firstOccurrence(c, new Predicate2<Integer, E>() {
            @Override
            public boolean test(Integer key, E value) {
                return Objs.equals(value, item);
            }
        });
    }

    public static <E, C extends Collection<E>> int firstOccurrence(C c, Predicate2<Integer, E> predicate) {
        List<Pair<Integer, E>> pairs = Collects.findNPairs(c, predicate, 1);
        if (Emptys.isEmpty(pairs)) {
            return -1;
        }
        Pair<Integer, E> pair = pairs.get(0);
        return pair.getKey();
    }


    /**
     * find the first matched element, null if not found
     */
    public static <E, C extends Iterable<E>, O> O firstMap(@Nullable C collection, @NonNull final Function2<Integer, E, O> mapper) {
        return firstMap(collection, mapper, (Predicate<O>) null);
    }

    /**
     * map every element in the collection with the mapper,
     * break the traverse if the mapped result match the breakPredicate
     * <p>
     * return the mapped result
     */
    public static <E, C extends Iterable<E>, O> O firstMap(@Nullable C collection, @NonNull final Function2<Integer, E, O> mapper, final Predicate<O> breakPredicate) {
        final Holder<O> holder = new Holder<O>();
        Collects.forEach(collection, new Consumer2<Integer, E>() {
            @Override
            public void accept(Integer index, E value) {
                holder.set(mapper.apply(index, value));
            }
        }, new Predicate2<Integer, E>() {
            @Override
            public boolean test(Integer index, E value) {
                return breakPredicate == null ? !holder.isNull() : breakPredicate.test(holder.get());
            }
        });
        return holder.get();
    }

    /**
     * map every element in the collection with the mapper,
     * break the traverse if the mapped result match the breakPredicate
     * <p>
     * return the mapped result
     */
    public static <E, C extends Iterable<E>, O> O firstMap(@Nullable C collection, @NonNull final Function2<Integer, E, O> mapper, final Predicate2<E, O> breakPredicate) {
        final Holder<O> holder = new Holder<O>();
        Collects.forEach(collection, new Consumer2<Integer, E>() {
            @Override
            public void accept(Integer index, E element) {
                holder.set(mapper.apply(index, element));
            }
        }, new Predicate2<Integer, E>() {
            @Override
            public boolean test(Integer index, E element) {
                return breakPredicate == null ? !holder.isNull() : breakPredicate.test(element, holder.get());
            }
        });
        return holder.get();
    }


    /**
     * find the first matched element, null if not found
     */
    public static <K, V, O> O firstMap(@Nullable Map<K, V> map, @NonNull final Function2<K, V, O> mapper) {
        return firstMap(map, mapper, null);
    }

    /**
     * find the first matched element, null if not found
     */
    public static <K, V, O> O firstMap(@Nullable Map<K, V> map, @NonNull final Function2<K, V, O> mapper, final Predicate<O> breakPredicate) {
        final Holder<O> holder = new Holder<O>();
        Collects.forEach(map, new Consumer2<K, V>() {
            @Override
            public void accept(K index, V value) {
                holder.set(mapper.apply(index, value));
            }
        }, new Predicate2<K, V>() {
            @Override
            public boolean test(K key, V value) {
                return breakPredicate == null ? !holder.isNull() : breakPredicate.test(holder.get());
            }
        });
        return holder.get();
    }

    /**
     * find the first matched element, null if not found
     */
    public static <E, C extends Collection<E>> E findFirst(@Nullable C collection) {
        return findFirst(collection, Functions.<E>nonNullPredicate());
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
    public static <E, C extends Collection<E>> List<E> findN(@Nullable C collection, @Nullable Predicate<E> predicate, final int n) {
        final List<E> ret = Collects.emptyArrayList();
        if (n <= 0 || Emptys.isEmpty(collection)) {
            return ret;
        }

        forEach(collection, predicate, new Consumer<E>() {
            @Override
            public void accept(E e) {
                ret.add(e);
            }
        }, new Predicate<E>() {
            @Override
            public boolean test(E value) {
                return ret.size() == n;
            }
        });

        return ret;
    }


    /**
     * find the first matched element, null if not found
     */
    public static <E, C extends Collection<E>> List<Pair<Integer, E>> findNPairs(@Nullable C collection, @Nullable Predicate2<Integer, E> predicate, final int n) {
        final List<Pair<Integer, E>> ret = Collects.emptyArrayList();
        if (n <= 0 || Emptys.isEmpty(collection)) {
            return ret;
        }

        forEach(collection, predicate, new Consumer2<Integer, E>() {
            @Override
            public void accept(Integer index, E e) {
                ret.add(new Pair<Integer, E>(index, e));
            }
        }, new Predicate2<Integer, E>() {
            @Override
            public boolean test(Integer index, E value) {
                return ret.size() == n;
            }
        });
        return ret;
    }


    /**
     * find the first matched element, null if not found
     */
    public static <K, V> Map<? extends K, ? extends V> findN(@Nullable Map<? extends K, ? extends V> map, @Nullable Predicate2<K, V> predicate, final int n) {
        final Map<K, V> ret = emptyHashMap(true);
        if (n <= 0 || map == null || map.isEmpty()) {
            return ret;
        }
        forEach(map, predicate, new Consumer2<K, V>() {
            @Override
            public void accept(K key, V value) {
                ret.put(key, value);
            }
        }, new Predicate2<K, V>() {
            @Override
            public boolean test(K key, V value) {
                return n == ret.size();
            }
        });
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

    /**
     * 从 collection 中移除 所有出现在 removed中的
     *
     * @param collection
     * @param removed
     * @since 4.6.5
     */
    public static <E, C extends Collection<E>> void removeAll(C collection, final C removed) {
        removeIf(collection, new Predicate<E>() {
            @Override
            public boolean test(E element) {
                return removed.contains(element);
            }
        });
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

    public static boolean anyMatch(@NonNull Predicate predicate, Object... array) {
        return anyMatch(asList(array), predicate);
    }


    /**
     * has any element match the specified condition
     *
     * @return whether has any element removed
     */
    public static <E, C extends Collection<E>> boolean anyMatch(@Nullable C collection, @NonNull Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(collection)) {
            // 不能采用 findFirst ，原因是 如果 predicat 就是来 取null 或者 empty的，而collection 中恰好有 null 或者 empty，那么取到时，结果也是 null
            // 没办法说清楚返回值是否为取到的结果
            // E e = findFirst(collection, predicate);
            // return e != null;
            final Holder<Boolean> found = new Holder<Boolean>(false);
            forEach(collection, predicate, new Consumer<E>() {
                @Override
                public void accept(E e) {
                    found.set(true);
                }
            }, new Predicate<E>() {
                @Override
                public boolean test(E value) {
                    return found.get();
                }
            });
            return found.get();
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

    public static boolean allMatch(@NonNull Predicate predicate, @Nullable Object... collection) {
        return allMatch(asList(collection), predicate);
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

    public static boolean noneMatch(@NonNull Predicate predicate, @Nullable Object... collection) {
        return noneMatch(asList(collection), predicate);
    }

    /**
     * has no any element match the specified condition ?
     *
     * @return whether has any element removed
     */
    public static <E, C extends Collection<E>> boolean noneMatch(@Nullable C collection, @NonNull Predicate<E> predicate) {
        Preconditions.checkNotNull(predicate);
        if (Emptys.isNotEmpty(collection)) {
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

    public static <E> E[] limit(E[] array, int maxSize) {
        return (E[]) toArray(limit(asList(array), maxSize));
    }

    /**
     * truncate a collection using subList(0, maxSize)
     */
    public static <E, C extends Collection<E>> List<E> limit(@Nullable C collection, int maxSize) {
        if (Emptys.isEmpty(collection)) {
            return emptyArrayList();
        }

        Preconditions.checkTrue(maxSize >= 0);

        List<E> list = (collection instanceof List) ? (List<E>) collection : new LinkedList<E>(collection);
        if (list.size() <= maxSize) {
            return list;
        }
        return list.subList(0, maxSize);
    }

    public static <E> E[] skip(@Nullable E[] array, int n) {
        return (E[]) toArray(skip(asList(array), n), (Class<E[]>) array.getClass());
    }

    /**
     * skip n elements, get a collection using subList(n, size)
     */
    public static <E, C extends Collection<E>> List<E> skip(@Nullable C collection, int n) {
        if (Emptys.isEmpty(collection)) {
            return emptyArrayList();
        }
        Preconditions.checkTrue(n >= 0);
        List<E> list = (collection instanceof List) ? (List<E>) collection : new LinkedList<E>(collection);
        if (list.size() <= n) {
            return emptyArrayList();
        }
        return list.subList(n, list.size());
    }

    public static <E, K> List<List<E>> partitionBy(Iterator<E> c, Function<E, K> classifier) {
        return asList(collect(c, partitioningBy(classifier)).values());
    }

    public static <E, K> List<List<E>> partitionBy(Iterator<E> c, Function2<Integer, E, K> classifier) {
        return asList(collect(c, partitioningBy(classifier)).values());
    }

    public static <E, K> List<List<E>> partitionBy(E[] c, Function<E, K> classifier) {
        return asList(collect(c, partitioningBy(classifier)).values());
    }

    public static <E, K> List<List<E>> partitionBy(E[] c, Function2<Integer, E, K> classifier) {
        return asList(collect(c, partitioningBy(classifier)).values());
    }

    public static <E, K> List<List<E>> partitionBy(Iterable<E> c, Function<E, K> classifier) {
        return asList(collect(c, partitioningBy(classifier)).values());
    }

    public static <E, K> List<List<E>> partitionBy(Iterable<E> c, Function2<Integer, E, K> classifier) {
        return asList(collect(c, partitioningBy(classifier)).values());
    }

    /**
     * 第二个参数为分区总数，不是单个分区中的数据量
     */
    public static <E> List<List<E>> partitionByCount(Iterable<E> c, final int partitionCount) {
        Preconditions.checkArgument(partitionCount > 0);
        return partitionBy(c, new Function2<Integer, E, Integer>() {
            @Override
            public Integer apply(Integer index, E element) {
                return index % partitionCount;
            }
        });
    }

    /**
     * 第二个参数为单个分区中的数据量
     *
     * @param c
     * @param partitionSize
     * @param <E>
     */
    public static <E> List<List<E>> partitionBySize(Iterable<E> c, final int partitionSize) {
        Preconditions.checkArgument(partitionSize > 0);
        return partitionBy(c, new Function2<Integer, E, Integer>() {
            @Override
            public Integer apply(Integer index, E element) {
                return index / partitionSize;
            }
        });
    }

    /**
     * sort a collection, return an new list. it is different to
     * Collections.sort(list) is that : Collections.sort() return void
     */
    public static <E extends Comparable<E>, C extends Collection<E>> List<E> sort(@Nullable C collection, boolean reverse) {
        return sort(collection, new ComparableComparator<E>(), reverse);
    }

    /**
     * sort a collection, return an new list. it is different to
     * Collections.sort(list) is that : Collections.sort() return void
     */
    public static <E, C extends Collection<E>> List<E> sort(@Nullable C collection, @NonNull Comparator<E> comparator) {
        return sort(collection, comparator, false);
    }

    /**
     * sort a collection, return an new list. it is different to
     * Collections.sort(list) is that : Collections.sort() return void
     */
    public static <E, C extends Collection<E>> List<E> sort(@Nullable C collection, @NonNull Comparator<E> comparator, boolean reverse) {
        Preconditions.checkNotNull(comparator);
        if (Emptys.isEmpty(collection)) {
            return Collects.emptyArrayList();
        } else {
            comparator = comparator == null ? new ComparableComparator() : comparator;
            Comparator c = reverse ? Collections.reverseOrder(comparator) : comparator;
            Object[] a = Collects.toArray(collection);
            TimSort.sort(a, c);
            return (List<E>) asList(a);
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

    public static <E> List<E> reverse(@Nullable List<E> list) {
        return reverse(list, false);
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
        forEach(asCollection(asIterable(anyObject)), null, new Consumer<Object>() {
            @Override
            public void accept(Object object) {
                count.set(count.get() + 1);
            }
        }, null);
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
        forEach((Collection<E>) asCollection(asIterable(anyObject)), new Consumer<E>() {
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

    public static <E, C extends Collection<E>> void addAll(@NonNull final C dest, @Nullable Iterable<E> iterable) {
        Preconditions.checkNotNull(dest);
        if (Emptys.isNotEmpty(iterable)) {
            forEach(iterable, new Consumer<E>() {
                @Override
                public void accept(E e) {
                    dest.add(e);
                }
            });
        }
    }

    public static <E, C extends Collection<E>> void addAll(@NonNull C dest, @Nullable E... iterator) {
        addAll(dest, Collects.<E>asIterable(iterator));
    }

    public static <E, C extends Collection<E>> void addAll(@NonNull C dest, @Nullable Iterator<E> iterator) {
        addAll(dest, Collects.<E>asIterable(iterator));
    }

    public static <E, C extends Collection<E>> void addIf(Collection<E> collection, E e, Predicate2<Collection<E>,E> predicate){
        if(predicate.test(collection, e)){
            collection.add(e);
        }
    }

    public static <E, C extends Collection<E>> void addTo(@NonNull C srcCollection, @Nullable C targetCollection) {
        addAll(targetCollection, srcCollection);
    }

    public static <E1, C1 extends Collection<E1>, E2, C2 extends Collection<E2>> void addTo(C1 srcCollection, C2 targetCollection, Function<E1, E2> mapper) {
        Collection<E2> mapped = Collects.map(srcCollection, mapper);
        addTo(mapped, targetCollection);
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

    /**
     * Clones a map.
     *
     * @param source the Map to clone
     * @param <K>    the map key type
     * @param <V>    the map value type
     * @return the cloned map
     */
    public static <K, V> Map<K, V> copy(final Map<K, V> source) {

        if (source == null) {
            return null;
        }

        final Map<K, V> result = new HashMap<K, V>();
        result.putAll(source);
        return result;
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

    /**
     * test c1 contains any element in c2
     */
    public static <E, C1 extends Collection<E>, C2 extends Collection<E>> boolean containsAny(final C1 c1, C2 c2) {
        if (Emptys.isEmpty(c1)) {
            return false;
        }
        return anyMatch(c2, new Predicate<E>() {
            @Override
            public boolean test(E value) {
                return c1.contains(value);
            }
        });
    }

    public static <E> boolean contains(E[] collection, final E obj) {
        return contains(collection, obj, null);
    }

    public static <E> boolean contains(Collection<E> collection, final E obj) {
        return contains(collection, obj, null);
    }

    public static <E> boolean contains(E[] collection, @Nullable final E obj, @Nullable Predicate<E> predicate) {
        return contains(asCollection(Collects.<E>asIterable(collection)), obj, predicate);
    }

    public static <E> boolean contains(Collection<E> collection, @Nullable final E obj, @Nullable Predicate<E> predicate) {
        Predicate<E> p = predicate == null ? Functions.<E>equalsPredicate(obj) : predicate;
        return Collects.anyMatch(collection, p);
    }

    /**
     * test c1 contains all elements in c2
     */
    public static <E, C1 extends Collection<E>, C2 extends Collection<E>> boolean containsAll(final C1 c1, C2 c2) {
        if (Emptys.isEmpty(c1)) {
            return false;
        }
        return allMatch(c2, new Predicate<E>() {
            @Override
            public boolean test(E value) {
                return c1.contains(value);
            }
        });
    }

    /**
     * test c1 contains all elements in c2
     */
    public static <E, C1 extends Collection<E>, C2 extends Collection<E>> boolean containsNone(final C1 c1, C2 c2) {
        if (Emptys.isEmpty(c1)) {
            return true;
        }
        return noneMatch(c2, new Predicate<E>() {
            @Override
            public boolean test(E value) {
                return c1.contains(value);
            }
        });
    }

    public static <E, C1 extends Collection<E>, C2 extends Collection<E>> Set<E> intersection(final C1 c1, final C2 c2) {
        final Set<E> set = emptyHashSet(true);
        if (Emptys.isEmpty(c1) || Emptys.isEmpty(c2)) {
            return set;
        }
        List<E> allElements = emptyArrayList();
        allElements.addAll(c1);
        allElements.addAll(c2);
        forEach((Collection<E>) allElements, new Predicate<E>() {
            @Override
            public boolean test(E element) {
                return set.contains(element) || (c1.contains(element) && c2.contains(element));
            }
        }, new Consumer<E>() {
            @Override
            public void accept(E e) {
                set.add(e);
            }
        }, null);
        return set;
    }

    public static <E, C1 extends Collection<E>, C2 extends Collection<E>> Set<E> union(final C1 c1, final C2 c2) {
        Set<E> allElements = emptyHashSet(true);
        allElements.addAll(Emptys.isEmpty(c1) ? Collects.<E>emptyArrayList() : c1);
        allElements.addAll(Emptys.isEmpty(c2) ? Collects.<E>emptyArrayList() : c2);
        return allElements;
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
        Collects.forEach((Collection<E>) asCollection(asIterable(iterable)), new Consumer<E>() {
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
                return Functions.emptyTreeSetSupplier0(comparator);
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
                return Functions.emptyHashSetSupplier0();
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

    public static <E, K> Collector<E, Map<K, List<E>>> groupingBy(@NonNull final Function2<Integer, E, K> classifier, @NonNull final Supplier0<Map<K, List<E>>> mapFactory) {
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
                    private int index = 0;

                    @Override
                    public void accept(Map<K, List<E>> map, E e) {
                        K group = classifier.apply(index, e);
                        List<E> list = map.get(group);
                        if (list == null) {
                            list = new ArrayList<E>();
                            map.put(group, list);
                        }
                        list.add(e);
                        index++;
                    }
                };
            }
        };
    }

    public static <E, K> Collector<E, Map<K, List<E>>> partitioningBy(@NonNull final Function<E, K> classifier) {
        return groupingBy(classifier, new Supplier0<Map<K, List<E>>>() {
            @Override
            public Map<K, List<E>> get() {
                return new NonAbsentTreeMap<K, List<E>>(new Supplier<K, List<E>>() {
                    @Override
                    public List<E> get(K input) {
                        return new ArrayList<E>();
                    }
                });
            }
        });
    }

    public static <E, K> Collector<E, Map<K, List<E>>> partitioningBy(@NonNull final Function2<Integer, E, K> classifier) {
        return groupingBy(classifier, new Supplier0<Map<K, List<E>>>() {
            @Override
            public Map<K, List<E>> get() {
                return new NonAbsentTreeMap<K, List<E>>(new Supplier<K, List<E>>() {
                    @Override
                    public List<E> get(K input) {
                        return new ArrayList<E>();
                    }
                });
            }
        });
    }

    public static <E, C extends Collection<E>> C clearNulls(@Nullable C collection) {
        return (C) filter(collection, Functions.nonNullPredicate());
    }
    public static <E, C extends Collection<E>> C clearEmptys(@Nullable C collection) {
        return (C) filter(collection, Functions.notEmptyPredicate());
    }

    /**
     * Randomly permutes the specified list using a default source of
     * randomness.  All permutations occur with approximately equal
     * likelihood.<p>
     * <p>
     * The hedge "approximately" is used in the foregoing description because
     * default source of randomness is only approximately an unbiased source
     * of independently chosen bits. If it were a perfect source of randomly
     * chosen bits, then the algorithm would choose permutations with perfect
     * uniformity.<p>
     * <p>
     * This implementation traverses the list backwards, from the last element
     * up to the second, repeatedly swapping a randomly selected element into
     * the "current position".  Elements are randomly selected from the
     * portion of the list that runs from the first element to the current
     * position, inclusive.<p>
     * <p>
     * This method runs in linear time.  If the specified list does not
     * implement the {@link RandomAccess} interface and is large, this
     * implementation dumps the specified list into an array before shuffling
     * it, and dumps the shuffled array back into the list.  This avoids the
     * quadratic behavior that would result from shuffling a "sequential
     * access" list in place.
     *
     * @param list the list to be shuffled.
     * @throws UnsupportedOperationException if the specified list or
     *                                       its list-iterator does not support the <tt>set</tt> operation.
     */
    public static <E> void shuffle(@NonNull List<E> list) {
        shuffle(list, GlobalThreadLocalMap.getRandom());
    }

    /**
     * Randomly permute the specified list using the specified source of
     * randomness.  All permutations occur with equal likelihood
     * assuming that the source of randomness is fair.<p>
     * <p>
     * This implementation traverses the list backwards, from the last element
     * up to the second, repeatedly swapping a randomly selected element into
     * the "current position".  Elements are randomly selected from the
     * portion of the list that runs from the first element to the current
     * position, inclusive.<p>
     * <p>
     * This method runs in linear time.  If the specified list does not
     * implement the {@link RandomAccess} interface and is large, this
     * implementation dumps the specified list into an array before shuffling
     * it, and dumps the shuffled array back into the list.  This avoids the
     * quadratic behavior that would result from shuffling a "sequential
     * access" list in place.
     *
     * @param list the list to be shuffled.
     * @param rnd  the source of randomness to use to shuffle the list.
     * @throws UnsupportedOperationException if the specified list or its
     *                                       list-iterator does not support the <tt>set</tt> operation.
     */
    public static <E> void shuffle(@NonNull List<E> list, @NonNull IRandom rnd) {
        int size = list.size();
        if (size < 5 || list instanceof RandomAccess) {
            for (int i = size; i > 1; i--) {
                swap(list, i - 1, rnd.nextInt(i));
            }
        } else {
            Object arr[] = list.toArray();

            // Shuffle array
            for (int i = size; i > 1; i--) {
                swap(arr, i - 1, rnd.nextInt(i));
            }

            // Dump array back into list
            ListIterator it = list.listIterator();
            for (int i = 0; i < arr.length; i++) {
                it.next();
                it.set(arr[i]);
            }
        }
    }


    /**
     * Swaps the elements at the specified positions in the specified list.
     * (If the specified positions are equal, invoking this method leaves
     * the list unchanged.)
     *
     * @param list The list in which to swap elements.
     * @param i    the index of one element to be swapped.
     * @param j    the index of the other element to be swapped.
     * @throws IndexOutOfBoundsException if either <tt>i</tt> or <tt>j</tt>
     *                                   is out of range (i &lt; 0 || i &gt;= list.size()
     *                                   || j &lt; 0 || j &gt;= list.size()).
     */
    public static <E> void swap(@NonNull List<E> list, int i, int j) {
        Preconditions.checkArgument(i > 0, StringTemplates.indexStyleSupplier("index i is illegal : {}"), i);
        Preconditions.checkArgument(j > 0, StringTemplates.indexStyleSupplier("index j is illegal : {}"), j);
        final List<E> l = list;
        l.set(i, l.set(j, l.get(i)));
    }

    /**
     * Swaps the two specified elements in the specified array.
     */
    public static <E> void swap(@NonNull E[] arr, int i, int j) {
        Arrs.swap(arr, i, j);
    }

    public static <E> int indexOf(List<E> list, E e) {
        return indexOf(list, e, 0);
    }

    public static <E> int indexOf(List<E> list, E e, int startIndex) {
        return indexOf(list, e, startIndex, Emptys.isEmpty(list) ? 0 : list.size());
    }

    /**
     * [startIndex, endIndex)
     *
     * @param list
     * @param e
     * @param startIndex 包含
     * @param endIndex   不包含
     * @param <E>
     */
    public static <E> int indexOf(List<E> list, E e, int startIndex, int endIndex) {
        if (list == null || list.isEmpty()) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (startIndex >= list.size()) {
            return -1;
        }
        if (endIndex <= 0) {
            return -1;
        }
        if (endIndex > list.size()) {
            endIndex = list.size();
        }
        if (startIndex >= endIndex) {
            return -1;
        }
        ArrayList<E> arrayList = (list instanceof ArrayList) ? (ArrayList<E>) list : newArrayList(list);
        for (int i = startIndex; i < endIndex; i++) {
            if (Objs.equals(arrayList.get(i), e)) {
                return i;
            }
        }
        return -1;
    }

    public static <E> int lastIndexOf(List<E> list, E e) {
        return lastIndexOf(list, e, 0);
    }

    /**
     * @since 5.4.6
     */
    public static <E> int lastIndexOf(List<E> list, Predicate<E> predicate) {
        return lastIndexOf(list, predicate, 0);
    }

    public static <E> int lastIndexOf(List<E> list, E e, int startIndex) {
        return lastIndexOf(list, e, startIndex, Emptys.isEmpty(list) ? 0 : list.size());
    }
    /**
     * @since 5.4.6
     */
    public static <E> int lastIndexOf(List<E> list, Predicate<E> predicate, int startIndex) {
        return lastIndexOf(list, predicate, startIndex, Emptys.isEmpty(list) ? 0 : list.size());
    }

    /**
     * [startIndex, endIndex)
     *
     * @param list
     * @param e
     * @param startIndex
     * @param endIndex
     * @param <E>
     */
    public static <E> int lastIndexOf(List<E> list, final E e, int startIndex, int endIndex) {
        return lastIndexOf(list, new Predicate<E>() {
            @Override
            public boolean test(E value) {
                return Objs.equals(value, e);
            }
        }, startIndex, endIndex);
    }

    /**
     * @since 5.4.6
     */
    public static <E> int lastIndexOf(List<E> list, Predicate<E> predicate, int startIndex, int endIndex) {
        if (list == null || list.isEmpty()) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (startIndex >= list.size()) {
            return -1;
        }
        if (endIndex <= 0) {
            return -1;
        }
        if (endIndex > list.size()) {
            endIndex = list.size();
        }
        if (startIndex >= endIndex) {
            return -1;
        }
        ArrayList<E> arrayList = (list instanceof ArrayList) ? (ArrayList<E>) list : newArrayList(list);
        for (int i = endIndex - 1; i >= startIndex; i--) {
            if(predicate.test(arrayList.get(i))){
                return i;
            }
        }
        return -1;
    }
    public static <E> int indexOf(E[] list, E e) {
        return indexOf(asList(list), e);
    }

    public static <E> int indexOf(E[] list, E e, int startIndex) {
        return indexOf(asList(list), e, startIndex);
    }

    public static <E> int indexOf(E[] list, E e, int startIndex, int endIndex) {
        return indexOf(asList(list), e, startIndex, endIndex);
    }

    public static <E> int lastIndexOf(E[] list, E e) {
        return lastIndexOf(asList(list), e);
    }

    /**
     * @since 5.4.6
     */
    public static <E> int lastIndexOf(E[] list, Predicate<E> predicate) {
        return lastIndexOf(asList(list), predicate);
    }

    /**
     * @since 5.4.6
     */
    public static <E> int lastIndexOf(E[] list, E e, int startIndex) {
        return lastIndexOf(asList(list), e, startIndex);
    }

    /**
     * @since 5.4.6
     */
    public static <E> int lastIndexOf(E[] list, Predicate<E> predicate, int startIndex) {
        return lastIndexOf(asList(list), predicate, startIndex);
    }

    public static <E> int lastIndexOf(E[] list, E e, int startIndex, int endIndex) {
        return lastIndexOf(asList(list), e, startIndex, endIndex);
    }

    /**
     * @since 5.4.6
     */
    public static <E> int lastIndexOf(E[] list, Predicate<E> predicate, int startIndex, int endIndex) {
        return lastIndexOf(asList(list), predicate, startIndex, endIndex);
    }

    public static <E> E apply(E input, Function<E, E>... mappers) {
        for (int i = 0; i < mappers.length; i++) {
            input = mappers[i].apply(input);
        }
        return input;
    }

    public static <E> boolean isFirst(@Nullable E obj, @Nullable List<E> list) {
        if (Objs.isEmpty(list)) {
            return false;
        }
        return Objs.equals(new ListSequence<E>(list).first(), obj);
    }

    public static <E> boolean isLast(@Nullable E obj, @Nullable List<E> list) {
        if (Objs.isEmpty(list)) {
            return false;
        }
        return Objs.equals(new ListSequence<E>(list).last(), obj);
    }

    public static <E> boolean isFirst(@Nullable E obj, @Nullable Iterable<E> list) {
        if (Objs.isEmpty(list)) {
            return false;
        }
        return Objs.equals(new IterableSequence<E>(list).first(), obj);
    }

    public static <E> boolean isLast(@Nullable E obj, @Nullable Iterable<E> list) {
        if (Objs.isEmpty(list)) {
            return false;
        }
        return Objs.equals(new IterableSequence<E>(list).last(), obj);
    }

    public static <E> boolean isFirst(@Nullable E obj, @Nullable SortedSet<E> set) {
        if (Objs.isEmpty(set)) {
            return false;
        }
        return Objs.equals(new SortedSetSequence<E>(set).first(), obj);
    }

    public static <E> boolean isLast(@Nullable E obj, @Nullable SortedSet<E> set) {
        if (Objs.isEmpty(set)) {
            return false;
        }
        return Objs.equals(new SortedSetSequence<E>(set).last(), obj);
    }
}
