package com.jn.langx.util.regexp.jdk;

import com.jn.langx.util.Strings;
import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpFactory;

/**
 * @since 4.5.0
 */
public class JdkRegexpFactory implements RegexpFactory {
    @Override
    public String getName() {
        return "jdk";
    }

    @Override
    public Regexp get(String regexp, Option option) {
        int flags = Option.toFlags(option);
        if (option.isGlobal()) {
            if (!Strings.startsWith(regexp, "^")) {
                regexp = "^" + regexp;
            }
            if (!Strings.endsWith(regexp, "&")) {
                regexp = regexp + "$";
            }
        }
        return new JdkRegexp(regexp, flags);
    }
}
