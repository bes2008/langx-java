package com.jn.langx.util.pattern.text;

import com.jn.langx.util.Strings;
import com.jn.langx.util.pattern.AbstractPatternMatcher;

/**
 * 单纯的文本匹配
 */
public abstract class TextPatternMatcher extends AbstractPatternMatcher {
    protected String pattern;

    @Override
    public void setPatternExpression(String patternExpression) {
        this.pattern = patternExpression;
    }

    @Override
    public boolean match(String string) {
        if (Strings.isEmpty(pattern) && (Strings.isEmpty(string))) {
            return true;
        }

        if (Strings.isEmpty(pattern) || Strings.isEmpty(string)) {
            return false;
        }
        return doMatch(string);

    }

    public abstract boolean doMatch(String string);
}
