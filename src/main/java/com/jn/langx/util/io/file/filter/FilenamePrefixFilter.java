package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilenamePrefixFilter extends AbstractFileFilter {
    private boolean ignoreCase = true;
    private Set<String> prefixes = new HashSet<String>();
    ;

    public FilenamePrefixFilter(String prefix) {
        this(prefix, true);
    }

    public FilenamePrefixFilter(String prefix, boolean ignoreCase) {
        this(ignoreCase, Collects.<String>asIterable(prefix));
    }

    public FilenamePrefixFilter(String[] prefixes) {
        this(prefixes, true);
    }

    public FilenamePrefixFilter(String[] prefixes, boolean ignoreCase) {
        this(Collects.asList(prefixes), ignoreCase);
    }

    public FilenamePrefixFilter(List<String> prefixes) {
        this(prefixes, true);
    }

    public FilenamePrefixFilter(List<String> prefixes, boolean ignoreCase) {
        this(ignoreCase, prefixes);
    }

    private FilenamePrefixFilter(boolean ignoreCase, Iterable<String> prefixes) {
        if (ignoreCase) {
            for (String suffix : prefixes) {
                if (Strings.isNotBlank(suffix)) {
                    this.prefixes.add(suffix.toLowerCase());
                }
            }
            Pipeline.of(prefixes).filter(Functions.<String>nonNullPredicate()).addTo(this.prefixes);
        }

        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean accept(File file) {
        return accept(file.getParentFile(), file.getName());
    }

    @Override
    public boolean accept(File dir, final String name) {
        return Collects.anyMatch(this.prefixes, new Predicate<String>() {
            @Override
            public boolean test(String prefix) {
                return Strings.startsWith(name, prefix, ignoreCase);
            }
        });
    }
}
