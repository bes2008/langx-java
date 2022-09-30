package com.jn.langx.util.pattern.regexp;

import com.jn.langx.io.resource.PathMatcher;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.pattern.AbstractPatternMatcher;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;


public class RegexpMatcher extends AbstractPatternMatcher implements PathMatcher {

    private String pattern;
    private Regexp regexp;

    public RegexpMatcher() {
    }

    public RegexpMatcher(String pattern) {
        this(pattern, false);
    }

    public RegexpMatcher(String pattern, boolean ignoreCase) {
        this(pattern, ignoreCase, true);
    }

    public RegexpMatcher(String pattern, boolean ignoreCase, boolean trimPattern) {
        setPatternExpression(pattern);
        setIgnoreCase(ignoreCase);
        setTrimPattern(trimPattern);
    }

    @Override
    public void setPatternExpression(String patternExpression) {
        this.pattern = patternExpression;
    }

    @Override
    public Boolean matches(String string) {
        Preconditions.checkNotEmpty(string, "the string is null or empty");
        Preconditions.checkNotEmpty(pattern, "the regexp is null or empty");

        if (regexp == null) {
            if (trimPattern) {
                pattern = Strings.trim(pattern);
                if (Strings.isEmpty(pattern)) {
                    throw new IllegalArgumentException("illegal regexp pattern");
                }
            }

            regexp = Regexps.createRegexp(pattern, this.option);
        }

        if (trimPattern) {
            string = Strings.trim(string);
        }
        return regexp.matcher(string).matches();
    }
}
