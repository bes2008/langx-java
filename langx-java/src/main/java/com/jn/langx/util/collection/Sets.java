package com.jn.langx.util.collection;


import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class Sets {

    public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
        return Collects.newEnumSet(iterable, elementType);
    }

    public static <E> HashSet<E> newHashSet() {
        return Collects.newHashSet();
    }

    public static <E> HashSet<E> newHashSet(E... elements) {
        return Collects.newHashSet(elements);
    }

    public static <E> HashSet<E> newHashSet(Iterable<E> elements) {
        return Collects.newHashSet(elements);
    }

    public static <E> HashSet<E> newHashSet(Iterator<E> elements) {
        return Collects.newHashSet(elements);
    }

    public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
        return Collects.newHashSetWithExpectedSize(expectedSize);
    }

    public static <E> Set<E> newConcurrentHashSet() {
        return Collects.newConcurrentHashSet();
    }

    public static <E> Set<E> newConcurrentHashSet(Iterable<E> elements) {
        return Collects.newConcurrentHashSet(elements);
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return Collects.newLinkedHashSet();
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<E> elements) {
        return Collects.newLinkedHashSet(elements);
    }

    public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
        return Collects.newLinkedHashSetWithExpectedSize(expectedSize);
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return Collects.newTreeSet();
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<E> elements) {
        return Collects.newTreeSet(elements);
    }

    public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
        return Collects.newTreeSet(comparator);
    }

    public static <E> Set<E> newIdentityHashSet() {
        return Collects.newIdentityHashSet();
    }

    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return Collects.newCopyOnWriteArraySet();
    }

    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<E> elements) {
        return Collects.newCopyOnWriteArraySet(elements);
    }

}
