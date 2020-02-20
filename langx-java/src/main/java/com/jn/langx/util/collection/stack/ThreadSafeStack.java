package com.jn.langx.util.collection.stack;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

public class ThreadSafeStack<E> implements Stack<E> {
    private LinkedBlockingDeque<E> deque;

    public ThreadSafeStack(){
        this.deque = new LinkedBlockingDeque<E>();
    }

    public ThreadSafeStack(Collection<E> c){
        this();
        if(Emptys.isNotEmpty(c)){
            this.deque.addAll(c);
        }
    }

    @Override
    public E push(E item) {
        deque.addLast(item);
        return item;
    }

    @Override
    public E pop() {
        return deque.pop();
    }

    @Override
    public E peek() {
        return deque.peek();
    }

    @Override
    public int search(Object o) {
        return Collects.firstOccurrence(this, (E) o);
    }

    @Override
    public int size() {
        return deque.size();
    }

    @Override
    public boolean isEmpty() {
        return deque.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return deque.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return deque.iterator();
    }

    @Override
    public Object[] toArray() {
        return deque.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return deque.toArray(a);
    }

    @Override
    public boolean add(E e) {
        push(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return deque.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return deque.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return deque.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return deque.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return deque.retainAll(c);
    }

    @Override
    public void clear() {
        deque.clear();
    }
}
