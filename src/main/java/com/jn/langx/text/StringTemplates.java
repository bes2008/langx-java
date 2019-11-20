package com.jn.langx.text;

import com.jn.langx.util.function.Function2;

import java.util.Map;
import java.util.regex.Pattern;

public class StringTemplates {

    /**
     * %s, %d
     *
     * @param template the template
     * @param args     the arguments
     * @return formatted string
     * @see {@link String#format(String, Object...)}
     */
    public static String formatWithCStyle(String template, Object... args) {
        return new CStyleStringFormatter().format(template, args);
    }

    /**
     * format based paceholder: {}
     *
     * @param template the string template
     * @return formatted string
     */
    public static String formatWithPlaceholder(String template, Object... args) {
        return new PlaceholderStringFormatter().format(template, args);
    }


    /**
     * format based index: {0}, {1}, {2}
     * the index based on 0
     *
     * @param template the string template
     * @return formatted string
     * @see #format(String, Object...)
     */
    public static String formatWithIndex(String template, Object... args) {
        return format(template, args);
    }

    /**
     * format based index
     *
     * @param template the string template
     * @param args     args
     * @return formatted string
     * @see #formatWithIndex(String, Object...)
     */
    public static String format(String template, Object... args) {
        return new IndexStringFormatter().format(template, args);
    }

    /**
     * format based on a bean, the variable: ${fieldName}
     *
     * @param template the string template
     * @param bean     the bean
     * @param <T>      the bean type
     * @return formatted string
     */
    public static <T> String formatWithBean(String template, T bean) {
        return new FiledNameStyleStringFormatter().format(template, bean);
    }

    /**
     * format based on a map, the variable: ${key}
     *
     * @param template the string template
     * @return formatted string
     */
    public static String formatWithMap(String template, Map<String, ?> map) {
        return new MapBasedStringFormatter().format(template, map);
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
