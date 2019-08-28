package com.jn.langx.util.function;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;

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
    public static <E> Predicate<E> nonNullPredicate() {
        return new Predicate<E>() {
            @Override
            public boolean test(E value) {
                return value != null;
            }
        };
    }


    public static <E> Predicate<E> nullPredicate() {
        return new Predicate<E>() {
            @Override
            public boolean test(E value) {
                return value == null;
            }
        };
    }

    public static <E> Predicate<E> allPredicate(@NonNull Predicate<E>... predicates) {
        Preconditions.checkTrue(Emptys.isNotEmpty(predicates));
        Preconditions.checkTrue(predicates.length >= 1);
        final Pipeline<Predicate<E>> pipeline = Pipeline.<Predicate<E>>of(predicates);
        return new Predicate<E>() {
            @Override
            public boolean test(final E value) {
                return pipeline.allMatch(new Predicate<Predicate<E>>() {
                    @Override
                    public boolean test(Predicate<E> filter) {
                        return filter.test(value);
                    }
                });
            }
        };
    }

    public static <E> Predicate<E> anyPredicate(@NonNull Predicate<E>... predicates) {
        Preconditions.checkTrue(Emptys.isNotEmpty(predicates));
        Preconditions.checkTrue(predicates.length >= 1);
        final Pipeline<Predicate<E>> pipeline = Pipeline.<Predicate<E>>of(predicates);
        return new Predicate<E>() {
            @Override
            public boolean test(final E value) {
                return pipeline.anyMatch(new Predicate<Predicate<E>>() {
                    @Override
                    public boolean test(Predicate<E> filter) {
                        return filter.test(value);
                    }
                });
            }
        };
    }

    public static <E> Predicate<E> andPredicate(@NonNull Predicate<E>... predicates) {
        Preconditions.checkTrue(Emptys.isNotEmpty(predicates));
        Preconditions.checkTrue(predicates.length >= 2);
        final Pipeline<Predicate<E>> pipeline = Pipeline.<Predicate<E>>of(predicates);
        return new Predicate<E>() {
            @Override
            public boolean test(final E value) {
                return pipeline.allMatch(new Predicate<Predicate<E>>() {
                    @Override
                    public boolean test(Predicate<E> filter) {
                        return filter.test(value);
                    }
                });
            }
        };
    }

    public static <E> Predicate<E> orPredicate(@NonNull Predicate<E>... predicates) {
        Preconditions.checkTrue(Emptys.isNotEmpty(predicates));
        Preconditions.checkTrue(predicates.length >= 2);
        final Pipeline<Predicate<E>> pipeline = Pipeline.<Predicate<E>>of(predicates);
        return new Predicate<E>() {
            @Override
            public boolean test(final E value) {
                return pipeline.anyMatch(new Predicate<Predicate<E>>() {
                    @Override
                    public boolean test(Predicate<E> filter) {
                        return filter.test(value);
                    }
                });
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
