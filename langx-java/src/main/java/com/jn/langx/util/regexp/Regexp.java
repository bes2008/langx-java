package com.jn.langx.util.regexp;

public interface Regexp {
    String getPattern();
    RegexpMatcher matcher(CharSequence input);
    String[] split(CharSequence input);
}
