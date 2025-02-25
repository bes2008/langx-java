package com.jn.langx.validator;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;

public class SensitiveWordsRule extends PredicateRule{
    public SensitiveWordsRule(String errorMessage, final String... sensitiveWords) {
        super(new Predicate<String>() {
            @Override
            public boolean test(final String value) {
                return Collects.anyMatch(new Predicate<String>() {
                    @Override
                    public boolean test(String sensitiveWord) {
                        return Strings.contains(value, sensitiveWord, true);
                    }
                },sensitiveWords );
            }
        }, Objs.useValueIfEmpty(errorMessage, "包含敏感词"));
    }
}
