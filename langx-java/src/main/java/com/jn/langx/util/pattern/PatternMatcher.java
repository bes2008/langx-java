package com.jn.langx.util.pattern;

public interface PatternMatcher {
    /**
     * @param caseSensitive 大小写敏感
     */
    void setCaseSensitive(boolean caseSensitive);

    /**
     * 在匹配时，是否对 pattern先进行 trim 操作
     */
    void setTrimPattern(boolean trimPattern);


    void setPatternExpression(String patternExpression);

    /**
     * 测试是否匹配
     *
     * @param string
     * @return
     */
    boolean match(String string);
}
