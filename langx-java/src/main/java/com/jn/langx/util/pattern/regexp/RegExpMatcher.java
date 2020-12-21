package com.jn.langx.util.pattern.regexp;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.pattern.PatternMatcher;

import java.util.regex.Pattern;

public class RegExpMatcher implements PatternMatcher {
    private boolean caseSensitive = false;
    private boolean trimPattern = true;
    private String regexp;
    private Pattern pattern;

    public RegExpMatcher() {
    }

    public RegExpMatcher(String regexp) {
        this(regexp, false);
    }

    public RegExpMatcher(String regexp, boolean caseSensitive) {
        this(regexp, caseSensitive, true);
    }

    public RegExpMatcher(String regexp, boolean caseSensitive, boolean trimPattern) {
        setRegexp(regexp);
        setCaseSensitive(caseSensitive);
        setTrimPattern(trimPattern);
    }

    @Override
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public void setTrimPattern(boolean trimPattern) {
        this.trimPattern = trimPattern;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public boolean match(String string) {
        Preconditions.checkNotEmpty(string, "the string is null or empty");
        Preconditions.checkNotEmpty(regexp, "the regexp is null or empty");

        if (pattern == null) {
            int flag = 0;
            if (caseSensitive) {
                flag = flag | Pattern.CASE_INSENSITIVE;
            }
            if (trimPattern) {
                regexp = Strings.trim(regexp);
            }
            pattern = Pattern.compile(regexp, flag);
        }

        return pattern.matcher(string).matches();
    }
}
