package com.jn.langx.text;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.function.Function2;

import java.util.regex.Pattern;

public class StringTemplates {

    private static final Pattern orderPattern = Pattern.compile("\\{\\}");

    /**
     * format based order
     * @param template the string template
     * @return formatted string
     */
    public static String formatWithoutIndex(String template, Object... args){
        return format(template, orderPattern, new Function2<String, Object[], String>() {
            int index = -1;
            @Override
            public String apply(String matched, Object[] args) {
                index++;
                Object value = args[index];
                return Emptys.isNull(value) ? "" : value.toString();
            }
        }, args);
    }

    /**
     * format based index
     * @param template the string template
     * @param args args
     * @return formatted string
     */
    public static String format(String template, Object... args) {
        return format(template, "", null, args);
    }


    /**
     * custom formatter
     * @param template the string template
     * @param variablePattern variable pattern in template
     * @param valueGetter variable's value getter, will get value from args
     * @param args args
     * @return formatted string
     */
    public static String format(String template, String variablePattern, Function2<String, Object[], String> valueGetter, Object... args) {
        return new StringTemplate().variablePattern(variablePattern).using(template).with(valueGetter).format(args);
    }

    /**
     * custom formatter
     * @param template the string template
     * @param variablePattern variable pattern in template
     * @param valueGetter variable's value getter, will get value from args
     * @param args args
     * @return formatted string
     */
    public static String format(String template, Pattern variablePattern, Function2<String, Object[], String> valueGetter, Object... args) {
        return new StringTemplate().variablePattern(variablePattern).using(template).with(valueGetter).format(args);
    }


}
