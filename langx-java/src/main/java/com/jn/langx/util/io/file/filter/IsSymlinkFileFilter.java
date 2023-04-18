package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.io.file.FileSystems;

import java.io.File;

public class IsSymlinkFileFilter extends AbstractFileFilter {
    @Override
    public boolean accept(File file) {
        return FileSystems.isSymlink(file);
    }

    @Override
    public boolean accept(File dir, String name) {
        return accept(new File(dir, name));
    }
}
