package com.jn.langx.util.io.file.filter.warp;

import com.jn.langx.Filter;
import com.jn.langx.util.io.file.filter.AbstractFileFilter;

import java.io.File;

public class FilterToFileFilter extends AbstractFileFilter {
    private Filter<File> delegate;

    public FilterToFileFilter(Filter<File> filter){
        this.delegate= filter;
    }

    @Override
    public boolean accept(File e) {
        return this.delegate.accept(e);
    }

    @Override
    public boolean accept(File dir, String name) {
        return this.delegate.accept(new File(dir,name));
    }
}
