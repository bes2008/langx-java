package com.jn.langx.util.comparator;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.Reflects;

public class Compares {
    private Compares(){

    }
    /**
     * grater than
     */
    public static <E> boolean gt(E e1, E e2) {
        if (!Reflects.isSubClassOrEquals(e1.getClass(), e2.getClass()) && !Reflects.isSubClassOrEquals(e2.getClass(), e1.getClass())) {
            throw new UnsupportedOperationException(StringTemplates.formatWithPlaceholder("unsupported operation > between {} and  {}", e1.getClass(), e2.getClass()));
        }
        if (e1 instanceof Comparable && e2 instanceof Comparable) {
            return ((Comparable) e1).compareTo(e2) > 0;
        }
        throw new UnsupportedOperationException(StringTemplates.formatWithPlaceholder("unsupported operation > between {} and  {}", e1.getClass(), e2.getClass()));
    }

    /**
     * letter than
     */
    public static <E> boolean lt(E e1, E e2) {
        if (!Reflects.isSubClassOrEquals(e1.getClass(), e2.getClass()) && !Reflects.isSubClassOrEquals(e2.getClass(), e1.getClass())) {
            throw new UnsupportedOperationException(StringTemplates.formatWithPlaceholder("unsupported operation > between {} and  {}", e1.getClass(), e2.getClass()));
        }
        if (e1 instanceof Comparable && e2 instanceof Comparable) {
            return ((Comparable) e1).compareTo(e2) < 0;
        }
        throw new UnsupportedOperationException(StringTemplates.formatWithPlaceholder("unsupported operation > between {} and  {}", e1.getClass(), e2.getClass()));
    }

    /**
     * equals
     */
    public static <E> boolean eq(E e1, E e2) {
        return e1.equals(e2);
    }

    /**
     * not equals
     */
    public static <E> boolean ne(E e1, E e2) {
        return !e1.equals(e2);
    }

    /**
     * grater or equals
     */
    public static <E> boolean ge(E e1, E e2) {
        if (e1.equals(e2)) {
            return true;
        }
        if (!Reflects.isSubClassOrEquals(e1.getClass(), e2.getClass()) && !Reflects.isSubClassOrEquals(e2.getClass(), e1.getClass())) {
            return false;
        }
        if (e1 instanceof Comparable && e2 instanceof Comparable) {
            return ((Comparable) e1).compareTo(e2) >= 0;
        }
        return false;
    }

    /**
     * letter or equals
     */
    public static <E> boolean le(E e1, E e2) {
        if (e1.equals(e2)) {
            return true;
        }
        if (!Reflects.isSubClassOrEquals(e1.getClass(), e2.getClass()) && !Reflects.isSubClassOrEquals(e2.getClass(), e1.getClass())) {
            return false;
        }
        if (e1 instanceof Comparable && e2 instanceof Comparable) {
            return ((Comparable) e1).compareTo(e2) <= 0;
        }
        return false;
    }

    /**
     * in
     */
    public static <E> boolean in(E e1, Object collection) {
        return Collects.asCollection(Collects.asIterable(collection)).contains(e1);
    }

    /**
     * not in
     */
    public static <E> boolean ni(E e1, Object collection) {
        return !Collects.asCollection(Collects.asIterable(collection)).contains(e1);
    }
}
