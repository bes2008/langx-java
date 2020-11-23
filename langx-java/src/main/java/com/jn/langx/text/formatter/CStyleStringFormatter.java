package com.jn.langx.text.formatter;

/**
 * %d
 * %s
 *
 * @see {@link String#format(String, Object...)}
 */
public class CStyleStringFormatter implements StringTemplateFormatter {
    @Override
    public String format(String template, Object... args) {
        return String.format(template, args);
    }
}
