package com.jn.langx.util.io.file.filter.warp;

import com.jn.langx.util.io.file.filter.AbstractFileFilter;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @since 4.1.0
 */
public class FilenameFilterToFileFilter extends AbstractFileFilter {

    private FilenameFilter delegate;

    public FilenameFilterToFileFilter(FilenameFilter filter) {
        this.delegate = filter;
    }

    @Override
    public boolean accept(File e) {
        return delegate.accept(e.getParentFile(), e.getName());
    }

    @Override
    public boolean accept(File dir, String name) {
        return delegate.accept(dir, name);
    }
}
