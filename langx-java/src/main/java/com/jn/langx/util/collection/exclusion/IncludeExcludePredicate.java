package com.jn.langx.util.collection.exclusion;


import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Predicate2;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A {@link Predicate} that allows for inclusion and exclusion of items.
 *
 * @param <E> 集体中元素的类型
 * @param <T> 要检测的数据类型
 * @since 5.5.2
 */
public class IncludeExcludePredicate<E, T> implements Predicate<T> {
    private final Set<E> inclusions = new LinkedHashSet<E>();
    private final Predicate2<E, T> includePredicate;
    private final Set<E> exclusions = new LinkedHashSet<E>();
    private final Predicate2<E, T> excludePredicate;

    /**
     * Default constructor over {@link HashSet}
     */
    public IncludeExcludePredicate() {
        this(null, null, null, null);
    }


    public IncludeExcludePredicate(Collection<E> includeSet, Collection<E> excludeSet) {
        this(includeSet, excludeSet, null);
    }

    public IncludeExcludePredicate(Predicate2<E, T> predicate) {
        this(null, predicate, null, predicate);
    }

    public IncludeExcludePredicate(Collection<E> includeSet, Collection<E> excludeSet, Predicate2<E, T> predicate) {
        this(includeSet, predicate, excludeSet, predicate);
    }

    public IncludeExcludePredicate(Collection<E> includeSet, Predicate2<E, T> includePredicate, Collection<E> excludeSet, Predicate2<E, T> excludePredicate) {
        if (includeSet != null) {
            inclusions.addAll(includeSet);
        }
        this.includePredicate = includePredicate != null ? includePredicate : new Predicate2<E, T>() {
            @Override
            public boolean test(E element, T value) {
                return Objs.deepEquals(element, value);
            }
        };
        if (excludeSet != null) {
            exclusions.addAll(excludeSet);
        }
        this.excludePredicate = excludePredicate != null ? excludePredicate : new Predicate2<E, T>() {
            @Override
            public boolean test(E element, T value) {
                return Objs.deepEquals(element, value);
            }
        };
    }

    public void addInclusion(E element) {
        if (element != null) {
            inclusions.add(element);
        }
    }

    public void addExclusion(E element) {
        if (element != null) {
            exclusions.add(element);
        }
    }

    public void addInclusions(E... elements) {
        for (E e : elements) {
            addInclusion(e);
        }
    }

    public void addExclusions(E... elements) {
        for (E e : elements) {
            addExclusion(e);
        }
    }

    @Override
    public boolean test(final T t) {
        if (!inclusions.isEmpty()) {
            boolean found = Collects.anyMatch(inclusions, new Predicate<E>() {
                @Override
                public boolean test(E element) {
                    return includePredicate.test(element, t);
                }
            });

            if (!found) {
                return false;
            }
        }
        if (exclusions.isEmpty()) {
            return true;
        }

        return Collects.noneMatch(exclusions, new Predicate<E>() {
            @Override
            public boolean test(E element) {
                return excludePredicate.test(element, t);
            }
        });
    }

    @Override
    public String toString() {
        return String.format("%s@%x{i=%s,ip=%s,e=%s,ep=%s}", this.getClass().getSimpleName(), hashCode(),
                inclusions,
                includePredicate == null ? "SELF" : includePredicate,
                exclusions,
                excludePredicate == null ? "SELF" : excludePredicate);
    }

}
