package com.jn.langx.security.prevention.injection;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.util.struct.Holder;

import java.util.HashMap;
import java.util.Map;

import static java.util.regex.Matcher.quoteReplacement;

public class LogInjectionPreventionHandler implements Function<String, String> {
    private Map<Regexp, String> replacementMapping = new HashMap<Regexp, String>();

    public void addReplacement(Regexp pattern, String replacement) {
        replacementMapping.put(pattern, replacement);
    }

    public void addCLRFReplacement(String replacement) {
        replacement = Strings.isEmpty(replacement) ? "_" : replacement;
        String pattern = "[\r\n\f]";
        addReplacement(Regexps.createRegexp(pattern), replacement);
    }

    @Override
    public String apply(String input) {
        if (input == null) {
            return null;
        }
        final Holder<String> stringHolder = new Holder<String>(input);
        Collects.forEach(replacementMapping, new Consumer2<Regexp, String>() {
            @Override
            public void accept(Regexp pattern, String replacement) {
                String v = stringHolder.get();
                RegexpMatcher matcher = pattern.matcher(v);
                StringBuilder b = new StringBuilder();
                while (matcher.find()) {
                    matcher.appendReplacement(b, quoteReplacement(replacement));
                }
                matcher.appendTail(b);
                v = b.toString();
                stringHolder.set(v);
            }
        }, new Predicate2<Regexp, String>() {
            @Override
            public boolean test(Regexp pattern, String str) {
                return stringHolder.isEmpty();
            }
        });
        return stringHolder.get();
    }
}
