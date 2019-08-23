package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.io.file.FileFilter;

import java.io.File;

public class IsDirectoryFileFilter implements FileFilter{
    @Override
    public boolean test(File file) {
        return file.isDirectory();
    }

    @Override
    public boolean test(File dir, String filename) {
        return test(new File(dir, filename));
    }

    @Override
    public boolean accept(File file) {
        return test(file);
    }
}
