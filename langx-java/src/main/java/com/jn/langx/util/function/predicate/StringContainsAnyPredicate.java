package com.jn.langx.util.function.predicate;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;

import java.util.Collection;

public class StringContainsAnyPredicate implements Predicate<String> {
    private Collection<String> seeds;
    private boolean ignoreCase;

    public StringContainsAnyPredicate(String... seeds) {
        this(true, Collects.asList(seeds));
    }

    public StringContainsAnyPredicate(Collection<String> seeds) {
        this(true, seeds);
    }

    public StringContainsAnyPredicate(boolean ignoreCase, Collection<String> seeds) {
        this.seeds = seeds;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean test(final String value) {
        return Pipeline.of(seeds)
                .anyMatch(new Predicate<String>() {
                    @Override
                    public boolean test(String seed) {
                        return Strings.contains(value, seed, ignoreCase);
                    }
                });
    }
}
