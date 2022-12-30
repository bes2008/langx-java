package com.jn.langx.util.collection;


import com.jn.langx.util.Maths;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class Lists {
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    public static <E> ArrayList<E> newArrayList(E... elements) {
        return Collects.newArrayList(elements);
    }

    public static <E> ArrayList<E> newArrayList(Iterable<E> elements) {
      return Collects.<E>newArrayList(elements);
    }

    public static <E> ArrayList<E> newArrayList(Iterator<E> elements) {
        return Collects.newArrayList(Collects.<E>asIterable(elements));
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

    public static <E> LinkedList<E> newLinkedList(Iterable<E> elements) {
       return Collects.newLinkedList(elements);
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<E>();
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<E> elements) {
        return new CopyOnWriteArrayList<E>(Collects.asList(elements));
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
