package com.jn.langx.util.regexp;

/**
 * @since 4.5.0
 */
public interface Regexp {
    String getPattern();
    RegexpMatcher matcher(CharSequence input);
    String[] split(CharSequence input);
    Option getOption();
}
