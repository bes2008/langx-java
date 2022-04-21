package com.jn.langx.util.pattern.glob;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.pattern.AbstractPatternMatcher;

/**
 * @since 4.5.2
 */
public class GlobMatcher extends AbstractPatternMatcher {
    private String globPattern;
    private GlobPattern glob;
    @Override
    public void setPatternExpression(String patternExpression) {
        this.globPattern = patternExpression;
    }

    @Override
    public boolean match(String string) {
        Preconditions.checkNotEmpty(string, "the string is null or empty");
        Preconditions.checkNotEmpty(globPattern, "the regexp is null or empty");

        if (glob == null) {
            if (trimPattern) {
                globPattern = Strings.trim(globPattern);
                if (Strings.isEmpty(globPattern)) {
                    throw new IllegalArgumentException("illegal regexp pattern");
                }
            }

            glob = new GlobPattern(globPattern);
        }

        if (trimPattern) {
            string = Strings.trim(string);
        }
        return glob.matches(string);
    }
}
