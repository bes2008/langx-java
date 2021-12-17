package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.function.Predicate;

import java.io.File;

public class FilenamePredicateFilter extends AbstractFileFilter{
    protected Predicate<String> predicate;

    public FilenamePredicateFilter(Predicate<String> predicate){
        this.predicate = predicate;
    }

    @Override
    public final boolean accept(File e) {
        return accept(e.getParentFile(), e.getName());
    }

    @Override
    public boolean accept(File dir, String name) {
        return predicate.test(name);
    }
}
