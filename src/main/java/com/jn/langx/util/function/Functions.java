package com.jn.langx.util.function;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Functions {
    public static Mapper<Object, String> toStringFunction = new Mapper<Object, String>() {
        @Override
        public String apply(Object input) {
            Preconditions.checkNotNull(input);
            return input.toString();
        }
    };

    public static Predicate<Object> nonNullPredicate = new Predicate<Object>() {
        @Override
        public boolean test(Object value) {
            return value != null;
        }
    };

    public static Supplier emptyArrayListSupplier = new Supplier() {
        @Override
        public Object get(Object input) {
            return Collects.emptyArrayList();
        }
    };
    public static Supplier emptyLinkedListSupplier = new Supplier() {
        @Override
        public Object get(Object input) {
            return Collects.emptyLinkedList();
        }
    };

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

    public static Supplier emptyLinkedHashSetSupplier = new Supplier() {
        @Override
        public Object get(Object input) {
            return Collects.emptyHashSet(true);
        }
    };

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
