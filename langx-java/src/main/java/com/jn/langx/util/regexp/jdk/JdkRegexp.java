package com.jn.langx.util.regexp.jdk;

import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;

import java.util.regex.Pattern;

public class JdkRegexp implements Regexp {
    private Pattern pattern;

    public JdkRegexp(Pattern pattern){
        this.pattern = pattern;
    }

    public JdkRegexp(String pattern) {
        this.pattern = Pattern.compile(pattern, 0);
    }

    public JdkRegexp(String pattern, int flag) {
        this.pattern = Pattern.compile(pattern, flag);
    }

    @Override
    public String getPattern() {
        return this.pattern.pattern();
    }

    @Override
    public RegexpMatcher matcher(CharSequence input) {
        return new JdkMatcher(this.pattern.matcher(input));
    }

    @Override
    public String[] split(CharSequence input) {
        return this.pattern.split(input);
    }
}
