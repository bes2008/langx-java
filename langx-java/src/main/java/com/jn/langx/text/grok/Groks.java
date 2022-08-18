package com.jn.langx.text.grok;

import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @since 4.7.2
 */
public class Groks {

    /**
     * Extract Grok patter like %{FOO} to FOO, Also Grok pattern with semantic.
     */
    public static final Regexp GROK_PATTERN = Regexps.createRegexp(
            "%\\{"
                    + "(?<name>"
                    + "(?<pattern>[A-z0-9]+)"
                    + "(?::(?<subname>[A-z0-9_:;,\\-\\/\\s\\.']+))?"
                    + ")"
                    + "(?:=(?<definition>"
                    + "(?:"
                    + "(?:[^{}]+|\\.+)+"
                    + ")+"
                    + ")"
                    + ")?"
                    + "\\}");

    public static final Regexp NAMED_REGEX =  Regexps.createRegexp("\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>");

    public static Set<String> getNameGroups(String regex) {
        Set<String> namedGroups = new LinkedHashSet<String>();
        RegexpMatcher matcher = NAMED_REGEX.matcher(regex);
        while (matcher.find()) {
            namedGroups.add(matcher.group(1));
        }
        return namedGroups;
    }

}
