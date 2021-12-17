package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.predicate.StringStartsWithPredicate;

import java.util.List;

public class FilenamePrefixFilter extends FilenamePredicateFilter {

    public FilenamePrefixFilter(String prefix) {
        this(prefix, true);
    }

    public FilenamePrefixFilter(String prefix, boolean ignoreCase) {
        super(new StringStartsWithPredicate(ignoreCase, prefix));
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
        super(new StringStartsWithPredicate(ignoreCase, prefixes));
    }

}
