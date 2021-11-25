package com.jn.langx.util.io.file.filter.warp;

import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.file.filter.AbstractFileFilter;

import java.io.File;

public class FilePredicateToFilterFilter extends AbstractFileFilter {
    private Predicate<File> predicate;

    public FilePredicateToFilterFilter(Predicate<File> filePredicate){
        this.predicate = filePredicate;
    }

    @Override
    public boolean accept(File e) {
        return predicate.test(e);
    }

    @Override
    public boolean accept(File dir, String name) {
        return accept(new File(dir,name));
    }
}
