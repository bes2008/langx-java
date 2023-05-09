package com.jn.langx.util.pattern;

import com.jn.langx.Matcher;

public interface PatternMatcher extends Matcher<String, Boolean> {
    /**
     * @param ignoreCase 忽略大小写
     */
    void setIgnoreCase(boolean ignoreCase);

    /**
     * 在匹配时，是否对 pattern先进行 trim 操作
     */
    void setTrimPattern(boolean trimPattern);

    /**
     * 是否做全部匹配
     */
    void setGlobal(boolean global);

    void setPatternExpression(String patternExpression);

    /**
     * 测试是否匹配
     *
     */
    Boolean matches(String string);
}
