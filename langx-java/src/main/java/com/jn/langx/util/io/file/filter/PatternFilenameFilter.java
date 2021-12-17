package com.jn.langx.util.io.file.filter;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.function.predicate.StringPatternPredicate;

import java.util.regex.Pattern;

public class PatternFilenameFilter extends FilenamePredicateFilter {

    public PatternFilenameFilter(@NonNull String pattern) {
        super(new StringPatternPredicate(pattern));
    }

    public PatternFilenameFilter(@NonNull Pattern pattern) {
        super(new StringPatternPredicate(pattern));
    }
}
