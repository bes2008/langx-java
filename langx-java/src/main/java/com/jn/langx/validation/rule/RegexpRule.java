package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

/**
 * RegexpRule类实现了Rule接口，用于定义基于正则表达式的规则
 * 它使用了一个Regexp实例来匹配输入字符串，以验证字符串是否符合规则
 */
public class RegexpRule extends PredicateRule {
    public RegexpRule(String errorMessage, final String... regexps){
        this(errorMessage, Pipeline.of(regexps).map(new Function<String, Regexp>(){
            @Override
            public Regexp apply(String regexp) {
                return Regexps.compile(regexp);
            }
        }).toArray(Regexp[].class));
    }

    public RegexpRule(String errorMessage, final Regexp... regexps) {
        super(new Predicate<String>() {
            @Override
            public boolean test(final String value) {
                return Pipeline.of(regexps).anyMatch(new Predicate<Regexp>() {
                    @Override
                    public boolean test(Regexp regexp) {
                        return regexp.matcher(value).matches();
                    }
                });
            }
        }, Objs.useValueIfEmpty(errorMessage, "The value does not match the regular expressions"));
    }
}
