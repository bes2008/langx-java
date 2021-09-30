package com.jn.langx.util.collection;

import com.jn.langx.util.*;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.struct.Holder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author jinuo.fang
 */
public class Tuple extends BasedStringAccessor<Integer, Collection> implements Iterable, Serializable {
    private static final long serialVersionUID = 1L;
    private List<Object> elements = Collects.emptyArrayList();
    private int maxSize = 0;

    public Tuple(Object value0, Object... values) {
        setTarget(Pipeline.of(new Object[]{value0}).addAll(Collects.asList(values)).getAll());
    }

    public Tuple(Collection<Object> values) {
        setTarget(values);
    }

    public Tuple(int expectedLength, Collection<Object> values) {
        setTarget0(expectedLength, values);
    }

    public static Tuple of(Object... objects) {
        if (objects == null) {
            return new Tuple(Collects.emptyArrayList());
        }
        return new Tuple(Collects.asList(objects));
    }

    private void setTarget0(int expectedLength, Collection values) {
        Preconditions.checkTrue(expectedLength >= 0);
        Preconditions.checkNotNull(values);
        this.maxSize = Maths.max(values.size(), expectedLength);
        this.elements.clear();
        List<Object> list = Collects.limit(values, this.maxSize);
        while (list.size() < maxSize) {
            list.add(null);
        }
        this.elements.addAll(list);
    }

    @Override
    public List getTarget() {
        return this.elements;
    }

    @Override
    public void setTarget(Collection target) {
        Preconditions.checkNotNull(target);
        setTarget0(target.size(), target);
    }

    @Override
    public boolean has(Integer index) {
        return index >= 0 && index < this.elements.size();
    }

    public boolean contains(Object element) {
        return this.elements.contains(element);
    }

    @Override
    public Object get(Integer index) {
        return this.elements.get(index);
    }

    @Override
    public String getString(Integer index, String defaultValue) {
        Object o = get(index);
        return o == null ? defaultValue : o.toString();
    }

    @Override
    public void remove(Integer index) {
        set(index, null);
    }

    @Override
    public void set(Integer index, Object value) {
        Preconditions.checkTrue(index < maxSize && index >= 0);
        this.elements.set(index, value);
    }

    @Override
    public Iterator iterator() {
        return elements.iterator();
    }

    public int capacity() {
        return maxSize;
    }

    public int size() {
        return elements.size();
    }

    @Override
    public int hashCode() {
        final Holder<Integer> hashcode = new Holder<Integer>(size() << 4);
        Collects.forEach(this, new Consumer<Object>() {
            @Override
            public void accept(Object element) {
                hashcode.set(hashcode.get() + element.hashCode());
            }
        });
        return hashcode.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Tuple)) {
            return false;
        }
        Tuple t = (Tuple) obj;
        if (this.size() != t.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            Object element = this.get(i);
            Object element2 = t.get(i);
            if (!element.equals(element2)) {
                return false;
            }
        }
        return true;
    }

    private boolean equals(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    public Object[] toArray() {
        return elements.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return elements.toArray(a);
    }

    @Override
    public String toString() {
        return Strings.join(", ","(",")",false,this.elements.iterator());
    }
}
