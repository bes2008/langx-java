package com.jn.langx.util.regexp;

/**
 * @since 4.5.0
 */
public interface RegexpMatcher extends RegexpMatchResult{
    boolean matches();
    boolean find();
    RegexpMatcher appendReplacement(StringBuffer b, String replacement);
    void appendTail(StringBuffer b);
}
