package com.jn.langx.util.io.file.filter;

import com.jn.langx.util.io.file.Filenames;

import java.io.File;

public class ValidFilenameFilter extends AbstractFileFilter {
    @Override
    public boolean accept(File file) {
        return Filenames.checkFilePath(file.getPath());
    }

    @Override
    public boolean accept(File dir, String name) {
        return accept(new File(dir, name));
    }
}
