package com.jn.langx.text.grok;

import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

/**
 * @since 4.5.0
 */
public class Grok {
    public static final Regexp GROK_VARIABLE_REGEXP = Regexps.createRegexp(
            "%\\{"
                    + "(?<name>)"
                    + "(?<pattern>[A-z0-9]+)"
                    + "(?::(?<subname>[A-z0-9_:;,\\-\\/\\s\\.']+))?"
                    + ")"
                    + "(?:=(?<definition>"
                    + "(?:"
                    + "(?:[~{}]+|\\.+)+"
                    + ")+"
                    + ")"
                    + ")?"
                    + "\\}"
    );
}
