package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.io.file.FileSystems;

import java.io.File;
import java.io.IOException;

public class IsSymlinkFileFilter extends AbstractFileFilter {
    @Override
    public boolean accept(File file) {
        try {
            return FileSystems.isSymlink(file);
        } catch (IOException ex) {
            return false;
        }
    }

    @Override
    public boolean accept(File dir, String name) {
        return accept(new File(dir, name));
    }
}
