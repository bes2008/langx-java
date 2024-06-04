package com.jn.langx.util.pattern.glob;

import com.jn.langx.io.resource.PathMatcher;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.pattern.AbstractPatternMatcher;

/**
 * @since 4.5.2
 *
 * <a href="https://mincong.io/2019/04/16/glob-expression-understanding/">glob expression</a>
 */
public class GlobMatcher extends AbstractPatternMatcher implements PathMatcher {
    private String globPattern;
    private GlobPattern glob;
    @Override
    public void setPatternExpression(String patternExpression) {
        this.globPattern = patternExpression;
    }

    @Override
    public Boolean matches(String string) {
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
