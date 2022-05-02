package com.jn.langx.util.collection.exclusion;


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
    private final Set<T> inclusions;
    private final Predicate<P> includePredicate;
    private final Set<T> exclusions;
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
            inclusions = setClass.getDeclaredConstructor().newInstance();
            exclusions = setClass.getDeclaredConstructor().newInstance();

            if (inclusions instanceof Predicate) {
                includePredicate = (Predicate<P>) inclusions;
            } else {
                includePredicate = new IncludeExcludeSet.SetContainsPredicate(inclusions);
            }

            if (exclusions instanceof Predicate) {
                excludePredicate = (Predicate<P>) exclusions;
            } else {
                excludePredicate = new IncludeExcludeSet.SetContainsPredicate(exclusions);
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

        inclusions = includeSet;
        this.includePredicate = includePredicate;
        exclusions = excludeSet;
        this.excludePredicate = excludePredicate;
    }

    public void addInclusion(T element) {
        inclusions.add(element);
    }

    public void addInclusions(T... element) {
        inclusions.addAll(Arrays.asList(element));
    }

    public void addExclusion(T element) {
        exclusions.add(element);
    }

    public void addExclusions(T... element) {
        exclusions.addAll(Arrays.asList(element));
    }

    @Override
    public boolean test(P t) {
        if (!inclusions.isEmpty() && !includePredicate.test(t)) {
            return false;
        }
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

    public boolean hasInclusions() {
        return !inclusions.isEmpty();
    }

    public boolean hasExclusions() {
        return !exclusions.isEmpty();
    }

    public int size() {
        return inclusions.size() + exclusions.size();
    }

    public Set<T> getInclusions() {
        return inclusions;
    }

    public Set<T> getExclusions() {
        return exclusions;
    }

    public void clear() {
        inclusions.clear();
        exclusions.clear();
    }

    @Override
    public String toString() {
        return String.format("%s@%x{i=%s,ip=%s,e=%s,ep=%s}", this.getClass().getSimpleName(), hashCode(),
                inclusions,
                includePredicate == inclusions ? "SELF" : includePredicate,
                exclusions,
                excludePredicate == exclusions ? "SELF" : excludePredicate);
    }

    public boolean isEmpty() {
        return inclusions.isEmpty() && exclusions.isEmpty();
    }
}
