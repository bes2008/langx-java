package com.jn.langx.text;

import com.jn.langx.util.function.Function2;

import java.util.regex.Pattern;

public class StringTemplates {

    private static final Pattern orderPattern = Pattern.compile("\\{\\}");

    public static String formatWithCStyle(String template, Object... args) {
        return new CStyleStringFormatter().format(template, args);
    }

    /**
     * format based order
     *
     * @param template the string template
     * @return formatted string
     */
    public static String formatWithPlaceholder(String template, Object... args) {
        return new PlaceholderStringFormatter().format(template, args);
    }

    /**
     * format based index
     *
     * @param template the string template
     * @param args     args
     * @return formatted string
     */
    public static String format(String template, Object... args) {
        return new IndexStringFormatter().format(template, args);
    }


    /**
     * custom formatter
     *
     * @param template        the string template
     * @param variablePattern variable pattern in template
     * @param valueGetter     variable's value getter, will get value from args
     * @param args            args
     * @return formatted string
     */
    public static String format(String template, String variablePattern, Function2<String, Object[], String> valueGetter, Object... args) {
        return new CustomPatternStringFormatter(variablePattern, valueGetter).format(template, args);
    }

    /**
     * custom formatter
     *
     * @param template        the string template
     * @param variablePattern variable pattern in template
     * @param valueGetter     variable's value getter, will get value from args
     * @param args            args
     * @return formatted string
     */
    public static String format(String template, Pattern variablePattern, Function2<String, Object[], String> valueGetter, Object... args) {
        return new CustomPatternStringFormatter(variablePattern, valueGetter).format(template, args);
    }


}
