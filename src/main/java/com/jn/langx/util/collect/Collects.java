package com.jn.langx.util.collect;

import com.jn.langx.util.Emptys;

import java.util.*;

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
        Properties
    }

    public static <K, V> Map<K, V> getEmptyMapIfNull(Map<K, V> map) {
        return getEmptyMapIfNull(map, null);
    }

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
        TreeSet
    }

    public static <E> Set<E> getEmptySetIfNull(Set<E> set) {
        return getEmptySetIfNull(set);
    }

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
        LinkedList
    }

    public static <E> List<E> getEmptyListIfNull(List<E> list) {
        return getEmptyListIfNull(list, null);
    }

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

    public static <E> List<E> asList(E[] array) {
        return asList(array, true, ListType.ArrayList);
    }

    public static <E> List<E> asList(E[] array, ListType listType) {
        return asList(array, true, listType);
    }

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

    public static <E> E[] toArray(List<E> list) {
        if (Emptys.isEmpty(list)) {
            list = Collections.emptyList();
        }
        return (E[]) list.toArray();
    }

    public static <E> E[] toArray(Set<E> set){
        if (Emptys.isEmpty(set)) {
            set = Collections.emptySet();
        }
        return (E[]) set.toArray();
    }

    public static <E> void forEach(E anyObject){

    }

    public static <E> void forEach(String array){

    }

    public static <E> void forEach(E[] array){

    }

    public static <E> Iterable<E> asIterable(Object object){
        if(Emptys.isNull(object)){
            return asList(null);
        }
        if(object instanceof Iterable){
            return (Iterable)object;
        }

        if(object instanceof Map){
            return ((Map)object).entrySet();
        }

        if(object instanceof Iterator){
            return new IteratorIterable<E>((Iterator<E>) object);
        }

        if(object instanceof Enumeration){
            return new EnumerationIterable<E>((Enumeration<E>)object);
        }

        if(object instanceof Number){
            return (Iterable<E>)asList(Arrs.wrapAsArray((Number) object));
        }

        if(object instanceof String){
            return (Iterable<E>)asList(Arrs.wrapAsArray((String)object));
        }

        return (Iterable<E>)asList(Arrs.wrapAsArray(object));

    }

    public static <E> void forEach(Enumeration<E> iterator, Consumer<E> consumer){
        while (iterator.hasMoreElements()){
            E e = iterator.nextElement();
            consumer.accept(e);
        }
    }


    public static <E> void forEach(Iterator<E> iterator, Consumer<E> consumer){
        while (iterator.hasNext()){
            E e = iterator.next();
            consumer.accept(e);
        }
    }

    public static <E> void forEach(Iterable<E> iterable, Consumer<E> consumer){
        for(E e : iterable){
            consumer.accept(e);
        }
    }
}
