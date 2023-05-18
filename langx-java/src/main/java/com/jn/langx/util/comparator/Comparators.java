package com.jn.langx.util.comparator;


import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Pipeline;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Comparators {
    public static final Comparator<String> STRING_COMPARATOR= new StringComparator();
    public static final Comparator<String> STRING_COMPARATOR_IGNORE_CASE = new StringComparator(true);
    public static final Comparator<Character> CHAR_COMPARATOR = new CharComparator();

    private Comparators(){

    }
    /**
     * Returns a new {@code Comparator} which is the result of chaining the
     * given {@code Comparator}s.  If the first {@code Comparator}
     * considers two objects unequal, its result is returned; otherwise, the
     * result of the second {@code Comparator} is returned.  Facilitates
     * sorting on primary and secondary keys.
     */
    public static <T> Comparator<T> chain(final Comparator<T> c1,
                                          final Comparator<T> c2) {
        return new Comparator<T>(){
            @Override
            public int compare(T o1, T o2) {
                int x = c1.compare(o1, o2);
                return (x == 0 ? c2.compare(o1, o2) : x);
            }
        };
    }

    /**
     * Returns a new {@code Comparator} which is the result of chaining the
     * given {@code Comparator}s.  Facilitates sorting on multiple keys.
     */
    public static <T> Comparator<T> chain(final List<Comparator<T>> c) {
        return new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                int x = 0;
                Iterator<Comparator<T>> it = c.iterator();
                while (x == 0 && it.hasNext()) {
                    x = it.next().compare(o1, o2);
                }
                return x;
            }
        };
    }

    public static <T> Comparator<T> chain(Comparator<T>... c) {
        return chain(Pipeline.of(c).asList());
    }

    /**
     * Returns a new {@code Comparator} which is the reverse of the
     * given {@code Comparator}.
     */
    public static <T> Comparator<T> reverse(final Comparator<T> c) {
        return new ReverseComparator<T>(c);
    }

    public static <T extends Comparable<? super T>> Comparator<T> nullSafeNaturalComparator() {
        return new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return nullSafeCompare(o1,o2);
            }
        };
    }

    /**
     * Returns a consistent ordering over two elements even if one of them is null
     * (as long as compareTo() is stable, of course).
     *
     * There's a "trickier" solution with xor at http://stackoverflow.com/a/481836
     * but the straightforward answer seems better.
     */
    public static <T extends Comparable<? super T>> int nullSafeCompare(final T one, final T two) {
        if (one == null) {
            if (two == null) {
                return 0;
            }
            return -1;
        } else {
            if (two == null) {
                return 1;
            }
            return one.compareTo(two);
        }
    }

    public static <C extends Comparable> Comparator<List<C>> getListComparator() {
        return new Comparator<List<C>>() {
            @Override
            public int compare(List<C> o1, List<C> o2) {
                return compareLists(o1,o2);
            }
        };
    }

    /**
     * A {@code Comparator} that compares objects by comparing their
     * {@code String} representations, as determined by invoking
     * {@code toString()} on the objects in question.
     */
    public static Comparator getStringRepresentationComparator() {
        return new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return new StringComparator().compare(Objs.toString(o1),Objs.toString(o2));
            }
        };
    }

    public static Comparator<boolean[]> getBooleanArrayComparator() {
        return new Comparator<boolean[]>() {
            @Override
            public int compare(boolean[] o1, boolean[] o2) {
                return compareBooleanArrays(o1,o2);
            }
        };
    }

    public static <C extends Comparable> Comparator<C[]> getArrayComparator() {
        return new Comparator<C[]>() {
            @Override
            public int compare(C[] o1, C[] o2) {
                return compareArrays(o1,o2);
            }
        };
    }

    public static <T extends Comparable<T>> int compareArrays(T[] first, T[] second) {
        List<T> firstAsList = Arrays.asList(first);
        List<T> secondAsList = Arrays.asList(second);
        return compareLists(firstAsList, secondAsList);
    }


    /**
     * Provides a consistent ordering over lists. First compares by the first
     * element. If that element is equal, the next element is considered, and so
     * on.
     */
    public static <T extends Comparable<T>> int compareLists(List<T> list1, List<T> list2) {
        if (list1 == null && list2 == null)
            return 0;
        if (list1 == null || list2 == null) {
            throw new IllegalArgumentException();
        }
        int size1 = list1.size();
        int size2 = list2.size();
        int size = Math.min(size1, size2);
        for (int i = 0; i < size; i++) {
            int c = list1.get(i).compareTo(list2.get(i));
            if (c != 0)
                return c;
        }
        if (size1 < size2)
            return -1;
        if (size1 > size2)
            return 1;
        return 0;
    }

    public static int compareBooleanArrays(boolean[] a1, boolean[] a2) {
        int len = Math.min(a1.length, a2.length);
        for (int i = 0; i < len; i++) {
            if (!a1[i] && a2[i]) return -1;
            if (a1[i] && !a2[i]) return 1;
        }
        // one is a prefix of the other, or they're identical
        if (a1.length < a2.length) return -1;
        if (a1.length > a2.length) return 1;
        return 0;
    }
}
