package com.jn.langx.util.collection;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class Lists {
    private Lists() {
    }

    public static <E> ArrayList<E> newArrayList() {
        return Collects.newArrayList();
    }

    public static <E> ArrayList<E> newArrayList(E... elements) {
        return Collects.newArrayList(elements);
    }

    public static <E> ArrayList<E> newArrayList(Iterable<E> elements) {
        return Collects.newArrayList(elements);
    }

    public static <E> ArrayList<E> newArrayList(Iterator<E> elements) {
        return Collects.newArrayList(elements);
    }

    public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
        return Collects.newArrayListWithCapacity(initialArraySize);
    }

    /**
     * @since 5.4.6
     */
    public static <E> ArrayList<E> newArrayListWithFill(int initialArraySize, E initFillElement) {
        ArrayList<E> list = Collects.newArrayListWithCapacity(initialArraySize);
        for (int i = 0; i < initialArraySize; i++) {
            list.add(initFillElement);
        }
        return list;
    }

    public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
        return Collects.newArrayListWithExpectedSize(estimatedSize);
    }

    public static <E> LinkedList<E> newLinkedList() {
        return Collects.newLinkedList();
    }

    public static <E> LinkedList<E> newLinkedList(Iterable<E> elements) {
        return Collects.newLinkedList(elements);
    }

    /**
     * @since 5.4.6
     */
    public static <E> LinkedList<E> newLinkedListWithFill(int initialArraySize, E initFillElement) {
        LinkedList<E> list = Collects.newLinkedList();
        for (int i = 0; i < initialArraySize; i++) {
            list.add(initFillElement);
        }
        return list;
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return Collects.newCopyOnWriteArrayList();
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<E> elements) {
        return Collects.newCopyOnWriteArrayList(elements);
    }

    public static <E> List<E> asList(E... elements) {
        List<E> list = new ArrayList<E>();
        Collects.addAll(list, elements);
        return list;
    }

    public static <E> List<E> immutableList() {
        return Collects.immutableList();
    }

    /**
     * @since 5.2.4
     */
    public static <E> List<E> immutableList(Collection<E> elements) {
        return Collects.immutableList(elements);
    }

    /**
     * @since 5.2.4
     */
    public static <E> List<E> immutableList(E... elements) {
        return Collects.immutableList(elements);
    }
}
