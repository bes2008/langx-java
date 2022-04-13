package com.jn.langx.regexp.joni;

import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpEngine;

public class JoniRegexpEngine implements RegexpEngine {
    @Override
    public String getName() {
        return "joni";
    }

    @Override
    public Regexp get(String pattern, Option option) {
        return new JoniRegexp(pattern, option);
    }
}
