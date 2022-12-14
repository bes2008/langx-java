package com.jn.langx.util;


import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;

import java.io.Serializable;
import java.util.Iterator;

/**
 * A strategy for determining whether two instances are considered equivalent. Examples of
 * equivalences are the {@linkplain #identity() identity equivalence} and {@linkplain #equals equals
 * equivalence}.
 */
public abstract class Equivalence<T> {
    /**
     * Constructor for use by subclasses.
     */
    protected Equivalence() {
    }

    /**
     * Returns {@code true} if the given objects are considered equivalent.
     * <p>
     * <p>The {@code equivalent} method implements an equivalence relation on object references:
     * <p>
     * <ul>
     * <li>It is <i>reflexive</i>: for any reference {@code x}, including null, {@code
     * equivalent(x, x)} returns {@code true}.
     * <li>It is <i>symmetric</i>: for any references {@code x} and {@code y}, {@code
     * equivalent(x, y) == equivalent(y, x)}.
     * <li>It is <i>transitive</i>: for any references {@code x}, {@code y}, and {@code z}, if
     * {@code equivalent(x, y)} returns {@code true} and {@code equivalent(y, z)} returns {@code
     * true}, then {@code equivalent(x, z)} returns {@code true}.
     * <li>It is <i>consistent</i>: for any references {@code x} and {@code y}, multiple invocations
     * of {@code equivalent(x, y)} consistently return {@code true} or consistently return {@code
     * false} (provided that neither {@code x} nor {@code y} is modified).
     * </ul>
     */
    public final boolean equivalent(@Nullable T a, @Nullable T b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return doEquivalent(a, b);
    }

    /**
     * Returns {@code true} if {@code a} and {@code b} are considered equivalent.
     * <p>
     * <p>Called by {@link #equivalent}. {@code a} and {@code b} are not the same
     * object and are not nulls.
     *
     *  (previously, subclasses would override equivalent())
     */
    protected abstract boolean doEquivalent(T a, T b);

    /**
     * Returns a hash code for {@code t}.
     * <p>
     * <p>The {@code hash} has the following properties:
     * <ul>
     * <li>It is <i>consistent</i>: for any reference {@code x}, multiple invocations of
     * {@code hash(x}} consistently return the same value provided {@code x} remains unchanged
     * according to the definition of the equivalence. The hash need not remain consistent from
     * one execution of an application to another execution of the same application.
     * <li>It is <i>distributable across equivalence</i>: for any references {@code x} and {@code y},
     * if {@code equivalent(x, y)}, then {@code hash(x) == hash(y)}. It is <i>not</i> necessary
     * that the hash be distributable across <i>inequivalence</i>. If {@code equivalence(x, y)}
     * is false, {@code hash(x) == hash(y)} may still be true.
     * <li>{@code hash(null)} is {@code 0}.
     * </ul>
     */
    public final int hash(@Nullable T t) {
        if (t == null) {
            return 0;
        }
        return doHash(t);
    }

    /**
     * Returns a hash code for non-null object {@code t}.
     * <p>
     * <p>Called by {@link #hash}.
     */
    protected abstract int doHash(T t);

    /**
     * Returns a new equivalence relation for {@code F} which evaluates equivalence by first applying
     * {@code function} to the argument, then evaluating using {@code this}. That is, for any pair of
     * non-null objects {@code x} and {@code y}, {@code
     * equivalence.onResultOf(function).equivalent(a, b)} is true if and only if {@code
     * equivalence.equivalent(function.apply(a), function.apply(b))} is true.
     * <p>
     * <p>For example:
     * <p>
     * <pre>   {@code
     *    Equivalence<Person> SAME_AGE = Equivalence.equals().onResultOf(GET_PERSON_AGE);}</pre>
     * <p>
     * <p>{@code function} will never be invoked with a null value.
     * <p>
     * <p>Note that {@code function} must be consistent according to {@code this} equivalence
     * relation. That is, invoking {@link Function#apply} multiple times for a given value must return
     * equivalent results.
     * For example, {@code Equivalence.identity().onResultOf(Functions.toStringFunction())} is broken
     * because it's not guaranteed that {@link Object#toString}) always returns the same string
     * instance.
     */
    public final <F> Equivalence<F> onResultOf(Function<F, ? extends T> function) {
        return new FunctionalEquivalence<F, T>(function, this);
    }

