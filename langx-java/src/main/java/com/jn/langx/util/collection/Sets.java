package com.jn.langx.util.collection;


import com.jn.langx.util.Maths;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.concurrent.ConcurrentHashSet;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;


public class Sets {


    public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
        EnumSet<E> set = EnumSet.noneOf(elementType);
        Collects.addAll(set, Collects.<E>asList(iterable));
        return set;
    }

    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }

    public static <E> HashSet<E> newHashSet(E... elements) {
        HashSet<E> set = newHashSetWithExpectedSize(elements.length);
        Collects.addAll(set, Collects.<E>asList(elements));
        return set;
    }

    public static <E> HashSet<E> newHashSet(Iterable<E> elements) {
        return Collects.<E>newHashSet(elements);
    }

    public static <E> HashSet<E> newHashSet(Iterator<E> elements) {
        HashSet<E> set = newHashSet();
        Collects.addAll(set, Collects.<E>asIterable(elements));
        return set;
    }

    public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
        return new HashSet<E>(Maths.max(0, expectedSize));
    }

    public static <E> Set<E> newConcurrentHashSet() {
        return new ConcurrentHashSet<E>();
    }

    public static <E> Set<E> newConcurrentHashSet(Iterable<E> elements) {
        Set<E> set = newConcurrentHashSet();
        Collects.addAll(set, Collects.asList(elements));
        return set;
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet<E>();
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<E> elements) {
        LinkedHashSet<E> set = newLinkedHashSet();
        Collects.addAll(set, Collects.asList(elements));
        return set;
    }

    public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
        return new LinkedHashSet<E>(Maths.max(0, expectedSize));
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet<E>();
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<E> elements) {
        TreeSet<E> set = newTreeSet();
        Collects.addAll(set, Collects.asList(elements));
        return set;
    }

    public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
        return new TreeSet<E>(Preconditions.checkNotNull(comparator));
    }

    public static <E> Set<E> newIdentityHashSet() {
        return Collections.newSetFromMap(Maps.<E, Boolean>newIdentityHashMap());
    }

    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet<E>();
    }

    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<E> elements) {
        CopyOnWriteArraySet<E> set = new CopyOnWriteArraySet<E>();
        Collects.addAll(set, Collects.asList(elements));
        return set;
    }

}
