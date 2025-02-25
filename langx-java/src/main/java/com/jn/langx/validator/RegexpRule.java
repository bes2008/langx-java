package com.jn.langx.validator;

import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.regexp.Regexp;

/**
 * RegexpRule类实现了Rule接口，用于定义基于正则表达式的规则
 * 它使用了一个Regexp实例来匹配输入字符串，以验证字符串是否符合规则
 */
public class RegexpRule extends PredicateRule{
    public RegexpRule(final Regexp regexp, String errorMessage) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return regexp.matcher(value).matches();
            }
        }, Objs.useValueIfEmpty(errorMessage, "The value does not match the regular expression: " + regexp.getPattern()));
    }
}
