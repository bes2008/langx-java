package com.jn.langx.util.io.file.filter;

import java.io.File;

public class IsFileFilter extends AbstractFileFilter {
    @Override
    public boolean accept(File file) {
        return file.isFile();
    }

    @Override
    public boolean accept(File dir, String filename) {
        return accept(new File(dir, filename));
    }
}
