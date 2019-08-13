package com.jn.langx.text;

import com.jn.langx.util.collect.function.Function2;

import java.util.regex.Pattern;

public class StringTemplates {

    public static String format(String template, Object[] args) {
        return format(template, "", null, args);
    }

    public static String format(String template, String variablePattern, Function2<String, Object[], String> valueGetter, Object[] args) {
        return new StringTemplate().variablePattern(variablePattern).using(template).with(valueGetter).format(args);
    }

    public static String format(String template, Pattern variablePattern, Function2<String, Object[], String> valueGetter, Object[] args) {
        return new StringTemplate().variablePattern(variablePattern).using(template).with(valueGetter).format(args);
    }
}
