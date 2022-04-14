package com.jn.langx.util.pattern;

import com.jn.langx.util.regexp.Option;

public abstract class AbstractPatternMatcher implements PatternMatcher{

    protected Option option = new Option();
    /**
     * 匹配之前，是否对 pattern 进行 trim 操作
     */
    protected boolean trimPattern = true;

    @Override
    public void setIgnoreCase(boolean ignoreCase) {
        this.option.setIgnoreCase(ignoreCase);
    }

    @Override
    public void setTrimPattern(boolean trimPattern) {
        this.trimPattern = trimPattern;
    }

    @Override
    public void setGlobal(boolean global) {
        this.option.setGlobal(global);
    }

}
