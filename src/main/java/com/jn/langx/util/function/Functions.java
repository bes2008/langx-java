package com.jn.langx.util.function;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;

import java.util.*;

public class Functions {
    /*******************************************
     *   Function, Mapper
     *******************************************/
    public static <E> Mapper<E, String> toStringFunction() {
        return new Mapper<E, String>() {
            @Override
            public String apply(E input) {
                Preconditions.checkNotNull(input);
                return input.toString();
            }
        };
    }

    /**********************************************
     *   Predicate
     **********************************************/
    public static <T> Predicate<T> nonNullPredicate() {
        return new Predicate<T>() {
            @Override
            public boolean test(T value) {
                return value != null;
            }
        };
    }


    public static <T> Predicate<T> nullPredicate() {
        return new Predicate<T>() {
            @Override
            public boolean test(T value) {
                return value == null;
            }
        };
    }


    /**********************************************
     * Supplier
     **********************************************/
    public static <K, V> Supplier<K, List<V>> emptyArrayListSupplier() {
        return new Supplier<K, List<V>>() {
            @Override
            public List<V> get(K input) {
                return Collects.emptyArrayList();
            }
        };
    }

    public static <K, V> Supplier<K, List<V>> emptyLinkedListSupplier() {
        return new Supplier<K, List<V>>() {
            @Override
            public List<V> get(K input) {
                return Collects.emptyLinkedList();
            }
        };
    }

    public static <E> Supplier0<HashSet<E>> emptyHashSetSupplier0() {
        return new Supplier0<HashSet<E>>() {
            @Override
            public HashSet<E> get() {
                return Collects.emptyHashSet();
            }
        };
    }

    public static <I, E> Supplier<I, HashSet<E>> emptyHashSetSupplier() {
        return new Supplier<I, HashSet<E>>() {
            @Override
            public HashSet<E> get(I input) {
                return Collects.emptyHashSet();
            }
        };
    }

    public static <I, E> Supplier<I, LinkedHashSet<E>> emptyLinkedHashSetSupplier() {
        return new Supplier<I, LinkedHashSet<E>>() {
            @Override
            public LinkedHashSet<E> get(I input) {
                return (LinkedHashSet<E>) Collects.emptyHashSet(true);
            }
        };
    }

    public static <E> Supplier0<TreeSet<E>> emptyTreeSetSupplier0(final Comparator<E> comparator) {
        return new Supplier0<TreeSet<E>>() {
            @Override
            public TreeSet<E> get() {
                return Collects.emptyTreeSet(comparator);
            }
        };
    }

    public static <I, V> Supplier<I, Set<V>> emptyTreeSetSupplier(final Comparator<V> comparator) {
        return new Supplier<I, Set<V>>() {
            @Override
            public Set<V> get(Object input) {
                return Collects.emptyTreeSet(comparator);
            }
        };
    }
}
