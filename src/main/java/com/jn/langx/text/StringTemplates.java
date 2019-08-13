package com.jn.langx.text;

import java.util.regex.Pattern;

public class StringTemplates {

    public static String format(String template, Object[] args) {
        return format(template, "", args);
    }

    public static String format(String template, String variablePattern, Object[] args) {
        return new StringTemplate().variablePattern(variablePattern).using(template).with(args);
    }

    public static String format(String template, Pattern variablePattern, Object[] args) {
        return new StringTemplate().variablePattern(variablePattern).using(template).with(args);
    }
}
