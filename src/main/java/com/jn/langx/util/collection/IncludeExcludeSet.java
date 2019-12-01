package com.jn.langx.util.collection;


import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Predicate;

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
    private final Set<T> _includes;
    private final Predicate<P> _includePredicate;
    private final Set<T> _excludes;
    private final Predicate<P> _excludePredicate;

    private static class SetContainsPredicate<T> implements Predicate<T> {
        private final Set<T> set;

        public SetContainsPredicate(Set<T> set) {
            this.set = set;
        }

        @Override
        public boolean test(T item) {
            return set.contains(item);
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
            _includes = setClass.getDeclaredConstructor().newInstance();
            _excludes = setClass.getDeclaredConstructor().newInstance();

            if (_includes instanceof Predicate) {
                _includePredicate = (Predicate<P>) _includes;
            } else {
                _includePredicate = new IncludeExcludeSet.SetContainsPredicate(_includes);
            }

            if (_excludes instanceof Predicate) {
                _excludePredicate = (Predicate<P>) _excludes;
            } else {
                _excludePredicate = new IncludeExcludeSet.SetContainsPredicate(_excludes);
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
        Preconditions.checkNotNull(includeSet, "Include Set");
        Preconditions.checkNotNull(includePredicate, "Include Predicate");
        Preconditions.checkNotNull(excludeSet, "Exclude Set");
        Preconditions.checkNotNull(excludePredicate, "Exclude Predicate");

        _includes = includeSet;
        _includePredicate = includePredicate;
        _excludes = excludeSet;
        _excludePredicate = excludePredicate;
    }

    public void include(T element) {
        _includes.add(element);
    }

    public void include(T... element) {
        for (T e : element) {
            _includes.add(e);
        }
    }

    public void exclude(T element) {
        _excludes.add(element);
    }

    public void exclude(T... element) {
        for (T e : element) {
            _excludes.add(e);
        }
    }

    @Deprecated
    public boolean matches(P t) {
        return test(t);
    }

    @Override
    public boolean test(P t) {
        if (!_includes.isEmpty() && !_includePredicate.test(t))
            return false;
        return !_excludePredicate.test(t);
    }

    /**
     * Test Included and not Excluded
     *
     * @param item The item to test
     * @return Boolean.TRUE if item is included, Boolean.FALSE if item is excluded or null if neither
     */
    public Boolean isIncludedAndNotExcluded(P item) {
        if (_excludePredicate.test(item)) {
            return Boolean.FALSE;
        }
        if (_includePredicate.test(item)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public boolean hasIncludes() {
        return !_includes.isEmpty();
    }

    public boolean hasExcludes() {
        return !_excludes.isEmpty();
    }

    public int size() {
        return _includes.size() + _excludes.size();
    }

    public Set<T> getIncluded() {
        return _includes;
    }

    public Set<T> getExcluded() {
        return _excludes;
    }

    public void clear() {
        _includes.clear();
        _excludes.clear();
    }

    @Override
    public String toString() {
        return String.format("%s@%x{i=%s,ip=%s,e=%s,ep=%s}", this.getClass().getSimpleName(), hashCode(),
                _includes,
                _includePredicate == _includes ? "SELF" : _includePredicate,
                _excludes,
                _excludePredicate == _excludes ? "SELF" : _excludePredicate);
    }

    public boolean isEmpty() {
        return _includes.isEmpty() && _excludes.isEmpty();
    }
}
