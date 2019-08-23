package com.jn.langx.util.io.file.filter;

import java.io.File;

public class IsDirectoryFileFilter extends AbstractFileFilter {

    @Override
    public boolean accept(File dir, String filename) {
        return accept(new File(dir, filename));
    }

    @Override
    public boolean accept(File file) {
        return file.isDirectory();
    }
}
