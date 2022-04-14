package com.jn.langx.util.pattern.regexp;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.pattern.AbstractPatternMatcher;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;


public class RegexpMatcher extends AbstractPatternMatcher {

    private String regexp;
    private Regexp pattern;

    public RegexpMatcher() {
    }

    public RegexpMatcher(String regexp) {
        this(regexp, false);
    }

    public RegexpMatcher(String regexp, boolean ignoreCase) {
        this(regexp, ignoreCase, true);
    }

    public RegexpMatcher(String regexp, boolean ignoreCase, boolean trimPattern) {
        setPatternExpression(regexp);
        setIgnoreCase(ignoreCase);
        setTrimPattern(trimPattern);
    }

    @Override
    public void setPatternExpression(String patternExpression) {
        this.regexp = patternExpression;
    }

    @Override
    public boolean match(String string) {
        Preconditions.checkNotEmpty(string, "the string is null or empty");
        Preconditions.checkNotEmpty(regexp, "the regexp is null or empty");

        if (pattern == null) {
            if (trimPattern) {
                regexp = Strings.trim(regexp);
                if (Strings.isEmpty(regexp)) {
                    throw new IllegalArgumentException("illegal regexp pattern");
                }
            }

            pattern = Regexps.createRegexp(regexp, this.option);
        }

        if (trimPattern) {
            string = Strings.trim(string);
        }
        return pattern.matcher(string).matches();
    }
}
