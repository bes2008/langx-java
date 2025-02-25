package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MacAddressRule extends RegexpRule {
    // key: separator, value: regexp
    private static final Map<String, Regexp> MAC_REGEX_MAP = new HashMap<String, Regexp>();
    static {
        MAC_REGEX_MAP.put("-", Regexps.compile("^([0-9A-Fa-f]{2}[-]){5}[0-9A-Fa-f]{2}$"));
        MAC_REGEX_MAP.put(":", Regexps.compile("^([0-9A-Fa-f]{2}[:]){5}[0-9A-Fa-f]{2}$"));
        MAC_REGEX_MAP.put("", Regexps.compile("^[0-9A-Fa-f]{12}$"));
    }

    public MacAddressRule(String separator, String errorMessage) {
        super(Objs.useValueIfEmpty(errorMessage, "Invalid MAC address"), findRegexps(separator));
    }

    private static Regexp[] findRegexps(String separator) {
        if(separator==null){
            return Collects.toArray(MAC_REGEX_MAP.values(),Regexp[].class);
        }
        Regexp regexp = MAC_REGEX_MAP.get(separator);
        if (regexp == null) {
            throw new IllegalArgumentException("Invalid separator: " + separator);
        }
        return Collects.toArray(Collects.immutableList(regexp), Regexp[].class);
    }

}
