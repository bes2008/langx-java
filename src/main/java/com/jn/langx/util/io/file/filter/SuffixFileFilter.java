package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.io.file.Filenames;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuffixFileFilter extends AbstractFileFilter {
    private boolean ignoreCase = true;
    private Set<String> suffixes = new HashSet<String>();

    public SuffixFileFilter(String suffix) {
        this(suffix, true);
    }

    public SuffixFileFilter(String suffix, boolean ignoreCase) {
        this(ignoreCase, Collects.<String>asIterable(suffix));
    }

    public SuffixFileFilter(String[] suffixes) {
        this(suffixes, true);
    }

    public SuffixFileFilter(String[] suffixes, boolean ignoreCase) {
        this(Collects.asList(suffixes), ignoreCase);
    }

    public SuffixFileFilter(List<String> suffixes) {
        this(suffixes, true);
    }

    public SuffixFileFilter(List<String> suffixes, boolean ignoreCase) {
        this(ignoreCase, suffixes);
    }

    private SuffixFileFilter(boolean ignoreCase, Iterable<String> suffixes) {
        if (ignoreCase) {
            for (String suffix : suffixes) {
                if (Strings.isNotBlank(suffix)) {
                    this.suffixes.add(suffix.toLowerCase());
                }
            }
            Pipeline.of(suffixes).filter(Functions.<String>nonNullPredicate()).addTo(this.suffixes);
        }

        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean accept(File file) {
        return accept(file.getParentFile(), file.getName());
    }

    @Override
    public boolean accept(File dir, String name) {
        String suffix = ignoreCase ? Filenames.getSuffixAsLowCase(name) : Filenames.getSuffix(name);
        return this.suffixes.contains(suffix);
    }

}
