/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jn.langx.util.collection.list;

import com.jn.langx.util.collection.BoundedCollection;
import com.jn.langx.util.collection.forwarding.ForwardingList;
import com.jn.langx.util.collection.iter.AbstractListIteratorDecorator;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Decorates another <code>List</code> to fix the size preventing add/remove.
 * <p>
 * The add, remove, clear and retain operations are unsupported.
 * The set method is allowed (as it doesn't change the list size).
 * </p>
 * <p>
 * NOTE:
 * Modifying the decorated list directly would results in influencing the outcome
 * of method calls on this object. For example, the bounds of this list would reflect
 * a newly added object to the underlying list.
 * </p>
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 * </p>
 *
 * @param <E> the type of elements in this collection
 * @since 4.7.6
 */
public class FixedSizeList<E> extends ForwardingList<E> implements BoundedCollection<E> {

    /**
     * @since 5.4.6
     */
    private boolean addable = false;

    /**
     * Serialization version
     */
    private static final long serialVersionUID = -2218010673611160319L;

    /**
     * Factory method to create a fixed size list.
     *
     * @param <E>  the type of the elements in the list
     * @param list the list to decorate, must not be null
     * @return a new fixed size list
     * @throws NullPointerException if list is null
     */
    public static <E> FixedSizeList<E> fixedSizeList(final List<E> list) {
        return fixedSizeList(list, false);
    }

    /**
     * @since 5.4.6
     */
    public static <E> FixedSizeList<E> fixedSizeList(final List<E> list, boolean addable) {
        return new FixedSizeList<E>(list, addable);
    }

    //-----------------------------------------------------------------------

    /**
     * Constructor that wraps (not copies).
     *
     * @param list the list to decorate, must not be null
     * @throws NullPointerException if list is null
     */
    protected FixedSizeList(final List<E> list, boolean addable) {
        setDelegate(list);
        this.addable = addable;
    }

    //-----------------------------------------------------------------------
    @Override
    public boolean add(final E object) {
        if(!addable){
            throw unsupportedOperationException();
        }
        if(isEmpty()){
            return false;
        }
        getDelegate().remove(0);
        getDelegate().add(object);
        return true;
    }

    @Override
    public void add(final int index, final E object) {
        if(!addable){
            throw unsupportedOperationException();
        }
        if(isEmpty()){
            return;
        }
        getDelegate().add(index, object);
        getDelegate().remove(size()-1);
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        if(isEmpty()){
            return false;
        }
        for(E o : coll){
            add(o);
        }
        return true;
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> coll) {
        if(isEmpty()){
            return false;
        }
        for(E o : coll){
            add(index, o);
        }
        return true;
    }

    @Override
    public void clear() {
        throw unsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        return new FixedSizeListIterator(getDelegate().listIterator(index));
    }

    @Override
    public E remove(final int index) {
        throw unsupportedOperationException();
    }

    @Override
    public boolean remove(final Object object) {
        throw unsupportedOperationException();
    }


    @Override
    public boolean removeAll(final Collection<?> coll) {
        throw unsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        throw unsupportedOperationException();
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        final List<E> sub = getDelegate().subList(fromIndex, toIndex);
        return new FixedSizeList<E>(sub, this.addable);
    }

    /**
     * List iterator that only permits changes via set()
     */
    private class FixedSizeListIterator extends AbstractListIteratorDecorator<E> {
        protected FixedSizeListIterator(final ListIterator<E> iterator) {
            super(iterator);
        }

        @Override
        public void remove() {
            throw unsupportedOperationException();
        }

        @Override
        public void add(final Object object) {
            throw unsupportedOperationException();
        }
    }

    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    public int maxSize() {
        return size();
    }

    private static UnsupportedOperationException unsupportedOperationException() {
        return new UnsupportedOperationException("List is fixed size");
    }

}