    /**
     * Returns a wrapper of {@code reference} that implements
     * {@link Wrapper#equals(Object) Object.equals()} such that
     * {@code wrap(a).equals(wrap(b))} if and only if {@code equivalent(a, b)}.
     */
    public final <S extends T> Wrapper<S> wrap(@Nullable S reference) {
        return new Wrapper<S>(this, reference);
    }

    /**
     * Wraps an object so that {@link #equals(Object)} and {@link #hashCode()} delegate to an
     * {@link Equivalence}.
     * <p>
     * <p>For example, given an {@link Equivalence} for {@link String strings} named {@code equiv}
     * that tests equivalence using their lengths:
     * <p>
     * <pre>   {@code
     *   equiv.wrap("a").equals(equiv.wrap("b")) // true
     *   equiv.wrap("a").equals(equiv.wrap("hello")) // false}</pre>
     * <p>
     * <p>Note in particular that an equivalence wrapper is never equal to the object it wraps.
     * <p>
     * <pre>   {@code
     *   equiv.wrap(obj).equals(obj) // always false}</pre>
     *
     * 
     */
    public static final class Wrapper<T> implements Serializable {
        private final Equivalence<? super T> equivalence;
        @Nullable
        private final T reference;

        private Wrapper(Equivalence<? super T> equivalence, @Nullable T reference) {
            this.equivalence = Preconditions.checkNotNull(equivalence);
            this.reference = reference;
        }

        /**
         * Returns the (possibly null) reference wrapped by this instance.
         */
        @Nullable
        public T get() {
            return reference;
        }

