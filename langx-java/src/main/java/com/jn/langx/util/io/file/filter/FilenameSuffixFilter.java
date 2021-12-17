package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.predicate.StringEndsWithPredicate;

import java.util.List;

public class FilenameSuffixFilter extends FilenamePredicateFilter {

    public FilenameSuffixFilter(String suffix) {
        this(suffix, true);
    }

    public FilenameSuffixFilter(String suffix, boolean ignoreCase) {
        super(new StringEndsWithPredicate(ignoreCase, suffix));
    }

    public FilenameSuffixFilter(String[] suffixes) {
        this(suffixes, true);
    }

    public FilenameSuffixFilter(String[] suffixes, boolean ignoreCase) {
        this(Collects.asList(suffixes), ignoreCase);
    }

    public FilenameSuffixFilter(List<String> suffixes) {
        this(suffixes, true);
    }

    public FilenameSuffixFilter(List<String> suffixes, boolean ignoreCase) {
        super(new StringEndsWithPredicate(ignoreCase, suffixes));
    }


}
