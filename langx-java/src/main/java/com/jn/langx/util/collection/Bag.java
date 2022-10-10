package com.jn.langx.util.collection;


import com.jn.langx.util.collection.iter.ReverseListIterator;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 只能放（add），不能取（get），不能删（remote），可遍历，可统计
 * <p>
 * 通常用于统计等
 * <p>
 * Collection which does not allow removing elements (only collect and iterate)
 *
 * @param <E> - element in this bag
 */
public class Bag<E> implements Iterable<E> {

    private ArrayList<E> elements;


    /**
     * Create an empty bag
     */
    public Bag() {
        elements = new ArrayList<E>();
    }

    /**
     * @return true if this bag is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    /**
     * @return the number of elements
     */
    public int size() {
        return this.elements.size();
    }

    /**
     * @param element - the element to add
     */
    public void add(E element) {
        this.elements.add(element);
    }

    /**
     * Checks if the bag contains a specific element
     *
     * @param element which you want to look for
     * @return true if bag contains element, otherwise false
     */
    public boolean contains(E element) {
        return this.elements.contains(element);
    }

    /**
     * @return an iterator that iterates over the elements in this bag in
     * arbitrary order
     */
    public Iterator<E> iterator() {
        return new ReverseListIterator<E>(this.elements);
    }


}