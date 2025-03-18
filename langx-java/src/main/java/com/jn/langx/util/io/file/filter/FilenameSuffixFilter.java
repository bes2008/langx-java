package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.function.predicate.StringEndsWithPredicate;
import com.jn.langx.util.io.file.Filenames;

import java.util.List;

public class FilenameSuffixFilter extends FilenamePredicateFilter {

    public FilenameSuffixFilter(String suffix) {
        this(suffix, true);
    }

    public FilenameSuffixFilter(String suffix, boolean ignoreCase) {
        super(new StringEndsWithPredicate(ignoreCase, suffix));
    }

    public FilenameSuffixFilter(final Predicate2<String, String[]> predicate, final String... suffixes) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String suffix) {
                return predicate.test(suffix, suffixes);
            }
        });
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

    @Override
    protected boolean doTest(String name) {
        String suffix = Filenames.getSuffix(name);
        return super.doTest(suffix);
    }
}