        /**
         * Returns {@code true} if {@link Equivalence#equivalent(Object, Object)} applied to the wrapped
         * references is {@code true} and both wrappers use the {@link Object#equals(Object) same}
         * equivalence.
         */
        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Wrapper) {
                Wrapper<?> that = (Wrapper<?>) obj; // note: not necessarily a Wrapper<T>

                if (this.equivalence.equals(that.equivalence)) {
                    /*
                     * We'll accept that as sufficient "proof" that either equivalence should be able to
                     * handle either reference, so it's safe to circumvent compile-time type checking.
                     */
                    @SuppressWarnings("unchecked")
                    Equivalence<Object> equivalence = (Equivalence<Object>) this.equivalence;
                    return equivalence.equivalent(this.reference, that.reference);
                }
            }
            return false;
        }

        /**
         * Returns the result of {@link Equivalence#hash(Object)} applied to the wrapped reference.
         */
        @Override
        public int hashCode() {
            return equivalence.hash(reference);
        }

        /**
         * Returns a string representation for this equivalence wrapper. The form of this string
         * representation is not specified.
         */
        @Override
        public String toString() {
            return equivalence + ".wrap(" + reference + ")";
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns an equivalence over iterables based on the equivalence of their elements.  More
     * specifically, two iterables are considered equivalent if they both contain the same number of
     * elements, and each pair of corresponding elements is equivalent according to
     * {@code this}.  Null iterables are equivalent to one another.
     * <p>
     * <p>Note that this method performs a similar function for equivalences
     *
     * 
     */
    public final <S extends T> Equivalence<Iterable<S>> pairwise() {
        // Ideally, the returned equivalence would support Iterable<? extends T>. However,
        // the need for this is so rare that it's not worth making callers deal with the ugly wildcard.
        return new PairwiseEquivalence<S>(this);
    }

    /**
     * Returns a predicate that evaluates to true if and only if the input is
     * equivalent to {@code target} according to this equivalence relation.
     */
    public final Predicate<T> equivalentTo(@Nullable T target) {
        return new EquivalentToPredicate<T>(this, target);
    }

    private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable {

        private final Equivalence<T> equivalence;
        @Nullable
        private final T target;

        EquivalentToPredicate(Equivalence<T> equivalence, @Nullable T target) {
            this.equivalence = Preconditions.checkNotNull(equivalence);
            this.target = target;
        }

        @Override
        public boolean test(@Nullable T input) {
            return equivalence.equivalent(input, target);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof EquivalentToPredicate) {
                EquivalentToPredicate<?> that = (EquivalentToPredicate<?>) obj;
                return equivalence.equals(that.equivalence)
                        && Objs.equals(target, that.target);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objs.hash(equivalence, target);
        }

        @Override
        public String toString() {
            return equivalence + ".equivalentTo(" + target + ")";
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Returns an equivalence that delegates to {@link Object#equals} and {@link Object#hashCode}.
     * {@link Equivalence#equivalent} returns {@code true} if both values are null, or if neither
     * value is null and {@link Object#equals} returns {@code true}. {@link Equivalence#hash} returns
     * {@code 0} if passed a null value.
     *
     * 
     *  (in Equivalences with null-friendly behavior)
     *  (in Equivalences)
     */
    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }

    /**
     * Returns an equivalence that uses {@code ==} to compare values and {@link
     * System#identityHashCode(Object)} to compute the hash code.  {@link Equivalence#equivalent}
     * returns {@code true} if {@code a == b}, including in the case that a and b are both null.
     *
     * 
     *  (in Equivalences)
     */
    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }

    static final class Equals extends Equivalence<Object>
            implements Serializable {

        static final Equals INSTANCE = new Equals();

        @Override
        protected boolean doEquivalent(Object a, Object b) {
            return a.equals(b);
        }

        @Override
        protected int doHash(Object o) {
            return o.hashCode();
        }

        private Object readResolve() {
            return INSTANCE;
        }

        private static final long serialVersionUID = 1;
    }

    static final class Identity extends Equivalence<Object>
            implements Serializable {

        static final Identity INSTANCE = new Identity();

        @Override
        protected boolean doEquivalent(Object a, Object b) {
            return false;
        }

        @Override
        protected int doHash(Object o) {
            return System.identityHashCode(o);
        }

        private Object readResolve() {
            return INSTANCE;
        }

        private static final long serialVersionUID = 1;
    }

    final class FunctionalEquivalence<F, T> extends Equivalence<F>
            implements Serializable {

        private static final long serialVersionUID = 0;

        private final Function<F, ? extends T> function;
        private final Equivalence<T> resultEquivalence;

        FunctionalEquivalence(
                Function<F, ? extends T> function, Equivalence<T> resultEquivalence) {
            this.function = Preconditions.checkNotNull(function);
            this.resultEquivalence = Preconditions.checkNotNull(resultEquivalence);
        }

        @Override
        protected boolean doEquivalent(F a, F b) {
            return resultEquivalence.equivalent(function.apply(a), function.apply(b));
        }

        @Override
        protected int doHash(F a) {
            return resultEquivalence.hash(function.apply(a));
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof FunctionalEquivalence) {
                FunctionalEquivalence<?, ?> that = (FunctionalEquivalence<?, ?>) obj;
                return function.equals(that.function)
                        && resultEquivalence.equals(that.resultEquivalence);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objs.hash(function, resultEquivalence);
        }

        @Override
        public String toString() {
            return resultEquivalence + ".onResultOf(" + function + ")";
        }
    }

    final class PairwiseEquivalence<T> extends Equivalence<Iterable<T>>
            implements Serializable {

        final Equivalence<? super T> elementEquivalence;

        PairwiseEquivalence(Equivalence<? super T> elementEquivalence) {
            this.elementEquivalence = Preconditions.checkNotNull(elementEquivalence);
        }

        @Override
        protected boolean doEquivalent(Iterable<T> iterableA, Iterable<T> iterableB) {
            Iterator<T> iteratorA = iterableA.iterator();
            Iterator<T> iteratorB = iterableB.iterator();

            while (iteratorA.hasNext() && iteratorB.hasNext()) {
                if (!elementEquivalence.equivalent(iteratorA.next(), iteratorB.next())) {
                    return false;
                }
            }

            return !iteratorA.hasNext() && !iteratorB.hasNext();
        }

        @Override
        protected int doHash(Iterable<T> iterable) {
            int hash = 78721;
            for (T element : iterable) {
                hash = hash * 24943 + elementEquivalence.hash(element);
            }
            return hash;
        }

        @Override
        public boolean equals(@Nullable Object object) {
            if (object instanceof PairwiseEquivalence) {
                PairwiseEquivalence<?> that = (PairwiseEquivalence<?>) object;
                return this.elementEquivalence.equals(that.elementEquivalence);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return elementEquivalence.hashCode() ^ 0x46a3eb07;
        }

        @Override
        public String toString() {
            return elementEquivalence + ".pairwise()";
        }

        private static final long serialVersionUID = 1;
    }

}
