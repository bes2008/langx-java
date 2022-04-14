package com.jn.langx.util.regexp;

/**
 * @since 4.5.0
 */
public interface Regexp {
    /**
     * Returns the pattern that is interpreted by this matcher.
     *
     * @return  The pattern for which this matcher was created
     */
    String getPattern();

    /**
     * 对指定的文本进行匹配
     *
     * @param text 要进行匹配的文本
     * @return 匹配器
     */
    RegexpMatcher matcher(CharSequence text);

    /**
     * 对指定的文本进行分割
     *
     * @param text 要被分割的文本
     * @return 分割后的字符串数组
     */
    String[] split(CharSequence text);

    /**
     * @return 正则表达式的option
     */
    Option getOption();
}
