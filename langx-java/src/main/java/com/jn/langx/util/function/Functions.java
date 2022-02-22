package com.jn.langx.util.function;

import com.jn.langx.Filter;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.*;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.predicate.EmptyPredicate;

import java.util.*;

public class Functions {
    private Functions(){}
    /*******************************************
     *   Function, Mapper
     *******************************************/
    public static <E> Function<E, E> noopFunction() {
        return new Function<E, E>() {
            @Override
            public E apply(E input) {
                return input;
            }
        };
    }

    public static <E> Mapper<E, E> noopMapper() {
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
                return Maths.max(a, b);
            }
        };
    }

    public static Function2<Float, Float, Float> maxFloatFunction() {
        return new Function2<Float, Float, Float>() {
            @Override
            public Float apply(Float a, Float b) {
                return Maths.maxFloat(a, b);
            }
        };
    }

    public static Function2<Long, Long, Long> maxLongFunction() {
        return new Function2<Long, Long, Long>() {
            @Override
            public Long apply(Long a, Long b) {
                return Maths.maxLong(a, b);
            }
        };
    }

    public static Function2<Double, Double, Double> maxDoubleFunction() {
        return new Function2<Double, Double, Double>() {
            @Override
            public Double apply(Double a, Double b) {
                return Maths.maxDouble(a, b);
            }
        };
    }

    public static Function2<Integer, Integer, Integer> minIntegerFunction() {
        return new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) {
                return Maths.min(a, b);
            }
        };
    }

    public static Function2<Float, Float, Float> minFloatFunction() {
        return new Function2<Float, Float, Float>() {
            @Override
            public Float apply(Float a, Float b) {
                return Maths.minFloat(a, b);
            }
        };
    }

    public static Function2<Long, Long, Long> minLongFunction() {
        return new Function2<Long, Long, Long>() {
            @Override
            public Long apply(Long a, Long b) {
                return Maths.minLong(a, b);
            }
        };
    }

    public static Function2<Double, Double, Double> minDoubleFunction() {
        return new Function2<Double, Double, Double>() {
            @Override
            public Double apply(Double a, Double b) {
                return Maths.minDouble(a, b);
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
                return Maths.sumFloat(a, b);
            }
        };
    }

    public static Function2<Long, Long, Long> sumLongFunction() {
        return new Function2<Long, Long, Long>() {
            @Override
            public Long apply(Long a, Long b) {
                return Maths.sumLong(a, b);
            }
        };
    }

    public static Function2<Double, Double, Double> sumDoubleFunction() {
        return new Function2<Double, Double, Double>() {
            @Override
            public Double apply(Double a, Double b) {
                return Maths.sumDouble(a, b);
            }
        };
    }


    public static Function<String, String> toLowerCase() {
        return new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input.toLowerCase();
            }
        };
    }

    public static Function<String, String> toUpperCase() {
        return new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input.toUpperCase();
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

    public static <E1, E2> Predicate2<E1, E2> nonNullPredicate2() {
        return new Predicate2<E1, E2>() {
            @Override
            public boolean test(E1 e1, E2 value) {
                return value != null;
            }
        };
    }


    public static <E1, E2> Predicate2<E1, E2> nullPredicate2() {
        return new Predicate2<E1, E2>() {
            @Override
            public boolean test(E1 e1, E2 value) {
                return value == null;
            }
        };
    }

    public static <E> Predicate<E> emptyPredicate() {
        return EmptyPredicate.IS_EMPTY_PREDICATE;
    }

    public static <E> Predicate<E> notEmptyPredicate() {
        return EmptyPredicate.IS_NOT_EMPTY_PREDICATE;
    }
    public static <E> Filter<E> trueFilter() {
        return new Filter<E>() {
            @Override
            public boolean accept(E e) {
                return true;
            }
        };
    }

    public static <E> Filter<E> falseFilter() {
        return new Filter<E>() {
            @Override
            public boolean accept(E e) {
                return false;
            }
        };
    }

    public static <E> Predicate<E> truePredicate() {
        return booleanPredicate(true);
    }

    public static <E> Predicate<E> falsePredicate() {
        return booleanPredicate(false);
    }

    public static <E> Predicate<E> booleanPredicate(final boolean value) {
        return new Predicate<E>() {
            @Override
            public boolean test(E element) {
                return value;
            }
        };
    }

    public static <E1, E2> Predicate2<E1, E2> truePredicate2() {
        return booleanPredicate2(true);
    }

    public static <E1, E2> Predicate2<E1, E2> falsePredicate2() {
        return booleanPredicate2(false);
    }

    public static <E1, E2> Predicate2<E1, E2> booleanPredicate2(final boolean value) {
        return new Predicate2<E1, E2>() {
            @Override
            public boolean test(E1 e1, E2 e2) {
                return value;
            }
        };
    }

    public static <E> Predicate<E> allPredicate(List<Predicate<E>> predicates) {
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

    public static <E> Predicate<E> allPredicate(@NonNull Predicate<E>... predicates) {
        Preconditions.checkTrue(Emptys.isNotEmpty(predicates));
        Preconditions.checkTrue(predicates.length >= 1);
        return allPredicate(Collects.asList(predicates));
    }

    public static <E> Predicate<E> anyPredicate(List<Predicate<E>> predicates) {
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

    public static <E> Predicate<E> anyPredicate(@NonNull Predicate<E>... predicates) {
        Preconditions.checkTrue(Emptys.isNotEmpty(predicates));
        Preconditions.checkTrue(predicates.length >= 1);
        return anyPredicate(Collects.asList(predicates));
    }

    public static <E> Predicate<E> nonePredicate(List<Predicate<E>> predicates) {
        final Pipeline<Predicate<E>> pipeline = Pipeline.<Predicate<E>>of(predicates);
        return new Predicate<E>() {
            @Override
            public boolean test(final E value) {
                return pipeline.noneMatch(new Predicate<Predicate<E>>() {
                    @Override
                    public boolean test(Predicate<E> filter) {
                        return filter.test(value);
                    }
                });
            }
        };
    }

    public static <E> Predicate<E> nonePredicate(Predicate<E>... predicates) {
        Preconditions.checkTrue(Emptys.isNotEmpty(predicates));
        Preconditions.checkTrue(predicates.length >= 1);
        return nonePredicate(Collects.asList(predicates));
    }

    public static <E> Predicate<E> andPredicate(@NonNull Predicate<E>... predicates) {
        return allPredicate(predicates);
    }

    public static <E> Predicate<E> orPredicate(@NonNull Predicate<E>... predicates) {
        return anyPredicate(predicates);
    }

    public static <E1, E2> Predicate2<E1, E2> deepEqualsPredicate() {
        return new Predicate2<E1, E2>() {
            @Override
            public boolean test(E1 v1, E2 v2) {
                return Objs.deepEquals(v1, v2);
            }
        };
    }

    public static <E> Predicate<E> equalsPredicate(final E obj) {
        return new Predicate<E>() {
            @Override
            public boolean test(E value) {
                return Objs.equals(obj, value);
            }
        };
    }

    public static <E1, E2> Predicate2<E1, E2> equalsPredicate() {
        return new Predicate2<E1, E2>() {
            @Override
            public boolean test(E1 v1, E2 v2) {
                return Objs.equals(v1, v2);
            }
        };
    }

    public static Predicate<String> stringContainsPredicate(final String cantained) {
        return stringContainsPredicate(cantained, false);
    }

    public static Predicate<String> stringContainsPredicate(final String cantained, final boolean ignoreCase) {
        Preconditions.checkTrue(Emptys.isNotEmpty(cantained));
        return new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return Strings.contains(value, cantained, ignoreCase);
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

    /******************************************
     *  noop consumer
     ******************************************/
    public static <T> Consumer<T> noopConsumer() {
        return new Consumer<T>() {
            @Override
            public void accept(T t) {
                // NOOP
            }
        };
    }
}
