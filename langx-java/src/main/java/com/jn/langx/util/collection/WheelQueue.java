package com.jn.langx.util.collection;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class WheelQueue<E> implements Queue<E> {
    private final LinkedList<E> list;
    private final ReentrantLock lock;

    @SuppressWarnings("rawtypes")
    public WheelQueue() {
        this.list = new LinkedList();
        this.lock = new ReentrantLock(true);
    }

    @Override
    public boolean add(E e) {
        this.lock.lock();
        try {
            return this.list.add(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean offer(E e) {
        this.lock.lock();
        try {
            return this.list.add(e);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public E remove() {
        this.lock.lock();
        try {
            if (size() > 0) {
                return this.list.remove(0);
            }
            throw new NoSuchElementException();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public E poll() {
        this.lock.lock();
        try {
            return (size() > 0 ? this.list.remove(0) : null);
        } finally {
            this.lock.unlock();
        }
    }

    private E get(boolean move) {
        E e = size() > 0 ? this.list.get(0) : null;
        if ((move) && (null != e)) {
            remove();
            add(e);
        }
        return e;
    }

    public E get() {
        this.lock.lock();
        try {
            return get(true);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public E element() {
        this.lock.lock();
        try {
            E e = get(false);
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public E peek() {
        this.lock.lock();
        try {
            return get(false);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        this.lock.lock();
        try {
            return this.list.contains(o);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public int size() {
        this.lock.lock();
        try {
            return this.list.size();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Iterator<E> iterator() {
        this.lock.lock();
        try {
            return this.list.iterator();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public Object[] toArray() {
        this.lock.lock();
        try {
            return this.list.toArray();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        this.lock.lock();
        try {
            return this.list.toArray(a);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        this.lock.lock();
        try {
            Iterator<E> iter = iterator();
            while (iter.hasNext()) {
                E e = iter.next();
                if (e.equals(o)) {
                    iter.remove();
                    return true;
                }
            }
        } finally {
            this.lock.unlock();
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if ((c == null) || (c.isEmpty())) {
            return false;
        }
        this.lock.lock();
        try {
            for (Object e : c) {
                if (!contains(e)) {
                    return false;
                }
            }
        } finally {
            this.lock.unlock();
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        this.lock.lock();
        try {
            return this.list.addAll(c);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        this.lock.lock();
        try {
            return this.list.removeAll(c);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        this.lock.lock();
        try {
            return this.list.retainAll(c);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void clear() {
        this.lock.lock();
        try {
            this.list.clear();
        } finally {
            this.lock.unlock();
        }
    }
}