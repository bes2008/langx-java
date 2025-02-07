package com.jn.langx.util.pattern;

import com.jn.langx.Matcher;

/**
 * PatternMatcher 接口继承自 Matcher 接口，专门用于字符串的模式匹配
 * 它提供了一些设置匹配选项的方法，以及执行匹配测试的方法
 */
public interface PatternMatcher extends Matcher<String, Boolean> {
    /**
     * 设置是否忽略大小写
     * @param ignoreCase true 表示忽略大小写，false 表示区分大小写
     */
    void setIgnoreCase(boolean ignoreCase);

    /**
     * 设置是否在匹配前对模式字符串进行 trim 操作
     * @param trimPattern true 表示在匹配前对模式字符串进行 trim 操作，false 表示不进行处理
     */
    void setTrimPattern(boolean trimPattern);

    /**
     * 设置是否进行全局匹配
     * @param global true 表示进行全局匹配，false 表示进行单次匹配
     */
    void setGlobal(boolean global);

    /**
     * 设置模式表达式
     * @param patternExpression 模式表达式字符串
     */
    void setPatternExpression(String patternExpression);

    /**
     * 测试给定字符串是否与当前设置的模式匹配
     * @param string 要测试的字符串
     * @return 如果字符串与模式匹配，则返回 true；否则返回 false
     */
    Boolean matches(String string);
}
