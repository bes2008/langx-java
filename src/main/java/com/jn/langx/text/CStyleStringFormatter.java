package com.jn.langx.text;

public class CStyleStringFormatter extends AbstractStringTemplateFormatter {
    @Override
    public String format(String template, Object... args) {
        return String.format(template, args);
    }
}
