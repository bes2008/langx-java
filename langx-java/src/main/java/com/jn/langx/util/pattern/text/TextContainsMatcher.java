package com.jn.langx.util.pattern.text;

import com.jn.langx.util.Strings;

public class TextContainsMatcher extends TextPatternMatcher {
    @Override
    public boolean doMatch(String string) {
        return Strings.contains(string, pattern, !caseSensitive);
    }
}
