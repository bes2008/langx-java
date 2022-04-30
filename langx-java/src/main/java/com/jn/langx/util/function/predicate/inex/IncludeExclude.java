package com.jn.langx.util.function.predicate.inex;

import com.jn.langx.util.function.Predicate;

import java.util.Set;

/**
 * Utility class to maintain a set of inclusions and exclusions.
 * <p>This extension of the {@link IncludeExcludeSet} class is used
 * when the type of the set elements is the same as the type of
 * the predicate test.
 * <p>
 *
 * @param <ITEM> The type of element
 */
public class IncludeExclude<ITEM> extends IncludeExcludeSet<ITEM, ITEM> {
    public IncludeExclude() {
        super();
    }

    public <SET extends Set<ITEM>> IncludeExclude(Class<SET> setClass) {
        super(setClass);
    }

    public <SET extends Set<ITEM>> IncludeExclude(Set<ITEM> includeSet, Predicate<ITEM> includePredicate, Set<ITEM> excludeSet,
                                                  Predicate<ITEM> excludePredicate) {
        super(includeSet, includePredicate, excludeSet, excludePredicate);
    }
}