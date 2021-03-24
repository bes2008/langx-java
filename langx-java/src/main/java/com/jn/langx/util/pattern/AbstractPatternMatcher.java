package com.jn.langx.util.pattern;

public abstract class AbstractPatternMatcher implements PatternMatcher{

    /**
     * 是否大小写敏感
     */
    protected boolean caseSensitive = false;
    /**
     * 匹配之前，是否对 pattern 进行 trim 操作
     */
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
