package com.jn.langx.util.io.file.filter;

import java.io.File;

public class WriteableFileFilter extends AbstractFileFilter {
    @Override
    public boolean accept(File file) {
        return file.canWrite();
    }

    @Override
    public boolean accept(File dir, String name) {
        return accept(new File(dir, name));
    }
}
