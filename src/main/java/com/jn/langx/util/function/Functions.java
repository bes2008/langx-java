package com.jn.langx.util.function;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Objects;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;

import java.util.*;

public class Functions {
    /*******************************************
     *   Function, Mapper
     *******************************************/
    public static <E> Function<E, E> noopFunction(){
        return new Function<E, E>() {
            @Override
            public E apply(E input) {
                return input;
            }
        };
    }

    public static <E> Mapper<E, E> noopMapper(){
        return new Mapper<E, E>() {
            @Override
            public E apply(E input) {
                return input;
            }
        };
    }

    public static <E> Mapper<E, String> toStringFunction() {
        return new Mapper<E, String>() {
            @Override
            public String apply(E input) {
                Preconditions.checkNotNull(input);
                return input.toString();
            }
        };
    }

    public static Function2<Integer, Integer, Integer> maxIntegerFunction() {
        return new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) {
                return Maths.<Integer>max(a, b);
            }
        };
    }

    public static Function2<Float, Float, Float> maxFloatFunction() {
        return new Function2<Float, Float, Float>() {
            @Override
            public Float apply(Float a, Float b) {
                return Maths.<Float>max(a, b);
            }
        };
    }

    public static Function2<Long, Long, Long> maxLongFunction() {
        return new Function2<Long, Long, Long>() {
            @Override
            public Long apply(Long a, Long b) {
                return Maths.<Long>max(a, b);
            }
        };
    }

    public static Function2<Double, Double, Double> maxDoubleFunction() {
        return new Function2<Double, Double, Double>() {
            @Override
            public Double apply(Double a, Double b) {
                return Maths.<Double>max(a, b);
            }
        };
    }

    public static Function2<Integer, Integer, Integer> minIntegerFunction() {
        return new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) {
                return Maths.<Integer>min(a, b);
            }
        };
    }

    public static Function2<Float, Float, Float> minFloatFunction() {
        return new Function2<Float, Float, Float>() {
            @Override
            public Float apply(Float a, Float b) {
                return Maths.<Float>min(a, b);
            }
        };
    }

    public static Function2<Long, Long, Long> minLongFunction() {
        return new Function2<Long, Long, Long>() {
            @Override
            public Long apply(Long a, Long b) {
                return Maths.<Long>min(a, b);
            }
        };
    }

    public static Function2<Double, Double, Double> minDoubleFunction() {
        return new Function2<Double, Double, Double>() {
            @Override
            public Double apply(Double a, Double b) {
                return Maths.<Double>min(a, b);
            }
        };
    }

    public static Function2<Integer, Integer, Integer> sumIntegerFunction() {
        return new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) {
                return Maths.sum(a, b);
            }
        };
    }

    public static Function2<Float, Float, Float> sumFloatFunction() {
        return new Function2<Float, Float, Float>() {
            @Override
            public Float apply(Float a, Float b) {
                return Maths.sum(a, b);
            }
        };
    }

    public static Function2<Long, Long, Long> sumLongFunction() {
        return new Function2<Long, Long, Long>() {
            @Override
            public Long apply(Long a, Long b) {
                return Maths.sum(a, b);
            }
        };
    }

    public static Function2<Double, Double, Double> sumDoubleFunction() {
        return new Function2<Double, Double, Double>() {
            @Override
            public Double apply(Double a, Double b) {
                return Maths.sum(a, b);
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

    public static <E1, E2> Predicate2<E1, E2> deepEqualsPredicate() {
        return new Predicate2<E1, E2>() {
            @Override
            public boolean test(E1 v1, E2 v2) {
                return Objects.deepEquals(v1, v2);
            }
        };
    }

    public static <E1, E2> Predicate2<E1, E2> equalsPredicate() {
        return new Predicate2<E1, E2>() {
            @Override
            public boolean test(E1 v1, E2 v2) {
                return Objects.equals(v1, v2);
            }
        };
    }

    public static Predicate<String> stringContainsPredicate(final String cantained) {
        Preconditions.checkTrue(Emptys.isNotEmpty(cantained));
        return new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return value.contains(cantained);
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

    public static <T> Supplier<T, T> noopSupplier() {
        return new Supplier<T, T>() {
            @Override
            public T get(T input) {
                return input;
            }
        };
    }
}
