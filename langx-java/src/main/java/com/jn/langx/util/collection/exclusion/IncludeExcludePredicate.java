package com.jn.langx.util.collection.exclusion;


import com.jn.langx.util.function.Predicate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A {@link Predicate} that allows for inclusion and exclusion of items.
 *
 * @param <T> the type of the items to be tested
 * @since 5.5.2
 */
public class IncludeExcludePredicate<T> implements Predicate<T> {
    private final Set<T> inclusions = new LinkedHashSet<T>();
    private final Predicate<T> includePredicate;
    private final Set<T> exclusions = new LinkedHashSet<T>();
    private final Predicate<T> excludePredicate;

    /**
     * Default constructor over {@link HashSet}
     */
    @SuppressWarnings("unchecked")
    public IncludeExcludePredicate() {
        this(null, null, null, null);
    }


    public IncludeExcludePredicate(Set<T> includeSet, Set<T> excludeSet) {
        this(includeSet, null, excludeSet, null);
    }

    public IncludeExcludePredicate(Set<T> includeSet, Predicate<T> includePredicate, Set<T> excludeSet, Predicate<T> excludePredicate) {
        if (includeSet != null) {
            inclusions.addAll(includeSet);
        }
        this.includePredicate = includePredicate != null ? includePredicate : new Predicate<T>() {
            @Override
            public boolean test(T value) {
                return IncludeExcludePredicate.this.inclusions.contains(value);
            }
        };
        if (excludeSet != null) {
            exclusions.addAll(excludeSet);
        }
        this.excludePredicate = excludePredicate != null ? excludePredicate : new Predicate<T>() {
            @Override
            public boolean test(T value) {
                return IncludeExcludePredicate.this.exclusions.contains(value);
            }
        };
    }

    public void addInclusions(T... element) {
        inclusions.addAll(Arrays.asList(element));
    }

    public void addExclusions(T... element) {
        exclusions.addAll(Arrays.asList(element));
    }

    @Override
    public boolean test(T t) {
        if (!inclusions.isEmpty() && !includePredicate.test(t)) {
            return false;
        }
        if (exclusions.isEmpty()) {
            return true;
        }
        return !excludePredicate.test(t);
    }

    public boolean hasInclusions() {
        return !inclusions.isEmpty();
    }

    public boolean hasExclusions() {
        return !exclusions.isEmpty();
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
                includePredicate == null ? "SELF" : includePredicate,
                exclusions,
                excludePredicate == null ? "SELF" : excludePredicate);
    }

    public boolean isEmpty() {
        return inclusions.isEmpty() && exclusions.isEmpty();
    }
}
