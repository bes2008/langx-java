package com.jn.langx.security.prevention.injection;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.struct.Holder;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Matcher.quoteReplacement;

public class LogInjectionPreventionHandler implements Function<String, String> {
    private Map<Pattern, String> replacementMapping = new HashMap<Pattern, String>();

    public void addReplacement(Pattern pattern, String replacement) {
        replacementMapping.put(pattern, replacement);
    }

    public void addCLRFReplacement(String replacement) {
        replacement = Strings.isEmpty(replacement) ? "_" : replacement;
        String pattern = "[\r\n\f]";
        addReplacement(Pattern.compile(pattern), replacement);
    }

    @Override
    public String apply(String input) {
        if (input == null) {
            return null;
        }
        final Holder<String> stringHolder = new Holder<String>(input);
        Collects.forEach(replacementMapping, new Consumer2<Pattern, String>() {
            @Override
            public void accept(Pattern pattern, String replacement) {
                String v = stringHolder.get();
                Matcher matcher = pattern.matcher(v);
                StringBuffer b = new StringBuffer();
                while (matcher.find()) {
                    matcher.appendReplacement(b, quoteReplacement(replacement));
                }
                matcher.appendTail(b);
                v = b.toString();
                stringHolder.set(v);
            }
        }, new Predicate2<Pattern, String>() {
            @Override
            public boolean test(Pattern pattern, String str) {
                return stringHolder.isEmpty();
            }
        });
        return stringHolder.get();
    }
}
