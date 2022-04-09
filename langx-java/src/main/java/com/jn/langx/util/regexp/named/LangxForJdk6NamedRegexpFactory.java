package com.jn.langx.util.regexp.named;

import com.jn.langx.util.Strings;
import com.jn.langx.util.regexp.Option;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpFactory;

public class LangxForJdk6NamedRegexpFactory implements RegexpFactory {
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
        return NamedRegexp.compile(regexp, flags);
    }
}
