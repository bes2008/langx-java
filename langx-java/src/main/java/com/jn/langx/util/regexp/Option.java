package com.jn.langx.util.regexp;

import java.util.regex.Pattern;

public class Option {
    /**
     * Enables case-insensitive matching.
     *
     * <p> By default, case-insensitive matching assumes that only characters
     * in the US-ASCII charset are being matched.  Unicode-aware
     * case-insensitive matching can be enabled by specifying the {@literal
     * #UNICODE_CASE} flag in conjunction with this flag.
     *
     * <p> Case-insensitive matching can also be enabled via the embedded flag
     * expression&nbsp;<tt>(?i)</tt>.
     *
     * <p> Specifying this flag may impose a slight performance penalty.  </p>
     */
    public static final int REGEXP_FLAG_CASE_INSENSITIVE = Pattern.CASE_INSENSITIVE;

    public static final int REGEXP_FLAG_MULTILINE = Pattern.MULTILINE;

    /**
     * 是否忽略大小写
     */
    private boolean ignoreCase = false;
    /**
     * 是否支持多行匹配
     */
    private boolean multiple = false;

    /**
     * 是否进行全局匹配
     */
    private boolean global = false;

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public static int toFlags(Option option) {
        int flags = 0;

        if (option.multiple) {
            flags |= REGEXP_FLAG_MULTILINE;
        }
        if (!option.isIgnoreCase()) {
            flags |= REGEXP_FLAG_CASE_INSENSITIVE;
        }
        return flags;
    }

    public static final Option DEFAULT = new Option();
}
