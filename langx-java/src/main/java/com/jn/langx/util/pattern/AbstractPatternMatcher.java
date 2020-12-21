package com.jn.langx.util.pattern;

public abstract class AbstractPatternMatcher implements PatternMatcher{

    protected boolean caseSensitive = false;
    protected boolean trimPattern = true;

    @Override
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive =caseSensitive;
    }

    @Override
    public void setTrimPattern(boolean trimPattern) {
        this.trimPattern = trimPattern;
    }
}
