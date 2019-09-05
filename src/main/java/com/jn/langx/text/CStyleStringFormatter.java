package com.jn.langx.text;

public class CStyleStringFormatter implements StringTemplateFormatter {
    @Override
    public String format(String template, Object... args) {
        return String.format(template, args);
    }
}
