package com.jn.langx.util.regexp;

import com.jn.langx.util.Objs;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * @since 4.5.0
 */
public class Option implements Serializable {
    private static final long serialVersionUID = 1L;
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
    public static final int CASE_INSENSITIVE = Pattern.CASE_INSENSITIVE;

    public static final int MULTILINE = Pattern.MULTILINE;

    /**
     * 是否忽略大小写，默认不忽略
     */
    private boolean ignoreCase = false;
    /**
     * 在要匹配的text是多行是，是否一行一行的匹配。如果要一行一行的匹配，设置为true；
     *
     * 默认值为 false，即将多行文本作为一个整体
     */
    private boolean multiline = false;

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

    public boolean isMultiline() {
        return multiline;
    }

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public static int toFlags(Option option) {
        int flags = 0;

        if (option.multiline) {
            flags |= MULTILINE;
        }
        if (option.isIgnoreCase()) {
            flags |= CASE_INSENSITIVE;
        }
        return flags;
    }

    public static final Option DEFAULT = new Option();

    public static final Option buildOption(int flags) {
        Option option = new Option();
        option.setMultiline(has(flags, MULTILINE));
        option.setIgnoreCase(has(flags, CASE_INSENSITIVE));
        return option;
    }

    public final int toFlags(){
        return toFlags(this);
    }

    /**
     * Indicates whether a particular flag is set or not.
     */
    public static boolean has(int flags, int f) {
        return (flags & f) != 0;
    }

    public static Option fromJavaScriptFlags(String flags) {
        Option opt = new Option();
        for (int i = 0; i < flags.length(); ++i) {
            char ch = flags.charAt(i);
            switch (ch) {
                case 'g':
                    opt.setGlobal(true);
                    break;
                case 'i':
                    opt.setIgnoreCase(true);
                    break;
                case 'm':
                    opt.setMultiline(true);
                    break;
                default: {
                }
            }
        }
        return opt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Option option = (Option) o;
        return ignoreCase == option.ignoreCase && multiline == option.multiline && global == option.global;
    }

    @Override
    public int hashCode() {
        return Objs.hash(ignoreCase, multiline, global);
    }
}
