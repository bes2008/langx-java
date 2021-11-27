package com.jn.langx.util.io.file.filter;

import java.io.File;

/**
 * @since 4.1.0
 */
public class BooleanFileFilter extends AbstractFileFilter {
    private boolean truth;

    public BooleanFileFilter(boolean truth) {
        this.truth = truth;
    }

    @Override
    public boolean accept(File e) {
        return this.truth;
    }

    @Override
    public boolean accept(File dir, String name) {
        return this.truth;
    }

    public static final BooleanFileFilter TRUE_FILTER = new BooleanFileFilter(true);
    public static final BooleanFileFilter FALSE_FILTER = new BooleanFileFilter(false);
}
