package com.jn.langx.util.collection.iter;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Arrs;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class Iterables {
    public static boolean isIterable(Object obj) {
        if (Objs.isNull(obj)) {
            return false;
        }
        if (Arrs.isArray(obj)) {
            return true;
        }
        if (obj instanceof Iterable) {
            return true;
        }
        if (obj instanceof Map) {
            return true;
        }
        if (obj instanceof Iterator) {
            return true;
        }
        return obj instanceof Enumeration;
    }

    public static <E> NullIterator<E> nullIterator() {
        return NullIterator.INSTANCE;
    }

    public static <E> Iterator<E> getIterator(Iterable<E> iterable) {
        if (iterable != null) {
            return iterable.iterator();
        }
        return nullIterator();
    }

    private Iterables(){

    }
}
