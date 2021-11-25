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

public class FilenameSuffixFilter extends AbstractFileFilter {
    private boolean ignoreCase = true;
    private Set<String> suffixes = new HashSet<String>();

    public FilenameSuffixFilter(String suffix) {
        this(suffix, true);
    }

    public FilenameSuffixFilter(String suffix, boolean ignoreCase) {
        this(ignoreCase, Collects.<String>asIterable(suffix));
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
        this(ignoreCase, suffixes);
    }

    private FilenameSuffixFilter(boolean ignoreCase, Iterable<String> suffixes) {
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
    public boolean accept(File file) {
        return accept(file.getParentFile(), file.getName());
    }

    @Override
    public boolean accept(File dir, String name) {
        String suffix = ignoreCase ? Filenames.getSuffixAsLowCase(name) : Filenames.getSuffix(name);
        return this.suffixes.contains(suffix);
    }

}
