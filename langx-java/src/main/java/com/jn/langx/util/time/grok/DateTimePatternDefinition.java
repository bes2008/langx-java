package com.jn.langx.util.time.grok;

import com.jn.langx.text.grok.pattern.AnonymousPatternDefinition;


public class DateTimePatternDefinition extends AnonymousPatternDefinition {
    private String format;

    public DateTimePatternDefinition(String expression, String format) {
        super(expression);
        setFormat(format);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
