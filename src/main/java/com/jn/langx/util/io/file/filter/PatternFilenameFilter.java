package com.jn.langx.util.io.file.filter;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

import java.io.File;
import java.util.regex.Pattern;

public class PatternFilenameFilter extends AbstractFileFilter {
    private Pattern pattern;

    public PatternFilenameFilter(@NonNull String pattern) {
        this(Pattern.compile(Preconditions.checkNotNull(pattern)));
    }

    public PatternFilenameFilter(@NonNull Pattern pattern) {
        this.pattern = Preconditions.checkNotNull(pattern);
    }

    @Override
    public boolean accept(File f) {
        return accept(f.getParentFile(), f.getName());
    }

    @Override
    public boolean accept(File dir, String name) {
        return pattern.matcher(name).matches();
    }
}
