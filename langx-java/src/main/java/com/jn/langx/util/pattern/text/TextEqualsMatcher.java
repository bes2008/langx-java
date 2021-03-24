package com.jn.langx.util.pattern.text;

import com.jn.langx.util.Strings;

public class TextEqualsMatcher extends TextPatternMatcher {
    @Override
    public boolean doMatch(String string) {
        return Strings.equals(string, pattern, !caseSensitive);
    }
}
