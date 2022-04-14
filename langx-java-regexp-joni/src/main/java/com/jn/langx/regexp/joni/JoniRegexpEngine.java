package com.jn.langx.regexp.joni;

import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpEngine;

/**
 * @since 4.5.0
 */
public class JoniRegexpEngine implements RegexpEngine {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "joni";
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Regexp get(String pattern, Option option) {
        return new JoniRegexp(pattern, option);
    }
}
