package com.jn.langx.util.collection;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class Lists {
    private Lists(){}
    public static <E> ArrayList<E> newArrayList() {
        return Collects.newArrayList();
    }

    public static <E> ArrayList<E> newArrayList(E... elements) {
        return Collects.newArrayList(elements);
    }

    public static <E> ArrayList<E> newArrayList(Iterable<E> elements) {
      return Collects.<E>newArrayList(elements);
    }

    public static <E> ArrayList<E> newArrayList(Iterator<E> elements) {
        return Collects.newArrayList(elements);
    }

    public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
        return Collects.newArrayListWithCapacity(initialArraySize);
    }

    public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
        return Collects.newArrayListWithExpectedSize(estimatedSize);
    }

    public static <E> LinkedList<E> newLinkedList() {
        return Collects.<E>newLinkedList();
    }

    public static <E> LinkedList<E> newLinkedList(Iterable<E> elements) {
       return Collects.newLinkedList(elements);
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return Collects.newCopyOnWriteArrayList();
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<E> elements) {
        return Collects.newCopyOnWriteArrayList(elements);
    }

    public static <E> List<E> asList( E first, E[] rest) {
        List<E> list = new ArrayList<E>();
        list.add(first);
        Collects.addAll(list, rest);
        return list;
    }

    public static <E> List<E> asList(E first,  E second, E[] rest) {
        List<E> list = new ArrayList<E>();
        list.add(first);
        list.add(second);
        Collects.addAll(list, rest);
        return list;
    }

}
