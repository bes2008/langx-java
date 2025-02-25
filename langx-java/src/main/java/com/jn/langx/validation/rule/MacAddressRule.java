package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

public class MacAddressRule extends RegexpRule {
    private static final Regexp[] MAC_REGEX = new Regexp[]{
            Regexps.compile("^([0-9A-Fa-f]{2}[-]){5}[0-9A-Fa-f]{2}$"),
            Regexps.compile("^([0-9A-Fa-f]{2}[:]){5}[0-9A-Fa-f]{2}$"),
            Regexps.compile("^[0-9A-Fa-f]{12}$")
    };

    public MacAddressRule(String errorMessage) {
        super(Objs.useValueIfEmpty(errorMessage, "Invalid MAC address"), MAC_REGEX);
    }
}
