package com.jn.langx.util.collection.forwarding;

import com.jn.langx.Delegatable;

import java.util.Collection;
import java.util.Iterator;

public abstract class ForwardingCollection<E,C extends Collection<E>> implements Collection<E>, Delegatable<C> {
    private C delegate;
    @Override
    public void setDelegate(C delegate) {
        this.delegate = delegate;
    }

    @Override
    public C getDelegate() {
        return this.delegate;
    }

    @Override
    public int size() {
        return getDelegate().size();
    }

    @Override
    public boolean isEmpty() {
        return getDelegate().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return getDelegate().contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return getDelegate().iterator();
    }

    @Override
    public Object[] toArray() {
        return getDelegate().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return getDelegate().toArray(a);
    }

    @Override
    public boolean add(E e) {
        return getDelegate().add(e);
    }

    @Override
    public boolean remove(Object o) {
        return getDelegate().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return getDelegate().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return getDelegate().addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return getDelegate().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return getDelegate().retainAll(c);
    }

    @Override
    public void clear() {
        getDelegate().clear();
    }
}
