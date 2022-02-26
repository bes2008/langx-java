package com.jn.langx.text.stringtemplate;

/**
 * %d
 * %s
 *
 * @see String#format(String, Object...)
 */
public class CStyleStringFormatter implements StringTemplateFormatter {
    @Override
    public String format(String template, Object... args) {
        return String.format(template, args);
    }
}
