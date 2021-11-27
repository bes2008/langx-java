package com.jn.langx.util.io.file.filter;


import com.jn.langx.util.Strings;

import java.io.File;

/**
 * @since 4.1.0
 */
public class FilenameEqualsFilter extends AbstractFileFilter {
    // 是否忽略大小写
    private boolean ignoreCase = false;
    // 对比参照物
    private String ref;

    public FilenameEqualsFilter(String ref) {
        this(ref, false);
    }

    public FilenameEqualsFilter(String ref, boolean ignoreCase) {
        this.ref = ref;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean accept(File e) {
        return accept(e.getParentFile(), e.getName());
    }

    @Override
    public boolean accept(File dir, String name) {
        return Strings.equals(name, ref, ignoreCase);
    }
}
