package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.io.file.FileFilter;

import java.io.File;

public abstract class AbstractFileFilter implements FileFilter {
    @Override
    public boolean test(File file) {
        return accept(file);
    }

    @Override
    public boolean test(File dir, String name) {
        return accept(dir, name);
    }

}
