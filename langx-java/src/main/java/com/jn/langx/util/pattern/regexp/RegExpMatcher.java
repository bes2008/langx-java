package com.jn.langx.util.pattern.regexp;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.pattern.AbstractPatternMatcher;

import java.util.regex.Pattern;

public class RegExpMatcher extends AbstractPatternMatcher {

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
        setPatternExpression(regexp);
        setCaseSensitive(caseSensitive);
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
