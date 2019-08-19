package com.jn.langx.util.reflect;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.BasedStringAccessor;
import com.jn.langx.util.Preconditions;

import java.lang.reflect.Array;

/**
 * An array accessor
 *
 * @param <E> an array
 * @author jinuo.fang
 */
public class ArrayAccessor<E> extends BasedStringAccessor<Integer, E> {

    public ArrayAccessor() {
    }

    public ArrayAccessor(E target) {
        setTarget(target);
    }

    @Override
    public void setTarget(E target) {
        Preconditions.checkNotNull(target);
        Preconditions.checkArgument(Arrs.isArray(target));
        super.setTarget(target);
    }


    @Override
    public Object get(Integer index) {
        return Array.get(getTarget(), index);
    }

    @Override
    public String getString(Integer index, String defaultValue) {
        Object o = get(index);
        return o == null ? defaultValue : o.toString();
    }

    @Override
    public void set(Integer index, Object value) {
        Array.set(getTarget(), index, value);
    }
}
