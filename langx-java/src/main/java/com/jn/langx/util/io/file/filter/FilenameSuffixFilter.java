package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.Strings;
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

    public FilenameSuffixFilter(final boolean ignoreCase, String... suffixes) {
        this(new Predicate2<String, String[]>() {
            @Override
            public boolean test(String suffix, String[] suffixes) {
                return new StringEndsWithPredicate(ignoreCase, suffixes).test(suffix);
            }
        }, suffixes);
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

    public static FilenameSuffixFilter ofIn(final boolean ignore, String... suffixes) {
        return new FilenameSuffixFilter(new Predicate2<String, String[]>() {
            @Override
            public boolean test(final String suffix, String[] suffixes) {
                return Collects.contains(suffixes, suffix, new Predicate<String>() {
                    @Override
                    public boolean test(String value) {
                        return Strings.equals(suffix, value, ignore);
                    }
                });
            }
        }, suffixes);
    }

    public static FilenameSuffixFilter ofNotIn(final boolean ignore, String... suffixes) {
        return new FilenameSuffixFilter(new Predicate2<String, String[]>() {
            @Override
            public boolean test(final String suffix, String[] suffixes) {
                return !Collects.contains(suffixes, suffix, new Predicate<String>() {
                    @Override
                    public boolean test(String value) {
                        return Strings.equals(suffix, value, ignore);
                    }
                });
            }
        }, suffixes);
    }
}
