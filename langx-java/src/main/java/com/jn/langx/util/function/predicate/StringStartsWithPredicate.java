package com.jn.langx.util.function.predicate;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringStartsWithPredicate implements Predicate<String> {
    private boolean ignoreCase;
    private Set<String> prefixes = new HashSet<String>();

    public StringStartsWithPredicate(String... prefixes) {
        this(true, prefixes);
    }

    public StringStartsWithPredicate(boolean ignoreCase, String... prefixes) {
        this(ignoreCase, Collects.asList(prefixes));
    }

    public StringStartsWithPredicate(List<String> prefixes) {
        this(true, prefixes);
    }

    public StringStartsWithPredicate(boolean ignoreCase, Iterable<String> prefixes) {
        if (ignoreCase) {
            for (String suffix : prefixes) {
                if (Strings.isNotBlank(suffix)) {
                    this.prefixes.add(suffix.toLowerCase());
                }
            }
        }
        Pipeline.of(prefixes).filter(Functions.<String>nonNullPredicate()).addTo(this.prefixes);
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean test(final String value) {
        Preconditions.checkNotEmpty(this.prefixes);
        return Collects.anyMatch(this.prefixes, new Predicate<String>() {
            @Override
            public boolean test(String prefix) {
                return Strings.startsWith(value, prefix, ignoreCase);
            }
        });
    }
}
