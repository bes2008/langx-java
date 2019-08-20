package com.jn.langx.util.collection;

import com.jn.langx.util.BasedStringAccessor;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Preconditions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * @author jinuo.fang
 */
public class Tuple extends BasedStringAccessor<Integer, List> implements Iterable, Serializable {
    private static final long serialVersionUID = 1L;
    private List<Object> elements = Collects.emptyArrayList();
    private int maxSize = 0;

    public Tuple(List<Object> values) {
        setTarget(values);
    }

    public Tuple(int length, List<Object> values) {
        setTarget0(length, values);
    }

    @Override
    public void setTarget(List target) {
        Preconditions.checkNotNull(target);
        setTarget0(target.size(), target);
    }

    private void setTarget0(int length, List values) {
        Preconditions.checkArgument(length >= 0);
        Preconditions.checkNotNull(values);
        this.maxSize = Maths.min(values.size(), length);
        this.elements.addAll(Collects.limit(values, this.maxSize));
    }

    @Override
    public List getTarget() {
        return this.elements;
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
    public void set(Integer index, Object value) {
        Preconditions.checkArgument(index < maxSize && index >= 0);
        this.elements.set(index, value);
    }

    @Override
    public Iterator iterator() {
        return elements.iterator();
    }

    public static Tuple of(Object... objects) {
        if (objects == null) {
            return new Tuple(Collects.emptyArrayList());
        }
        return new Tuple(Collects.asList(objects));
    }
}
