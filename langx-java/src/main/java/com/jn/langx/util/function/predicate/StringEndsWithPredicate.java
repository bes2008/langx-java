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

public class StringEndsWithPredicate implements Predicate<String> {

    private boolean ignoreCase;
    private Set<String> suffixes = new HashSet<String>();

    public StringEndsWithPredicate(String... suffixes) {
        this(true, suffixes);
    }

    public StringEndsWithPredicate(boolean ignoreCase, String... suffixes) {
        this(ignoreCase, Collects.asList(suffixes));
    }

    public StringEndsWithPredicate(List<String> suffixes) {
        this(true, suffixes);
    }

    public StringEndsWithPredicate(boolean ignoreCase, List<String> suffixes) {
        if (ignoreCase) {
            for (String suffix : suffixes) {
                if (Strings.isNotBlank(suffix)) {
                    this.suffixes.add(suffix.toLowerCase());
                }
            }
        }
        Pipeline.of(suffixes).filter(Functions.<String>nonNullPredicate()).addTo(this.suffixes);
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean test(final String value) {
        Preconditions.checkNotEmpty(this.suffixes);
        return Collects.anyMatch(this.suffixes, new Predicate<String>() {
            @Override
            public boolean test(String suffix) {
                return Strings.endsWith(value, suffix, ignoreCase);
            }
        });
    }
}
