package com.jn.langx.util.pattern;

public abstract class AbstractPatternMatcher implements PatternMatcher{

    protected boolean caseSensitive = false;
    protected boolean trimPattern = true;
    protected boolean global = false;

    @Override
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive =caseSensitive;
    }

    @Override
    public void setTrimPattern(boolean trimPattern) {
        this.trimPattern = trimPattern;
    }

    @Override
    public void setGlobal(boolean global) {
        this.global = global;
    }
}
