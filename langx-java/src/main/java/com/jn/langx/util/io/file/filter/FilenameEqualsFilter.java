package com.jn.langx.util.io.file.filter;


import com.jn.langx.util.function.predicate.StringEqualsPredicate;


/**
 * @since 4.1.0
 */
public class FilenameEqualsFilter extends FilenamePredicateFilter {


    public FilenameEqualsFilter(String ref) {
        this(ref, false);
    }

    public FilenameEqualsFilter(String ref, boolean ignoreCase) {
        super(new StringEqualsPredicate(ref, ignoreCase));
    }

}
