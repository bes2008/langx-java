package com.jn.langx.util.regexp.jdk;

import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;

import java.util.regex.Pattern;

/**
 * @since 4.5.0
 */
public class JdkRegexp implements Regexp {
    private Pattern pattern;
    private Option option;

    public JdkRegexp(Pattern pattern){
        this.pattern = pattern;
        this.option = Option.buildOption(pattern.flags());
    }

    public JdkRegexp(String pattern) {
        this.pattern = Pattern.compile(pattern, 0);
    }

    public JdkRegexp(String pattern, int flags) {
        this.pattern = Pattern.compile(pattern, flags);
        this.option = Option.buildOption(flags);
    }

    @Override
    public Option getOption() {
        return option;
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