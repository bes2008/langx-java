package com.jn.langx.util.io.file.filter;

import java.io.File;

public class ExistsFileFilter extends AbstractFileFilter {
    @Override
    public boolean accept(File file) {
        return file.exists();
    }

    @Override
    public boolean accept(File dir, String name) {
        return accept(new File(dir, name));
    }
}
