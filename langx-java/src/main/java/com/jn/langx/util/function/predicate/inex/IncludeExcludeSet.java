package com.jn.langx.util.function.predicate.inex;


import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class to maintain a set of inclusions and exclusions.
 * <p>Maintains a set of included and excluded elements.  The method {@link #test(Object)}
 * will return true IFF the passed object is not in the excluded set AND ( either the
 * included set is empty OR the object is in the included set)
 * <p>The type of the underlying {@link Set} used may be passed into the
 * constructor, so special sets like Servlet PathMap may be used.
 * <p>
 *
 * @param <T> The type of element of the set (often a pattern)
 * @param <P> The type of the instance passed to the predicate
 */
public class IncludeExcludeSet<T, P> implements Predicate<P> {
    private final Set<T> includes;
    private final Predicate<P> includePredicate;
    private final Set<T> excludes;
    private final Predicate<P> excludePredicate;

    private static class SetContainsPredicate<T> implements Predicate<T> {
        private final Set<T> set;

        public SetContainsPredicate(Set<T> set) {
            this.set = set;
        }

        @Override
        public boolean test(T item) {
            return set.contains(item);
        }

        @Override
        public String toString() {
            return "CONTAINS";
        }
    }

    /**
     * Default constructor over {@link HashSet}
     */
    public IncludeExcludeSet() {
        this(HashSet.class);
    }

    /**
     * Construct an IncludeExclude.
     *
     * @param setClass The type of {@link Set} to using internally to hold patterns. Two instances will be created.
     *                 one for include patterns and one for exclude patters.  If the class is also a {@link Predicate},
     *                 then it is also used as the item test for the set, otherwise a {@link IncludeExcludeSet.SetContainsPredicate} instance
     *                 is created.
     * @param <SET>    The type of a set to use as the backing store
     */
    public <SET extends Set<T>> IncludeExcludeSet(Class<SET> setClass) {
        try {
            includes = setClass.getDeclaredConstructor().newInstance();
            excludes = setClass.getDeclaredConstructor().newInstance();

            if (includes instanceof Predicate) {
                includePredicate = (Predicate<P>) includes;
            } else {
                includePredicate = new IncludeExcludeSet.SetContainsPredicate(includes);
            }

            if (excludes instanceof Predicate) {
                excludePredicate = (Predicate<P>) excludes;
            } else {
                excludePredicate = new IncludeExcludeSet.SetContainsPredicate(excludes);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Construct an IncludeExclude
     *
     * @param includeSet       the Set of items that represent the included space
     * @param includePredicate the Predicate for included item testing (null for simple {@link Set#contains(Object)} test)
     * @param excludeSet       the Set of items that represent the excluded space
     * @param excludePredicate the Predicate for excluded item testing (null for simple {@link Set#contains(Object)} test)
     * @param <SET>            The type of a set to use as the backing store
     */
    public <SET extends Set<T>> IncludeExcludeSet(Set<T> includeSet, Predicate<P> includePredicate, Set<T> excludeSet, Predicate<P> excludePredicate) {
        Objs.requireNonNull(includeSet, "Include Set");
        Objs.requireNonNull(includePredicate, "Include Predicate");
        Objs.requireNonNull(excludeSet, "Exclude Set");
        Objs.requireNonNull(excludePredicate, "Exclude Predicate");

        includes = includeSet;
        this.includePredicate = includePredicate;
        excludes = excludeSet;
        this.excludePredicate = excludePredicate;
    }

    public void addIncluded(T element) {
        includes.add(element);
    }

    public void addIncluded(T... element) {
        includes.addAll(Arrays.asList(element));
    }

    public void addExcluded(T element) {
        excludes.add(element);
    }

    public void addExcluded(T... element) {
        excludes.addAll(Arrays.asList(element));
    }

    @Override
    public boolean test(P t) {
        if (!includes.isEmpty() && !includePredicate.test(t))
            return false;
        return !excludePredicate.test(t);
    }

    /**
     * Test Included and not Excluded
     *
     * @param item The item to test
     * @return Boolean.TRUE if item is included, Boolean.FALSE if item is excluded or null if neither
     */
    public Boolean isIncludedAndNotExcluded(P item) {
        if (excludePredicate.test(item))
            return Boolean.FALSE;
        if (includePredicate.test(item))
            return Boolean.TRUE;

        return null;
    }

    public boolean hasIncluded() {
        return !includes.isEmpty();
    }

    public boolean hasExcluded() {
        return !excludes.isEmpty();
    }

    public int size() {
        return includes.size() + excludes.size();
    }

    public Set<T> getIncluded() {
        return includes;
    }

    public Set<T> getExcluded() {
        return excludes;
    }

    public void clear() {
        includes.clear();
        excludes.clear();
    }

    @Override
    public String toString() {
        return String.format("%s@%x{i=%s,ip=%s,e=%s,ep=%s}", this.getClass().getSimpleName(), hashCode(),
                includes,
                includePredicate == includes ? "SELF" : includePredicate,
                excludes,
                excludePredicate == excludes ? "SELF" : excludePredicate);
    }

    public boolean isEmpty() {
        return includes.isEmpty() && excludes.isEmpty();
    }
}
