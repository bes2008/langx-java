package com.jn.langx.util.regexp;

/**
 * @since 4.5.0
 */
public interface RegexpMatcher extends RegexpMatchResult{
    boolean matches();

    /**
     * 找到下一个匹配的子串
     * @return 是否找到
     */
    boolean find();
    RegexpMatcher appendReplacement(StringBuffer b, String replacement);
    void appendTail(StringBuffer b);
}
